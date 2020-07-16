/*
* Software Name : IoT for technicians
* Version: 1.0
*
* Copyright (c) 2020 Orange
*
* This software is distributed under the Apache License, Version 2.0,
* the text of which is available at http://www.apache.org/licenses/
* or see the "license.txt" file for more details.
*
*/

package com.company.iotfortechnicians.findmydevice;

import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class DeviceSearchParameters implements Serializable {

    private static final long serialVersionUID = -8398357861598847609L;

    @Setter(AccessLevel.NONE)
    private Map<FindMyDeviceSearchType, String> filters;
    private FindMyDeviceSortBy sortBy;
    private int limit;
    private int offset;

    public DeviceSearchParameters() {
        this.filters = new HashMap<>();
        this.limit = Constants.LIMIT_VALUE;
        this.offset = 0;
        this.sortBy = FindMyDeviceSortBy.DEVICE_LAST_CONTACT;
    }

    public void addFilter(FindMyDeviceSearchType searchType, String searchValue) {
        if (StringUtils.isNoneEmpty(searchValue)) {
            filters.put(searchType, searchValue);
        } else {
            filters.remove(searchType);
        }
    }

    public void clearFilters() {
        filters.clear();
    }

    public FindMyDeviceSortBy getActualSortingKey() {
        return this.sortBy;
    }

    public int getFiltersCount() {
        return filters.size();
    }

    public Map<String, String> toRequestParams() {
        HashMap<String, String> params = new HashMap<>();

        for (Map.Entry<FindMyDeviceSearchType, String> entry : filters.entrySet()) {
            FindMyDeviceSearchType searchType = entry.getKey();
            String searchValue = entry.getValue();

            if (FindMyDeviceSearchType.DEVICE_NAME.equals(searchType)) {
                List<String> strings = Arrays.asList(Constants.STAR, searchValue, Constants.STAR);
                searchValue = StringUtils.join(strings);
            }

            params.put(searchType.getRequestParameter(), searchValue);
        }

        params.put(Constants.LIMIT, String.valueOf(limit));
        params.put(Constants.OFFSET, String.valueOf(offset));
        params.put(Constants.SORT, sortBy.getRequestParameter());

        return params;
    }
}

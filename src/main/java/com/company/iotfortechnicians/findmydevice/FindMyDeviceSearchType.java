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

import androidx.annotation.StringRes;

import com.company.iotfortechnicians.IOTApplication;
import com.company.iotfortechnicians.R;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FindMyDeviceSearchType {
    DEVICE_ID(R.string.search_type_device_id, "id"),
    DEVICE_NAME(R.string.search_type_device_name, "name"),
    DEVICE_GROUP(R.string.search_type_device_group, "groupPath"),
    INTERFACE_STATUS(R.string.search_type_interface_status, "interfaces.status");

    private static final Map<Integer, FindMyDeviceSearchType> BY_RESOURCE_ID = new HashMap<>();

    static {
        for (FindMyDeviceSearchType e : values()) {
            BY_RESOURCE_ID.put(e.resourceId, e);
        }
    }

    @StringRes
    private final int resourceId;
    @Getter
    private final String requestParameter;

    @Override
    public String toString() {
        return IOTApplication.getAppContext()
                .getResources()
                .getString(resourceId);
    }

    public static FindMyDeviceSearchType valueOfResourceId(@StringRes int stringResId) {
        return BY_RESOURCE_ID.get(stringResId);
    }
}

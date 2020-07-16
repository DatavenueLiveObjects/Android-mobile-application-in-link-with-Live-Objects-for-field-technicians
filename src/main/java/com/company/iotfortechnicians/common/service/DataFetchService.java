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

package com.company.iotfortechnicians.common.service;

import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.DynamicLocationFetchInterface;
import com.company.iotfortechnicians.common.FetchDynamicLocationCallback;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.search.payload.SearchDTO;
import com.company.iotfortechnicians.common.util.StringUtils;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class DataFetchService {

    protected static String getPayloadQuery(int limit, String deviceId) {
        return "{" +
            "\"from\":0,\"size\":" + limit + ",\"sort\":[{\"timestamp\":\"desc\"}]," +
            "\"query\":{\"bool\":{" +
            "\"must\":[" +
                "{\"wildcard\":{\"metadata.source\":\"" + deviceId + "\"}}," +
                "{\"exists\":{\"field\":\"location\"}}" +
            "]" +
            "}}" +
        "}";
    }

    private static Call<SearchDTO> getPayloadQueryRequest(int limit, String deviceId) {
        if (StringUtils.isNoneEmpty(deviceId)) {
            DataSearchService dataSearchService = ApiClient.createApiService(DataSearchService.class);

            RequestBody body = RequestBody.create(getPayloadQuery(limit, deviceId), MediaType.parse("application/json"));
            return dataSearchService.searchForPayload(body);
        } else
            return null;
    }

    public static void fetchPayload(String deviceId, AbstractIOTApiCallbackFragment<SearchDTO> fragmentInterface) {
        Call<SearchDTO> queryRequest = getPayloadQueryRequest(Constants.PAYLOAD_MESSAGES_COUNT, deviceId);
        if (queryRequest != null)
            queryRequest.enqueue(fragmentInterface);
    }

    public static void fetchDynamicLocation(String deviceId, DynamicLocationFetchInterface fragmentInterface) {
        if (StringUtils.isNoneEmpty(deviceId)) {
            Call<SearchDTO> queryRequest = getPayloadQueryRequest(1, deviceId);
            if (queryRequest != null)
                queryRequest.enqueue(new FetchDynamicLocationCallback(fragmentInterface));
        } else
            fragmentInterface.onDynamicLocationFetched(null);
    }
}

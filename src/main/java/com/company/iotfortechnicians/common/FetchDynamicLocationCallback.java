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

package com.company.iotfortechnicians.common;

import com.company.iotfortechnicians.common.dto.devicemanagement.LocationDTO;
import com.company.iotfortechnicians.common.dto.search.SourceDTO;
import com.company.iotfortechnicians.common.dto.search.payload.HitDTO;
import com.company.iotfortechnicians.common.dto.search.payload.HitsDTO;
import com.company.iotfortechnicians.common.dto.search.payload.SearchDTO;

import java.util.List;

import lombok.RequiredArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiredArgsConstructor
public class FetchDynamicLocationCallback implements Callback<SearchDTO> {

    private final DynamicLocationFetchInterface fragmentInterface;

    protected void processData(SearchDTO data) {
        HitsDTO hits = data.getHits();
        List<HitDTO> hitList = hits.getHits();
        if (!hitList.isEmpty()) {
            HitDTO hit = hitList.get(0);
            SourceDTO source = hit.getSource();
            LocationDTO location = source.getLocation();

            fragmentInterface.onDynamicLocationFetched(location);
        } else
            fragmentInterface.onDynamicLocationFetched(null);
    }

    @Override
    public void onResponse(Call<SearchDTO> call, Response<SearchDTO> response) {
        if (response.isSuccessful()) {
            SearchDTO body = response.body();
            processData(body);
        } else {
            fragmentInterface.onResponseError(response.code(), response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<SearchDTO> call, Throwable t) {
        fragmentInterface.onFailure(t);
    }
}

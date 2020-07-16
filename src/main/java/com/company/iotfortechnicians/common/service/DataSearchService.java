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

import com.company.iotfortechnicians.common.dto.search.SourceDTO;
import com.company.iotfortechnicians.common.dto.search.payload.SearchDTO;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DataSearchService {

    @POST("v0/data/search/hits")
    Call<List<SourceDTO>> searchHits(@Body RequestBody bod);

    @POST("v0/data/search")
    Call<SearchDTO> searchForPayload(@Body RequestBody bod);
}

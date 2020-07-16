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

import com.company.iotfortechnicians.common.dto.stream.StoredDataMessageDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StreamService {

    @GET("v0/data/streams/{streamId}?limit=1")
    Call<List<StoredDataMessageDTO>> getStreams(@Path("streamId") String streamId);
}

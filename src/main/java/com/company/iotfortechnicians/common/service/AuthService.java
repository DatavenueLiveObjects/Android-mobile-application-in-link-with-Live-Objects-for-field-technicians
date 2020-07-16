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

import com.company.iotfortechnicians.common.dto.auth.AuthRequestDTO;
import com.company.iotfortechnicians.common.dto.auth.AuthResponseDTO;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("v0/auth")
    Call<AuthResponseDTO> login(@Body AuthRequestDTO loginRequestDTO);

    @POST("v0/logout?cookie=false")
    Call<String> logout(@Body HashMap<String, String> request);
}

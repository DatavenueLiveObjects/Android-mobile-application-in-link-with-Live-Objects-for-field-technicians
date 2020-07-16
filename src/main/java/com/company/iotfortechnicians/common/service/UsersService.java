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

import com.company.iotfortechnicians.common.dto.users.UserDTO;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UsersService {

    @GET("v0/users/me")
    Call<UserDTO> getMe();
}

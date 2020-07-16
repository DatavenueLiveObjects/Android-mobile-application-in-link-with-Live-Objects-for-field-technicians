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

import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.GroupDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.InterfaceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.PatchInterfaceStatusRequestDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.PatchPropertiesRequestDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.PatchTagsRequestDTO;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface DeviceManagementService {

    @GET("v1/deviceMgt/devices/{deviceId}")
    Call<DeviceDTO> getDevice(@Path("deviceId") String deviceId);

    @GET("v1/deviceMgt/devices?fields=name&fields=interfaces")
    Call<List<DeviceDTO>> getDevices(@QueryMap Map<String, String> options);

    @PATCH("v1/deviceMgt/devices/{deviceId}")
    Call<DeviceDTO> patchDevice(@Path("deviceId") String deviceId, @Body DeviceDTO deviceDTO);

    @PATCH("v1/deviceMgt/devices/{deviceId}")
    Call<DeviceDTO> patchProperties(@Path("deviceId") String deviceId, @Body PatchPropertiesRequestDTO patchPropertiesRequestDTO);

    @PATCH("v1/deviceMgt/devices/{deviceId}")
    Call<DeviceDTO> patchTags(@Path("deviceId") String deviceId, @Body PatchTagsRequestDTO patchTagsRequestDTO);

    @PATCH("v1/deviceMgt/devices/{deviceId}/interfaces/{connector}:{nodeId}")
    Call<InterfaceDTO> patchInterfaceStatus(@Path("deviceId") String deviceId, @Path("connector") String connector,
                                            @Path("nodeId") String nodeId, @Body PatchInterfaceStatusRequestDTO patchInterfaceStatusRequestDTO);

    @GET("v1/deviceMgt/groups?limit=1000&offset=0")
    Call<List<GroupDTO>> getGroups();
}

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

package com.company.iotfortechnicians.common.dto.devicemanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PatchInterfaceStatusRequestDTO implements Serializable {

    private static final long serialVersionUID = 7361916977204780854L;

    @SerializedName("enabled")
    @Expose
    private final boolean enabled;

    @SerializedName("deviceId")
    @Expose
    private final String deviceId;
}

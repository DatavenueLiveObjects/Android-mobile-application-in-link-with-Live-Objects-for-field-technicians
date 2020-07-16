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

package com.company.iotfortechnicians.common.dto.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class RateLimitDTO implements Serializable {

    private static final long serialVersionUID = 3676459922413947404L;

    @SerializedName("mqttBridgeWindowSize")
    @Expose
    private Integer mqttBridgeWindowSize;
    @SerializedName("mqttBridgeMaxMessages")
    @Expose
    private Integer mqttBridgeMaxMessages;
    @SerializedName("mqttDeviceWindowSize")
    @Expose
    private Integer mqttDeviceWindowSize;
    @SerializedName("mqttDeviceMaxMessages")
    @Expose
    private Integer mqttDeviceMaxMessages;
    @SerializedName("httpWindowSize")
    @Expose
    private Integer httpWindowSize;
    @SerializedName("httpMaxCalls")
    @Expose
    private Integer httpMaxCalls;

}

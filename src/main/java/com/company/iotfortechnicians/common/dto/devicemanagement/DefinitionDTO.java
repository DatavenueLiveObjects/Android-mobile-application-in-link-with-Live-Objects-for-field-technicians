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
import java.util.Map;

import lombok.Data;

@Data
public class DefinitionDTO implements Serializable {

    private static final long serialVersionUID = -1281229374025605168L;

    @SerializedName("devEUI")
    @Expose
    private String devEUI;
    @SerializedName("activationType")
    @Expose
    private String activationType;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("encoding")
    @Expose
    private String encoding;
    @SerializedName("connectivityOptions")
    @Expose
    private Map<String, String> connectivityOptions;
    @SerializedName("customConnectivityPlan")
    @Expose
    private String customConnectivityPlan;
    @SerializedName("appEUI")
    @Expose
    private String appEUI;
    @SerializedName("clientId")
    @Expose
    private String clientId;
    @SerializedName("msisdn")
    @Expose
    private String msisdn;
    @SerializedName("serverPhoneNumber")
    @Expose
    private String serverPhoneNumber;

}

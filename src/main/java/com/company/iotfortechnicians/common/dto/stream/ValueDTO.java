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


package com.company.iotfortechnicians.common.dto.stream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.company.iotfortechnicians.common.dto.search.StatusDTO;
import com.company.iotfortechnicians.common.dto.search.WifiDTO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ValueDTO implements Serializable {

    private static final long serialVersionUID = 6447095072408442967L;

    @SerializedName("payload")
    @Expose
    private String payload;
    @SerializedName("battery")
    @Expose
    private BatteryDTO battery;
    @SerializedName("messageType")
    @Expose
    private String messageType;
    @SerializedName("trackerMode")
    @Expose
    private String trackerMode;
    @SerializedName("version")
    @Expose
    private VersionDTO version;
    @SerializedName("lastResetReason")
    @Expose
    private Integer lastResetReason;
    @SerializedName("wifis")
    @Expose
    private List<WifiDTO> wifis = new ArrayList<>();
    @SerializedName("triggerEvent")
    @Expose
    private String triggerEvent;
    @SerializedName("status")
    @Expose
    private StatusDTO status;

}

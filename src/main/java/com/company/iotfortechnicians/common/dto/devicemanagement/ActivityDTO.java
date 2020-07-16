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

import lombok.Data;

@Data
public class ActivityDTO implements Serializable {

    private static final long serialVersionUID = -439594808464247739L;

    @SerializedName("devAddr")
    @Expose
    private String devAddr;
    @SerializedName("lastActivationTs")
    @Expose
    private String lastActivationTs;
    @SerializedName("lastDeactivationTs")
    @Expose
    private String lastDeactivationTs;
    @SerializedName("lastSignalLevel")
    @Expose
    private Integer lastSignalLevel;
    @SerializedName("lastDlFcnt")
    @Expose
    private Integer lastDlFcnt;
    @SerializedName("lastUlFcnt")
    @Expose
    private Integer lastUlFcnt;
    @SerializedName("lastBatteryLevel")
    @Expose
    private String lastBatteryLevel;

}

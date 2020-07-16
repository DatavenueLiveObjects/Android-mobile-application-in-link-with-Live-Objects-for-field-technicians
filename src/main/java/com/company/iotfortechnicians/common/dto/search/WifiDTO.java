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


package com.company.iotfortechnicians.common.dto.search;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class WifiDTO implements Serializable {

    private static final long serialVersionUID = 2679875396587023440L;

    @SerializedName("rssi")
    @Expose
    private String rssi;
    @SerializedName("bssid")
    @Expose
    private String bssid;

}

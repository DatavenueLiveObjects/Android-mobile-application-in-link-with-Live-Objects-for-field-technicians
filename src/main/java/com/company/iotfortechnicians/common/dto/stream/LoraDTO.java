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

import com.company.iotfortechnicians.common.dto.devicemanagement.LocationDTO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class LoraDTO implements Serializable {

    private static final long serialVersionUID = 1903118954541442678L;

    @SerializedName("devEUI")
    @Expose
    private String devEUI;
    @SerializedName("port")
    @Expose
    private Integer port;
    @SerializedName("fcnt")
    @Expose
    private Integer fcnt;
    @SerializedName("rssi")
    @Expose
    private Integer rssi;
    @SerializedName("snr")
    @Expose
    private Double snr;
    @SerializedName("esp")
    @Expose
    private Double esp;
    @SerializedName("sf")
    @Expose
    private Integer sf;
    @SerializedName("signalLevel")
    @Expose
    private Integer signalLevel;
    @SerializedName("ack")
    @Expose
    private Boolean ack;
    @SerializedName("messageType")
    @Expose
    private String messageType;
    @SerializedName("location")
    @Expose
    private LocationDTO location;
    @SerializedName("gatewayCnt")
    @Expose
    private Integer gatewayCnt;

}

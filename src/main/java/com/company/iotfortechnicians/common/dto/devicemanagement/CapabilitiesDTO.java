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
public class CapabilitiesDTO implements Serializable {

    private static final long serialVersionUID = 5995264939758217446L;

    @SerializedName("configuration")
    @Expose
    private CapabilityDTO configuration;
    @SerializedName("command")
    @Expose
    private CapabilityDTO command;
    @SerializedName("resource")
    @Expose
    private CapabilityDTO resource;
}

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


package com.company.iotfortechnicians.common.dto.users;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Preferences implements Serializable {

    private static final long serialVersionUID = -18591872730177603L;

    @SerializedName("portal_datazone_showdetails")
    @Expose
    private Boolean portalDatazoneShowdetails;
    @SerializedName("portal_device_lora_uplink_showdetails")
    @Expose
    private Boolean portalDeviceLoraUplinkShowdetails;

}

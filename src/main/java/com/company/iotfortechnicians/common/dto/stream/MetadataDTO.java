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

import com.company.iotfortechnicians.common.dto.devicemanagement.GroupDTO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class MetadataDTO implements Serializable {

    private static final long serialVersionUID = 5461556978827008568L;

    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("group")
    @Expose
    private GroupDTO group;
    @SerializedName("encoding")
    @Expose
    private String encoding;
    @SerializedName("connector")
    @Expose
    private String connector;
    @SerializedName("network")
    @Expose
    private NetworkDTO network;

}

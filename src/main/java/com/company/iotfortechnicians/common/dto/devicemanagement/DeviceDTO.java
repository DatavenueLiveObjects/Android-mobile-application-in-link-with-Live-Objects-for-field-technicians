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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class DeviceDTO implements Serializable {

    private static final long serialVersionUID = 5995264939758217446L;

    public DeviceDTO() {
        interfaces = new ArrayList<>();
        tags = new ArrayList<>();
        properties = new LinkedHashMap<>();
    }

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tags")
    @Expose
    private List<String> tags;
    @SerializedName("group")
    @Expose
    private GroupDTO group;
    @SerializedName("interfaces")
    @Expose
    private List<InterfaceDTO> interfaces;
    @SerializedName("defaultDataStreamId")
    @Expose
    private String defaultDataStreamId;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("properties")
    @Expose
    private Map<String, String> properties;
    @SerializedName("activityState")
    @Expose
    private String activityState;
    @Expose
    private LocationDTO dynamicLocation;
}

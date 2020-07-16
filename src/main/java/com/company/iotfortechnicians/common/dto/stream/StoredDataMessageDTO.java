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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.company.iotfortechnicians.common.dto.devicemanagement.LocationDTO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class StoredDataMessageDTO implements Serializable {

    private static final long serialVersionUID = 2213913658053316464L;

    public StoredDataMessageDTO() {
        tags = new ArrayList<>();
        extra = new LinkedHashMap<>();
    }

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("streamId")
    @Expose
    private String streamId;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("location")
    @Expose
    private LocationDTO location;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("value")
    @Expose
    private ValueDTO value;
    @SerializedName("tags")
    @Expose
    private List<String> tags;
    @SerializedName("extra")
    @Expose
    private Map<String, String> extra;
    @SerializedName("metadata")
    @Expose
    private MetadataDTO metadata;
    @SerializedName("created")
    @Expose
    private String created;

}

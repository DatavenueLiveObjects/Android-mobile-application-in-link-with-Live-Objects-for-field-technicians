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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class InterfaceDTO implements Serializable {

    private static final long serialVersionUID = 5640974785047299635L;

    public InterfaceDTO() {
        locations = new ArrayList<>();
    }

    public InterfaceDTO(boolean enabled, InterfaceStatus status) {
        this();
        this.enabled = enabled;
        this.status = status;
    }

    @SerializedName("connector")
    @Expose
    private String connector;
    @SerializedName("nodeId")
    @Expose
    private String nodeId;
    @SerializedName("enabled")
    @Expose
    private boolean enabled;
    @SerializedName("status")
    @Expose
    private InterfaceStatus status;
    @SerializedName("definition")
    @Expose
    private DefinitionDTO definition;
    @SerializedName("lastContact")
    @Expose
    private String lastContact;
    @SerializedName("activity")
    @Expose
    private ActivityDTO activity;
    @SerializedName("locations")
    @Expose
    private List<LocationDTO> locations;

}

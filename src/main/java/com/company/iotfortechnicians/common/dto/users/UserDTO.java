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
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 3072687228365718961L;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("tenantId")
    @Expose
    private String tenantId;
    @SerializedName("roles")
    @Expose
    private List<String> roles = new ArrayList<>();
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("portalData")
    @Expose
    private PortalData portalData;

}

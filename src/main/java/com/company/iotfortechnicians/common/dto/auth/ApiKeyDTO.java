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


package com.company.iotfortechnicians.common.dto.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ApiKeyDTO implements Serializable {

    private static final long serialVersionUID = -7948374183278916289L;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("parentId")
    @Expose
    private String parentId;
    @SerializedName("tenantId")
    @Expose
    private String tenantId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("nonce")
    @Expose
    private String nonce;
    @SerializedName("creationTs")
    @Expose
    private Long creationTs;
    @SerializedName("from")
    @Expose
    private Long from;
    @SerializedName("to")
    @Expose
    private Long to;
    @SerializedName("lastActivity")
    @Expose
    private Long lastActivity;
    @SerializedName("sessionTTL")
    @Expose
    private Long sessionTTL;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("rateLimit")
    @Expose
    private RateLimitDTO rateLimitDTO;
    @SerializedName("roles")
    @Expose
    private List<String> roles = new ArrayList<>();
    @SerializedName("clientCert")
    @Expose
    private ClientCertDTO clientCertDTO;
    @SerializedName("usurpedKey")
    @Expose
    private Boolean usurpedKey;
    @SerializedName("masterKey")
    @Expose
    private Boolean masterKey;
    @SerializedName("sessionKey")
    @Expose
    private Boolean sessionKey;
    @SerializedName("expired")
    @Expose
    private Boolean expired;

}

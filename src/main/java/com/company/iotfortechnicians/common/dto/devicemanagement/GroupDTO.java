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

import androidx.annotation.NonNull;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GroupDTO implements Serializable {

    private static final long serialVersionUID = -5925340945578042489L;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("pathNode")
    @Expose
    private String pathNode;
    @SerializedName("parentId")
    @Expose
    private String parentId;
    @SerializedName("description")
    @Expose
    private String description;

    @NonNull
    @Override
    public String toString() {
        return pathNode;
    }
}

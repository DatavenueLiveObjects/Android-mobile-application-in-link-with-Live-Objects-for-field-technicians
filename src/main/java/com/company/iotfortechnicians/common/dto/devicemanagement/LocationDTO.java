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

import androidx.annotation.Nullable;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class LocationDTO implements Serializable {

    private static final long serialVersionUID = -1403779417387974382L;

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("alt")
    @Expose
    private Double alt;
    @SerializedName("accuracy")
    @Expose
    private Double accuracy;
    @SerializedName("lastUpdateTs")
    @Expose
    private String lastUpdateTs;
    @SerializedName("provider")
    @Expose
    private String provider;

    public LocationDTO(Double lat, Double lon, Double alt) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
    }

    @Nullable
    @Override
    public String toString() {
        if (lat == null || lon == null)
            return null;

        Double tAlt = alt;
        if (tAlt == null)
            tAlt = 0.0;

        return "" + lat + ';' + lon + ';' + tAlt;
    }
}

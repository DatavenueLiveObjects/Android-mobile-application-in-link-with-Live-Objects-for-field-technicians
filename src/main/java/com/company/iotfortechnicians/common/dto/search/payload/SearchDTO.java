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


package com.company.iotfortechnicians.common.dto.search.payload;

import com.company.iotfortechnicians.common.dto.search.AggregationsDTO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class SearchDTO implements Serializable {

    private static final long serialVersionUID = -6998954239052682927L;

    public SearchDTO() {
        this.hits = new HitsDTO();
    }

    @SerializedName("took")
    @Expose
    private Integer took;
    @SerializedName("hits")
    @Expose
    private HitsDTO hits;
    @SerializedName("aggregations")
    @Expose
    private AggregationsDTO aggregations;

}

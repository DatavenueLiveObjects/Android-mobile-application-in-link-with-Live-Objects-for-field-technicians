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


package com.company.iotfortechnicians.common.dto.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class HitsDTO implements Serializable {

    private static final long serialVersionUID = 2503633300827159488L;

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("hits")
    @Expose
    private List<HitDTO> hits = new ArrayList<>();

}

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

import com.google.gson.annotations.Expose;
import com.company.iotfortechnicians.common.dto.search.SourceDTO;

import java.io.Serializable;

import lombok.Data;

@Data
public class HitDTO implements Serializable {

    private static final long serialVersionUID = -6033107298509752313L;

    @Expose
    private SourceDTO source;

    @Expose
    private String payload;

}

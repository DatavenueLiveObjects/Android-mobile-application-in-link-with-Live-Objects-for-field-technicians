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
class ListTagDTO implements Serializable {

    private static final long serialVersionUID = 6549379595204837251L;

    @SerializedName("doc_count_error_upper_bound")
    @Expose
    private Integer docCountErrorUpperBound;
    @SerializedName("sum_other_doc_count")
    @Expose
    private Integer sumOtherDocCount;
    @SerializedName("buckets")
    @Expose
    private List<BucketDTO> buckets = new ArrayList<>();

}

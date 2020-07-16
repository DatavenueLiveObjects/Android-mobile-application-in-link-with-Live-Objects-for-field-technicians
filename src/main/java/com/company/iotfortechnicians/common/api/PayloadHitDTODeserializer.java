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

package com.company.iotfortechnicians.common.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.company.iotfortechnicians.common.dto.search.SourceDTO;
import com.company.iotfortechnicians.common.dto.search.payload.HitDTO;
import com.company.iotfortechnicians.common.util.StringUtils;

import java.lang.reflect.Type;

public class PayloadHitDTODeserializer implements JsonDeserializer<HitDTO> {

    private static final String SOURCE = "_source";

    @Override
    public HitDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json.isJsonObject()) {
            JsonObject asJsonObject = json.getAsJsonObject();
            String payload = getAsString(asJsonObject, SOURCE);
            HitDTO hitDTO = new HitDTO();

            if (StringUtils.isNoneEmpty(payload)) {
                hitDTO.setPayload(payload);

                Gson g = new Gson();
                SourceDTO source = g.fromJson(payload, SourceDTO.class);
                hitDTO.setSource(source);
            }

            return hitDTO;
        }
        return null;
    }

    private String getAsString(JsonObject asJsonObject, String value) {
        return asJsonObject.has(value) ? asJsonObject.get(value).toString() : StringUtils.EMPTY;
    }
}

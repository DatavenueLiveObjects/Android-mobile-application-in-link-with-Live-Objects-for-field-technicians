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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.dto.auditlog.AuditLogMessageDTO;
import com.company.iotfortechnicians.common.util.StringUtils;

import java.lang.reflect.Type;

public class AuditLogMessageDTODeserializer implements JsonDeserializer<AuditLogMessageDTO> {

    private static final String CONTENT = "content";
    private static final String DESCRIPTION = "description";
    private static final String DETAILED_DESCRIPTION = "detailedDescription";
    private static final String LEVEL = "level";

    @Override
    public AuditLogMessageDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json.isJsonObject()) {
            JsonObject asJsonObject = json.getAsJsonObject();
            String timestamp = getAsString(asJsonObject, Constants.TIMESTAMP);
            String type = getAsString(asJsonObject, Constants.TYPE);
            String created = getAsString(asJsonObject, Constants.CREATED);
            String content = getAsString(asJsonObject, CONTENT);
            String description = getAsString(asJsonObject, DESCRIPTION);
            String detailedDescription = getAsString(asJsonObject, DETAILED_DESCRIPTION);
            String level = getAsString(asJsonObject, LEVEL);

            return AuditLogMessageDTO.builder()
                    .timestamp(timestamp)
                    .type(type)
                    .level(level)
                    .description(description)
                    .detailedDescription(detailedDescription)
                    .content(content)
                    .created(created)
                    .build();
        }
        return null;
    }

    private String getAsString(JsonObject asJsonObject, String value) {
        if (asJsonObject.has(value)) {
            String s = asJsonObject.get(value).toString();
            return StringUtils.removeQuotes(s);
        } else {
            return StringUtils.EMPTY;
        }
    }
}

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

package com.company.iotfortechnicians.common.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.ResponseBody;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GsonUtils {

    public static String formatJSONPretty(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    public static <T> T fromJson(ResponseBody responseBody, Class<T> classOfT) {
        try {
            return new Gson().fromJson(responseBody.string(), classOfT);
        } catch (IOException | NullPointerException e) {
            Log.e(GsonUtils.class.getSimpleName(), e.getMessage(), e);
        }
        return null;
    }
}

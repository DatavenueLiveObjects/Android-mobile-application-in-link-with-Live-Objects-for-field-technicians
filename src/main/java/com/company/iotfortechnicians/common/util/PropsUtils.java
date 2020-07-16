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

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.company.iotfortechnicians.IOTApplication;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.dto.devicemanagement.LocationDTO;

import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.company.iotfortechnicians.common.Constants.APP_ANDROID_FLAG_LENGTH;
import static com.company.iotfortechnicians.common.Constants.INDOOR_LOCATION_KEY;
import static com.company.iotfortechnicians.common.Constants.OUTDOOR_LOCATION_KEY;
import static com.company.iotfortechnicians.common.util.IndoorProps.BUILDING;
import static com.company.iotfortechnicians.common.util.IndoorProps.FLOOR;
import static com.company.iotfortechnicians.common.util.IndoorProps.ROOM;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropsUtils {

    public static String retrieveValueFrom(String prop, IndoorProps indoorProps) {
        if (prop != null) {
            String[] indoorPropValues = prop.split(";");
            int index = indoorProps.getValue();
            return indoorPropValues.length > index ? indoorPropValues[index] : StringUtils.EMPTY;
        }
        return StringUtils.EMPTY;
    }

    public static LocationDTO getStaticLocationFromProps(Map<String, String> properties) {
        String outdoorLocation = getStaticLocationText(properties);
        if (outdoorLocation != null) {
            try {
                String[] outdoorPropValues = outdoorLocation.split(";", 4);
                int startIndex = outdoorPropValues.length == 4 ? 1 : 0;
                Double lat = Double.parseDouble(outdoorPropValues[startIndex]);
                Double lon = Double.parseDouble(outdoorPropValues[startIndex + 1]);
                Double altDouble = Double.parseDouble(outdoorPropValues[startIndex + 2]);
                return new LocationDTO(lat, lon, altDouble);
            } catch (Exception e) {
                Log.e("retrieveLocationFrom", e.getMessage(), e);
            }
        }
        return null;
    }

    public static String getStaticLocationText(Map<String, String> properties) {
        String outdoorLocation = getProperty(properties, OUTDOOR_LOCATION_KEY);
        return outdoorLocation != null ? outdoorLocation.substring(APP_ANDROID_FLAG_LENGTH) : null;
    }

    private static String getProperty(Map<String, String> properties, String key) {
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            if (entry.getKey().equals(key))
                return entry.getValue();
        }
        return null;
    }

    public static String getIndoorLocation(Map<String, String> properties) {
        String indoorLocation = PropsUtils.getProperty(properties, INDOOR_LOCATION_KEY);

        int id = getNotAvailableIfEmpty(indoorLocation, BUILDING).length() > 10
                ? R.string.building_floor_room_template
                : R.string.building_floor_room_no_enter_template;

        return getString(id,
                getNotAvailableIfEmpty(indoorLocation, BUILDING),
                getNotAvailableIfEmpty(indoorLocation, FLOOR),
                getNotAvailableIfEmpty(indoorLocation, ROOM));
    }

    @NonNull
    private static String getString(@StringRes int resId) {
        Context appContext = IOTApplication.getAppContext();
        return appContext.getString(resId);
    }

    private static String getString(@StringRes int resId, Object... formatArgs) {
        Context appContext = IOTApplication.getAppContext();
        return appContext.getString(resId, formatArgs);
    }

    public static boolean hasNoStaticLocationProp(Map.Entry<String, String> entry) {
        return !entry.getKey().equals(INDOOR_LOCATION_KEY) && !entry.getKey().equals(OUTDOOR_LOCATION_KEY);
    }


    private static String getNotAvailableIfEmpty(String indoorLocation, IndoorProps building) {
        String string = getString(R.string.not_available);
        String s = retrieveValueFrom(indoorLocation, building);
        return StringUtils.isNoneEmpty(s) ? s : string;
    }
}

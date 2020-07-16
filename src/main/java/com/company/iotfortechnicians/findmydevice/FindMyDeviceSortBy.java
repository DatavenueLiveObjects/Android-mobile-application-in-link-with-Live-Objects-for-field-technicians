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

package com.company.iotfortechnicians.findmydevice;

import androidx.annotation.StringRes;

import com.company.iotfortechnicians.IOTApplication;
import com.company.iotfortechnicians.R;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FindMyDeviceSortBy {
    DEVICE_ID(R.string.sort_by_device_id, "id"),
    DEVICE_NAME(R.string.sort_by_device_name, "name"),
    DEVICE_LAST_CONTACT(R.string.sort_by_device_last_contact, "-interfaces.lastContact");

    @StringRes
    private final int resourceId;
    @Getter
    private final String requestParameter;

    @Override
    public String toString() {
        return IOTApplication.getAppContext()
                .getResources()
                .getString(resourceId);
    }
}

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

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.company.iotfortechnicians.R;

public class FindMyDeviceSpinnerAdapter<T> extends ArrayAdapter<T> {

    public FindMyDeviceSpinnerAdapter(@NonNull Context context, @NonNull T[] objects) {
        super(context, android.R.layout.simple_spinner_item, android.R.id.text1, objects);
        setDropDownViewResource(R.layout.spinner_dropdown_item);
    }
}

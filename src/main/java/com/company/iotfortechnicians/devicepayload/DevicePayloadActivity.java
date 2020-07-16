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

package com.company.iotfortechnicians.devicepayload;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.company.iotfortechnicians.common.AbstractIOTAppBarActivity;

import static com.company.iotfortechnicians.common.Constants.DEVICE_ID_KEY;

public class DevicePayloadActivity extends AbstractIOTAppBarActivity {

    @Override
    protected Fragment getFragment() {
        Intent intent = getIntent();
        String deviceId = intent.getStringExtra(DEVICE_ID_KEY);
        return DevicePayloadFragment.newInstance(deviceId);
    }
}

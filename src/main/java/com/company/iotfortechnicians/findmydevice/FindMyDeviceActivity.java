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


import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.company.iotfortechnicians.common.AbstractIOTAppBarActivity;
import com.company.iotfortechnicians.common.Constants;

public class FindMyDeviceActivity extends AbstractIOTAppBarActivity {

    public FindMyDeviceActivity() {
        super(true);
    }

    @Override
    protected Fragment getFragment() {
        Intent intent = getIntent();
        DeviceSearchParameters parameters = (DeviceSearchParameters) intent
                .getSerializableExtra(Constants.DEVICE_SEARCH_PARAMETERS_KEY);
        return FindMyDeviceFragment.newInstance(parameters);
    }
}

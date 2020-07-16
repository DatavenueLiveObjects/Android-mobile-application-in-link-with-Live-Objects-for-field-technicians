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

package com.company.iotfortechnicians.devicestatus;

import android.content.Intent;
import android.view.Menu;

import androidx.fragment.app.Fragment;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTAppBarActivity;

import static com.company.iotfortechnicians.common.Constants.DEVICE_ID_KEY;

public class DeviceStatusActivity extends AbstractIOTAppBarActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_refresh, menu);
        return true;
    }

    @Override
    protected Fragment getFragment() {
        Intent intent = getIntent();
        String deviceId = intent.getStringExtra(DEVICE_ID_KEY);
        return DeviceStatusFragment.newInstance(deviceId);
    }
}

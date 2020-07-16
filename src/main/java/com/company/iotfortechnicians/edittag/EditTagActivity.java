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

package com.company.iotfortechnicians.edittag;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTFragmentActivity;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;

import static com.company.iotfortechnicians.common.Constants.DEVICE_DATA_KEY;

public class EditTagActivity extends AbstractIOTFragmentActivity {

    @Override
    protected Fragment getFragment() {
        Intent intent = getIntent();
        DeviceDTO deviceDTO = (DeviceDTO) intent.getSerializableExtra(DEVICE_DATA_KEY);
        return EditTagFragment.newInstance(deviceDTO);
    }

    @Override
    protected int getContentView() {
        return R.layout.abstract_fragment_activity_without_toolbar;
    }
}

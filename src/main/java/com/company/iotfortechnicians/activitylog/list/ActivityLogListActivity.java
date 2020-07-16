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

package com.company.iotfortechnicians.activitylog.list;


import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.company.iotfortechnicians.common.AbstractIOTAppBarActivity;

import static com.company.iotfortechnicians.common.Constants.NODE_ID_KEY;

public class ActivityLogListActivity extends AbstractIOTAppBarActivity {

    public ActivityLogListActivity() {
        super(true);
    }

    @Override
    protected Fragment getFragment() {
        Intent intent = getIntent();
        String nodeId = intent.getStringExtra(NODE_ID_KEY);
        return ActivityLogListFragment.newInstance(nodeId);
    }
}

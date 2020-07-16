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

package com.company.iotfortechnicians.agreeterms;

import androidx.fragment.app.Fragment;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTFragmentActivity;

public class AgreeTermsActivity extends AbstractIOTFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return AgreeTermsFragment.newInstance();
    }

    @Override
    protected int getContainerViewId() {
        return R.id.agree_terms_layout;
    }

    @Override
    protected int getContentView() {
        return R.layout.agree_terms_layout;
    }
}

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

package com.company.iotfortechnicians.login;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.company.iotfortechnicians.IOTApplication;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTFragmentActivity;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.Session;

public class LoginActivity extends AbstractIOTFragmentActivity {

    @Override
    protected int getContainerViewId() {
        return R.id.loginLayout;
    }

    @Override
    protected int getContentView() {
        return R.layout.login_layout;
    }

    @Override
    protected Fragment getFragment() {
        Intent intent = getIntent();
        String login = intent.getStringExtra(Constants.USER_LOGIN_KEY);
        return LoginFragment.newInstance(login);
    }

    public static Intent getActivityIntent(Context context) {
        Session session = IOTApplication.getSession();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(Constants.USER_LOGIN_KEY, session.getLogin());
        return intent;
    }
}

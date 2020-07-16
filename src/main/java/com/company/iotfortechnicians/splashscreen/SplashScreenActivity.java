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

package com.company.iotfortechnicians.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.company.iotfortechnicians.BuildConfig;
import com.company.iotfortechnicians.IOTApplication;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.agreeterms.AgreeTermsActivity;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.Session;
import com.company.iotfortechnicians.common.AbstractIOTActivity;
import com.company.iotfortechnicians.common.util.SharedPreferencesUtils;
import com.company.iotfortechnicians.login.LoginActivity;
import com.company.iotfortechnicians.menu.MenuActivity;

import static com.company.iotfortechnicians.common.Constants.ACCEPTED_TERMS_DEFAULT_VALUE;
import static com.company.iotfortechnicians.common.Constants.SPLASH_TIME;

public class SplashScreenActivity extends AbstractIOTActivity {

    private static final String NBLAUNCH = "nblaunch_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {

            Handler handler = new Handler();
            handler.postDelayed(this::goToNextScreen, SPLASH_TIME);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.splash_screen_layout;
    }

    private void goToNextScreen() {
        boolean acceptedTermsInCurrentVersion = isAcceptedTermsInCurrentVersion();

        Intent mainIntent;
        Session session = IOTApplication.getSession();
        if (acceptedTermsInCurrentVersion && session.isAuthenticated()) {
            mainIntent = new Intent(this, MenuActivity.class);
            mainIntent.putExtra(Constants.RELOAD_USERDATA_KEY, true);
        } else if (acceptedTermsInCurrentVersion) {
            mainIntent = LoginActivity.getActivityIntent(this);
        } else {
            mainIntent = new Intent(this, AgreeTermsActivity.class);
        }

        startActivity(mainIntent);
        finish();
    }

    private boolean isAcceptedTermsInCurrentVersion() {
        return SharedPreferencesUtils.getInt(this, Constants.ACCEPTED_TERMS_KEY, ACCEPTED_TERMS_DEFAULT_VALUE) == BuildConfig.VERSION_CODE;
    }

}

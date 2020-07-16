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

package com.company.iotfortechnicians.menu;

import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTAppBarActivity;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.LogoutCallback;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.service.AuthService;

import java.util.HashMap;

public class MenuActivity extends AbstractIOTAppBarActivity {

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            callLogoutApi();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.please_click_back_again, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    protected Fragment getFragment() {
        Intent intent = getIntent();
        boolean reload = intent.getBooleanExtra(Constants.RELOAD_USERDATA_KEY, false);
        return MenuFragment.newInstance(reload);
    }

    private void callLogoutApi() {
        AuthService authService = ApiClient.createApiService(AuthService.class);
        authService.logout(new HashMap<>()).enqueue(new LogoutCallback(this, true));
    }

    @Override
    protected String type() {
        return MENU;
    }

}

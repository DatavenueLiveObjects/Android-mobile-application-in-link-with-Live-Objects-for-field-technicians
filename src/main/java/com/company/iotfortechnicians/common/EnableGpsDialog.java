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

package com.company.iotfortechnicians.common;

import android.content.Intent;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.company.iotfortechnicians.R;

public class EnableGpsDialog {

    public static void newInstance(Fragment clazz) {
        new AlertDialog.Builder(clazz.getContext())
                .setMessage(R.string.gps_network_not_enabled)
                .setPositiveButton(R.string.open_location_settings, (paramDialogInterface, paramInt) -> clazz.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}

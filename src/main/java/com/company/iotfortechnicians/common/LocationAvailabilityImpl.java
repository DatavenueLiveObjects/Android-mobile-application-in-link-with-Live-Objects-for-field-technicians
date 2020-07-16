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

import android.content.Context;
import android.location.LocationManager;

public class LocationAvailabilityImpl implements LocationAvailability {

    private final Context context;

    public LocationAvailabilityImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean isLocationDisabled() {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return !manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
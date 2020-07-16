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

package com.company.iotfortechnicians.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.company.iotfortechnicians.common.api.NoConnectivityException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NetworkUtils {

    private static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    public static boolean isOffline(Context context) {
        return !isOnline(context);
    }

    public static boolean isNoConnectionAvailable(Throwable t) {
        return t instanceof NoConnectivityException;
    }

    public static boolean isTimeout(Throwable t) {
        return t instanceof SocketTimeoutException;
    }

    public static boolean isConnectProblem(Throwable t) {
        return t instanceof ConnectException;
    }
}

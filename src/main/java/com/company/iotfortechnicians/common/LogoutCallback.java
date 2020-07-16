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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.company.iotfortechnicians.IOTApplication;
import com.company.iotfortechnicians.common.util.DialogUtils;
import com.company.iotfortechnicians.login.LoginActivity;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoutCallback implements Callback<String> {

    private final boolean closeApp;
    private final WeakReference<Context> contextReference;

    LogoutCallback(Context context) {
        this(context, false);
    }

    public LogoutCallback(Context context, boolean closeApp) {
        this.closeApp = closeApp;
        this.contextReference = new WeakReference<>(context);
    }

    @Override
    public void onResponse(@NonNull Call<String> call, Response<String> response) {
        if (response.isSuccessful()) {
            afterLogout();
        } else {
            showProblemOccursDialog();
        }
    }

    @Override
    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
        Log.e(this.getClass().getSimpleName(), t.getMessage(), t);
        showProblemOccursDialog();
    }

    private void showProblemOccursDialog() {
        DialogUtils.showProblemOccursDialog(getContext(), (dialogInterface, i) -> afterLogout());
    }

    private Context getContext() {
        return contextReference.get();
    }

    private void afterLogout() {
        IOTApplication.clearSession();
        if (shouldCloseApp()) {
            closeApp();
        } else {
            startLoginActivity();
        }
    }

    private boolean shouldCloseApp() {
        return closeApp && getContext() instanceof Activity;
    }

    private void startLoginActivity() {
        Context context = getContext();
        Intent intent = LoginActivity.getActivityIntent(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void closeApp() {
        Activity activity = (Activity) getContext();
        activity.finishAffinity();
    }
}

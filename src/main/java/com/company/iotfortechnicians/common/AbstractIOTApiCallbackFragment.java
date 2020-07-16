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

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.company.iotfortechnicians.IOTApplication;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.dto.error.ErrorDTO;
import com.company.iotfortechnicians.common.util.DialogUtils;
import com.company.iotfortechnicians.common.util.GsonUtils;
import com.company.iotfortechnicians.login.LoginActivity;

import java.net.HttpURLConnection;
import java.util.Collection;

import lombok.AccessLevel;
import lombok.Getter;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.company.iotfortechnicians.common.util.DialogUtils.showMessageFromApiDialog;
import static com.company.iotfortechnicians.common.util.DialogUtils.showNoConnectionAvailableDialog;
import static com.company.iotfortechnicians.common.util.DialogUtils.showProblemOccursDialog;
import static com.company.iotfortechnicians.common.util.NetworkUtils.isConnectProblem;
import static com.company.iotfortechnicians.common.util.NetworkUtils.isNoConnectionAvailable;
import static com.company.iotfortechnicians.common.util.NetworkUtils.isTimeout;

public abstract class AbstractIOTApiCallbackFragment<T> extends AbstractIOTFragment implements Callback<T>, OnFailure {

    private ProgressDialog mProgressDialog;
    @Getter(AccessLevel.PROTECTED)
    private int xTotalCount;

    protected abstract void processData(T data);

    @Override
    public void onResponse(@NonNull Call<T> call, Response<T> response) {
        xTotalCount = getXTotalCount(response);
        if (response.isSuccessful()) {
            T body = response.body();
            processData(body);
        } else {
            int code = response.code();
            ResponseBody responseBody = response.errorBody();
            processResponseErrorBody(code, responseBody);
        }
        hideLoadingDialog();
    }

    @Override
    public void onResponseError(int code, ResponseBody responseBody) {
        hideLoadingDialog();
        processResponseErrorBody(code, responseBody);
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        onFailure(t);
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(getClass().getName(), t.getMessage(), t);
        hideLoadingDialog();
        if (isNoConnectionAvailable(t) || isConnectProblem(t) || isTimeout(t)) {
            showNoConnectionAvailableDialog(getActivity());
        } else {
            showProblemOccursDialog(getActivity());
        }
    }

    protected void processResponseErrorBody(int code, ResponseBody responseBody) {
        if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            processUnauthorizedResponse(responseBody);
        } else {
            ErrorDTO errorDTO = GsonUtils.fromJson(responseBody, ErrorDTO.class);
            showMessageFromApiDialog(getActivity(), errorDTO, null);
        }
    }

    protected void processUnauthorizedResponse(ResponseBody responseBody) {
        Session session = IOTApplication.getSession();
        boolean isSessionExpired = session.isExpired();
        IOTApplication.clearSession();
        if (isSessionExpired) {
            showOkDialog(R.string.session_expired, getBackToLoginOnClickListener());
        } else {
            ErrorDTO errorDTO = GsonUtils.fromJson(responseBody, ErrorDTO.class);
            showMessageFromApiDialog(getActivity(), errorDTO, getBackToLoginOnClickListener());
        }
    }

    protected ErrorDTO getErrorDTO(ResponseBody responseBody) {
        return GsonUtils.fromJson(responseBody, ErrorDTO.class);
    }

    @NonNull
    private DialogInterface.OnClickListener getBackToLoginOnClickListener() {
        return (dialogInterface, i) -> {
            Intent intent = LoginActivity.getActivityIntent(getActivity());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        };
    }

    protected void showSessionRestoringDialog() {
        String message = getString(R.string.session_restoring);
        showLoadingDialog(message);
    }

    public void showLoadingDialog() {
        String message = getString(R.string.loading);
        showLoadingDialog(message);
    }

    private void showLoadingDialog(String message) {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        } else {
            mProgressDialog = ProgressDialog.show(getActivity(), null, message);
        }
    }

    public void hideLoadingDialog() {
        if (isLoadingDialogVisible()) {
            mProgressDialog.dismiss();
        }
    }

    private boolean isLoadingDialogVisible() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideLoadingDialog();
    }

    private int getXTotalCount(Response<T> response) {
        try {
            Headers headers = response.headers();
            String s = headers.get(Constants.X_TOTAL_COUNT);
            return Integer.parseInt(s);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }

        return 0;
    }

    protected boolean isEmptyResponse(T body) {
        return body == null || (body instanceof Collection && ((Collection) body).isEmpty());
    }

    protected void showOkDialog(@StringRes int id) {
        DialogUtils.showOkDialog(getActivity(), id);
    }

    protected void showOkDialog(@StringRes int id, DialogInterface.OnClickListener onClickListener) {
        DialogUtils.showOkDialog(getActivity(), id, onClickListener);
    }

}

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
import android.content.DialogInterface;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.dto.error.ErrorDTO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DialogUtils {

    public static void showProblemOccursDialog(Context context) {
        showProblemOccursDialog(context, null);
    }

    public static void showProblemOccursDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        showOkDialog(context, R.string.problem_occurs, onClickListener);
    }

    public static void showNoConnectionAvailableDialog(Context context) {
        showOkDialog(context, R.string.no_connection_available);
    }

    public static void showOkDialog(Context context, @StringRes int messageId) {
        showOkDialog(context, messageId, null);
    }

    public static void showOkDialog(Context context, @StringRes int messageId, DialogInterface.OnClickListener onClickListener) {
        showOkDialog(context, context.getString(messageId), onClickListener);
    }

    private static void showOkDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.ok, onClickListener)
                .show();
    }

    public static void showMessageFromApiDialog(Context context, ErrorDTO errorDTO, DialogInterface.OnClickListener onClickListener) {
        String messageToShow = StringUtils.EMPTY;
        if (errorDTO != null) {
            messageToShow = getMessage(errorDTO);
        }

        String message = StringUtils.isNoneEmpty(messageToShow) ? messageToShow : context.getString(R.string.problem_occurs);
        showOkDialog(context, message, onClickListener);
    }

    private static String getMessage(ErrorDTO errorDTO) {
        String message = errorDTO.getMessage();
        String details = errorDTO.getDetails();
        if (StringUtils.isNoneEmpty(details)) {
            message += " " + details;
        }
        return message;
    }
}

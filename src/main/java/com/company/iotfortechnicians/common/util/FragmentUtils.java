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

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FragmentUtils {

    public static void setValueToTextView(View inflatedLayout, int p, String name) {
        TextView nameTextView = inflatedLayout.findViewById(p);
        nameTextView.setText(name);
    }

    public static void setValueToTextView(View inflatedLayout, @IdRes int p, Number value) {
        setValueToTextView(inflatedLayout, p, String.valueOf(value));
    }

    public static void setValueToTextView(View inflatedLayout, @IdRes int p, Boolean value) {
        setValueToTextView(inflatedLayout, p, String.valueOf(value));
    }

    public static void setViewVisibility(View inflatedLayout, @IdRes int p, boolean visible) {
        View view = inflatedLayout.findViewById(p);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public static void setValueToFormattedTextView(View inflatedLayout, int p, String... values) {
        TextView nameTextView = inflatedLayout.findViewById(p);
        String s = String.valueOf(nameTextView.getText());
        nameTextView.setText(String.format(s, (Object[]) values));
    }

    public static void setValueToFormattedTextView(View inflatedLayout, @IdRes int p, Number value) {
        setValueToFormattedTextView(inflatedLayout, p, String.valueOf(value));
    }

    public static void setViewEnabled(View inflatedLayout, @IdRes int p, boolean enabled) {
        View view = inflatedLayout.findViewById(p);
        view.setEnabled(enabled);
    }

    public static void setViewBackgroundResource(View inflatedLayout, @IdRes int p, int b) {
        View view = inflatedLayout.findViewById(p);
        view.setBackgroundResource(b);
    }

    public static String getValueFromTextView(View inflatedLayout, @IdRes int p) {
        TextView view = inflatedLayout.findViewById(p);
        return view.getText().toString();
    }

    public static void setImageToImageView(View inflatedLayout, @IdRes int p, @DrawableRes int b) {
        ImageView imageView = inflatedLayout.findViewById(p);
        imageView.setImageResource(b);
    }

    public static void closeKeyboard(Activity activity) {
        if (activity != null) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            IBinder windowToken = decorView.getWindowToken();
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}

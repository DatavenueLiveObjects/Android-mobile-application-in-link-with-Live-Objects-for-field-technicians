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

package com.company.iotfortechnicians.findmydevice.sort;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatRadioButton;

public class SortRadioButton extends AppCompatRadioButton {

    public SortRadioButton(Context context) {
        super(context);
        init();
    }

    public SortRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setId(View.generateViewId());
        setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setLayoutParams(getParams());
    }

    private ViewGroup.LayoutParams getParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}

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

package com.company.iotfortechnicians.findmydevice.filter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.util.FragmentUtils;
import com.company.iotfortechnicians.findmydevice.FindMyDeviceSearchType;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractFilterView extends LinearLayout {

    private View filterTopBar;
    private TextView filterTitle;
    private CheckBox filterCheckBox;
    @Getter
    private FindMyDeviceSearchType searchType;
    private LinearLayout filterValueContainer;
    @Setter
    private OnFilterValueChanged onFilterValueChanged;

    public AbstractFilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViewElements(context);
        initViewValuesAndBehaviors(context, attrs);
    }

    private void initViewElements(Context context) {
        inflate(context, getLayoutResource(), this);
        filterTopBar = findViewById(R.id.filter_top_bar);
        filterTitle = findViewById(R.id.filter_title);
        filterCheckBox = findViewById(R.id.filter_checkbox);
        filterValueContainer = findViewById(R.id.filter_value_container);
    }

    private void initViewValuesAndBehaviors(@NonNull Context context, @Nullable AttributeSet attrs) {
        filterTopBar.setOnClickListener(v -> changeFilterVisibility());
        filterCheckBox.setOnClickListener(v -> changeFilterVisibility());
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AbstractFilterView);
        int resourceId = attributes.getResourceId(R.styleable.AbstractFilterView_title, 0);
        searchType = FindMyDeviceSearchType.valueOfResourceId(resourceId);
        filterTitle.setText(context.getString(resourceId));
        attributes.recycle();
    }

    protected void changeFilterVisibility() {
        boolean newCheckedValue = !isChecked();
        filterCheckBox.setChecked(newCheckedValue);
        filterValueContainer.setVisibility(newCheckedValue ? View.VISIBLE : View.GONE);
        onFilterValueChanged(getFilterValue());
        closeKeyboard();
    }

    protected void onFilterValueChanged(String newFilterValue) {
        onFilterValueChanged.onFilterValueChanged(searchType, newFilterValue);
    }

    protected boolean isChecked() {
        return filterCheckBox.isChecked();
    }

    protected String getTitle() {
        return filterTitle.getText().toString();
    }

    protected void closeKeyboard() {
        Activity activity = getActivity();
        FragmentUtils.closeKeyboard(activity);
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    public abstract void setFilterValue(String value);

    public abstract String getFilterValue();
}

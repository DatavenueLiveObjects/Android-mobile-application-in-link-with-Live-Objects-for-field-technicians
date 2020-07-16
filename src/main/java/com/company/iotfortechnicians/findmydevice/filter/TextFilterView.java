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

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.util.StringUtils;

public class TextFilterView extends AbstractFilterView implements TextWatcher {

    private TextInputEditText filterValueInput;
    private Handler handler = new Handler();

    public TextFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        filterValueInput = findViewById(R.id.filter_value_input);
        filterValueInput.addTextChangedListener(this);
        TextInputLayout inputLayout = findViewById(R.id.input_layout);
        inputLayout.setEndIconOnClickListener(v -> filterValueInput.setText(StringUtils.EMPTY));
        inputLayout.setHint(getTitle());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.text_filter_view_layout;
    }

    public String getFilterValue() {
        return isChecked() ? filterValueInput.getText().toString() : null;
    }

    public void setFilterValue(String filterValue) {
        filterValueInput.setText(filterValue);
        changeFilterVisibility();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Not needed
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //Not needed
    }

    @Override
    public void afterTextChanged(Editable s) {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(() -> onFilterValueChanged(s.toString()), 500);
    }
}

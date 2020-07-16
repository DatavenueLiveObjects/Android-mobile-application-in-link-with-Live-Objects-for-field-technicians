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
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.components.select.SelectComponent;
import com.company.iotfortechnicians.common.components.select.SelectionItem;
import com.company.iotfortechnicians.common.components.spinner.SpinnerComponent;

import java.util.List;

public class SelectionFilterView extends AbstractFilterView {

    private SelectComponent<String> filterSpinner;
    private String previous;

    public SelectionFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        filterSpinner = new SpinnerComponent(this, R.id.filter_value_spinner);
        filterSpinner.setOnSelectedCallback((position, selectedItem) -> {
            if (previous != null) {
                onFilterValueChanged(selectedItem);
            }
            previous = selectedItem;
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.selection_filter_view_layout;
    }

    @Override
    public String getFilterValue() {
        return isChecked() ? filterSpinner.getSelectedValue() : null;
    }

    @Override
    public void setFilterValue(String filterValue) {
        filterSpinner.setValue(filterValue);
        changeFilterVisibility();
    }

    public void setValues(List<SelectionItem<String>> values) {
        filterSpinner.setValues(values);
    }


}

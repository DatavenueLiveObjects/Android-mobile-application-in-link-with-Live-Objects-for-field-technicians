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

package com.company.iotfortechnicians.common.components.spinner;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.components.select.SelectionItem;

import java.util.List;

class SpinnerAdapter<T> extends ArrayAdapter<SelectionItem<T>> {

    public SpinnerAdapter(@NonNull Context context, @NonNull List<SelectionItem<T>> objects) {
        super(context, android.R.layout.simple_spinner_item, android.R.id.text1, objects);
        setDropDownViewResource(R.layout.spinner_dropdown_item);
    }

    int getItemPosition(T item) {
        int count = getCount();
        for (int i = 0; i < count; i++) {
            SelectionItem<T> element = super.getItem(i);
            if (element != null && element.getValue().equals(item)) {
                return i;
            }
        }
        return -1;
    }
}

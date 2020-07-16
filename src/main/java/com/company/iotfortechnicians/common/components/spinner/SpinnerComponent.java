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

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.company.iotfortechnicians.common.components.select.SelectComponent;
import com.company.iotfortechnicians.common.components.select.Selectable;
import com.company.iotfortechnicians.common.components.select.SelectionItem;

import java.util.List;

public class SpinnerComponent<T> implements AdapterView.OnItemSelectedListener, SelectComponent<T> {

    private View parentView;
    private Spinner spinnerElementView;
    private Selectable<T> onSelectedCallback;

    public SpinnerComponent(@NonNull View parentView, int spinnerId) {
        this.parentView = parentView;
        spinnerElementView = parentView.findViewById(spinnerId);
        spinnerElementView.setOnItemSelectedListener(this);
    }

    public void setOnSelectedCallback(Selectable<T> onSelectedCallback) {
        this.onSelectedCallback = onSelectedCallback;
    }

    @Override
    public T getSelectedValue() {
        SelectionItem<T> item = (SelectionItem<T>) spinnerElementView.getSelectedItem();

        if (item != null) {
            return item.getValue();
        }

        return null;
    }

    @Override
    public void setValue(T filterValue) {
        SpinnerAdapter adapter = (SpinnerAdapter) spinnerElementView.getAdapter();
        int position = adapter.getItemPosition(filterValue);
        spinnerElementView.setSelection(position);
    }

    public void setValues(List<SelectionItem<T>> values) {
        SpinnerAdapter adapter = new SpinnerAdapter(parentView.getContext(), values);
        spinnerElementView.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (onSelectedCallback != null) {
            onSelectedCallback.onItemSelected(position, getSelectedValue());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Not needed
    }
}

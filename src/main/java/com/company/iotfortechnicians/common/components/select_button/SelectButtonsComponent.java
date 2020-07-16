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

package com.company.iotfortechnicians.common.components.select_button;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.iotfortechnicians.common.components.select.SelectComponent;
import com.company.iotfortechnicians.common.components.select.Selectable;
import com.company.iotfortechnicians.common.components.select.SelectionItem;

import java.util.List;

public class SelectButtonsComponent<T> implements SelectComponent<T> {

    private RecyclerView optionsComponent;
    private Selectable<T> onSelectedCallback;
    private SelectButtonsAdapter<T> selectButtonsAdapter;

    public SelectButtonsComponent(View parentView, int spinnerId, int spanCount) {
        optionsComponent = parentView.findViewById(spinnerId);

        final GridLayoutManager layout = new GridLayoutManager(parentView.getContext(), spanCount);
        optionsComponent.setLayoutManager(layout);
    }

    public void setOnSelectedCallback(Selectable<T> onSelectedCallback) {
        this.onSelectedCallback = onSelectedCallback;

        if (selectButtonsAdapter != null)
            selectButtonsAdapter.setOnSelectedCallback(onSelectedCallback);
    }

    @Override
    public T getSelectedValue() {
        if (selectButtonsAdapter != null)
            return selectButtonsAdapter.getSelectedValue();

        return null;
    }

    @Override
    public void setValue(T filterValue) {
        if (selectButtonsAdapter != null)
            selectButtonsAdapter.setValue(filterValue);
    }

    @Override
    public void setValues(List<SelectionItem<T>> values) {
        selectButtonsAdapter = new SelectButtonsAdapter<>(values);
        selectButtonsAdapter.setOnSelectedCallback(this.onSelectedCallback);
        optionsComponent.setAdapter(selectButtonsAdapter);
    }
}

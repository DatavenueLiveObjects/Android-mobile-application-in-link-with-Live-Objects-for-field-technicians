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

package com.company.iotfortechnicians.editproperties;

import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.company.iotfortechnicians.R;

public class EditPropertiesViewHolder extends RecyclerView.ViewHolder {

    private TextInputEditText key;
    private TextInputEditText value;
    private ImageView delete;

    public EditPropertiesViewHolder(View view) {
        super(view);
    }

    void bind(Property prop) {
        key = itemView.findViewById(R.id.editTextKey);
        key.setText(prop.getName());
        value = itemView.findViewById(R.id.editTextValue);
        value.setText(prop.getValue());
        delete = itemView.findViewById(R.id.property_delete);
    }

    void addKeyTextChangedListener(TextWatcher textWatcher) {
        key.addTextChangedListener(textWatcher);
    }

    void addValueTextChangedListener(TextWatcher textWatcher) {
        value.addTextChangedListener(textWatcher);
    }

    void setDeleteOnClickListener(View.OnClickListener onClickListener) {
        delete.setOnClickListener(onClickListener);
    }

    String getKeyText() {
        return key.getText().toString();
    }

    String getValueText() {
        return value.getText().toString();
    }
}

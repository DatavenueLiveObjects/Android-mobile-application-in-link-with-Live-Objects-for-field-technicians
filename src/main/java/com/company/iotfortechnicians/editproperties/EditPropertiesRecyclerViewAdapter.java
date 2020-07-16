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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.company.iotfortechnicians.common.Constants.INDOOR_LOCATION_KEY;
import static com.company.iotfortechnicians.common.Constants.OUTDOOR_LOCATION_KEY;

public class EditPropertiesRecyclerViewAdapter extends RecyclerView.Adapter<EditPropertiesViewHolder> {

    static final List<String> PROPERTIES_TO_SKIP = new ArrayList<>(
            Arrays.asList(INDOOR_LOCATION_KEY, OUTDOOR_LOCATION_KEY)
    );
    private final List<Property> mValues = new ArrayList<>();

    public EditPropertiesRecyclerViewAdapter(Map<String, String> values) {
        for (Map.Entry<String, String> e : values.entrySet()) {
            if (!isPropertyToSkip(e)) {
                this.mValues.add(new Property(e.getKey(), e.getValue()));
            }
        }
    }

    private boolean isPropertyToSkip(Map.Entry<String, String> e) {
        return PROPERTIES_TO_SKIP.contains(e.getKey());
    }

    @Override
    public void onViewAttachedToWindow(@NonNull EditPropertiesViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        TextInputEditText key = holder.itemView.findViewById(R.id.editTextKey);
        key.requestFocus();
    }

    @NonNull
    @Override
    public EditPropertiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.property_row_layout, parent, false);
        return new EditPropertiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EditPropertiesViewHolder holder, int position) {
        Property property = mValues.get(position);
        holder.bind(property);
        holder.addKeyTextChangedListener(getWatcher(holder, position));
        holder.addValueTextChangedListener(getWatcher(holder, position));
        holder.setDeleteOnClickListener(getDeleteOnClickListener(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @NonNull
    private View.OnClickListener getDeleteOnClickListener(int position) {
        return v -> {
            mValues.remove(position);
            notifyDataSetChanged();
        };
    }

    @NonNull
    private TextWatcher getWatcher(final EditPropertiesViewHolder holder, int position) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Not needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isNotRemovedElement()) {
                    Property property = new Property(holder.getKeyText(), holder.getValueText());
                    mValues.set(position, property);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Not needed
            }

            private boolean isNotRemovedElement() {
                return mValues.size() - 1 <= position;
            }

        };
    }

    void addNewItem() {
        mValues.add(new Property(StringUtils.EMPTY, StringUtils.EMPTY));
        notifyDataSetChanged();
    }

    HashMap<String, String> getProperties() {
        HashMap<String, String> properties = new LinkedHashMap<>();
        for (Property property : mValues) {
            if (StringUtils.isNoneEmpty(property.getName()) || StringUtils.isNoneEmpty(property.getValue())) {
                properties.put(property.getName(), property.getValue());
            }
        }
        return properties;
    }

}

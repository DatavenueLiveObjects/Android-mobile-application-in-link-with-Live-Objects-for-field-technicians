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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.components.select.Selectable;
import com.company.iotfortechnicians.common.components.select.SelectionItem;

import java.util.List;

public class SelectButtonsAdapter<T> extends RecyclerView.Adapter<SelectButtonsAdapter.ViewHolder> {

    private List<SelectionItem<T>> optionList;
    private Selectable<T> onSelectedCallback;
    private int activePosition = 0;

    public SelectButtonsAdapter(List<SelectionItem<T>> optionList) {
        this.optionList = optionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View optionListItem = inflater.inflate(R.layout.select_buttons_item, parent, false);

        return new ViewHolder(optionListItem);
    }

    public void setOnSelectedCallback(Selectable<T> onSelectedCallback) {
        this.onSelectedCallback = onSelectedCallback;
    }

    private void setActivePosition(int position) {
        if (position != activePosition) {
            int prevPosition = activePosition;
            activePosition = position;

            notifyItemChanged(prevPosition);
            notifyItemChanged(activePosition);

            if (onSelectedCallback != null) {
                onSelectedCallback.onItemSelected(position, optionList.get(position).getValue());
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SelectButtonsAdapter.ViewHolder holder, int position) {
        SelectionItem<T> coverage = optionList.get(position);

        holder.setText(coverage.toString());
        holder.setActive(activePosition == position);

        holder.setOnClickListener(listener -> setActivePosition(position));
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public T getSelectedValue() {
        if (!optionList.isEmpty())
            return optionList.get(activePosition).getValue();

        return null;
    }

    public void setValue(T filterValue) {
        int index = -1;

        for (SelectionItem<T> item : optionList) {
            index++;
            if (item.getValue().equals(filterValue)) {
                setActivePosition(index);
                break;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private android.widget.TextView optionText;
        private android.widget.LinearLayout option;
        private final Resources resources;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.optionText = itemView.findViewById(R.id.optionText);
            this.option = itemView.findViewById(R.id.option);
            this.resources = itemView.getResources();
        }

        public void setOnClickListener(View.OnClickListener listener) {
            this.option.setOnClickListener(listener);
        }

        public void setText(String text) {
            optionText.setText(text);
        }

        public void setActive(boolean active) {
            Drawable background = resources.getDrawable(
                    active
                            ? R.drawable.rounded_rectangle_active
                            : R.drawable.rounded_rectangle_inactive,
                    null
            );

            int pL = option.getPaddingLeft();
            int pT = option.getPaddingTop();
            int pR = option.getPaddingRight();
            int pB = option.getPaddingBottom();
            option.setBackground(background);
            option.setPadding(pL, pT, pR, pB);
        }

    }

}


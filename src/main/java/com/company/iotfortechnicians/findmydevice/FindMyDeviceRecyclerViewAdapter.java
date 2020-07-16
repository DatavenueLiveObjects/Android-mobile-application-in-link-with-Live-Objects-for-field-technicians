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

package com.company.iotfortechnicians.findmydevice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class FindMyDeviceRecyclerViewAdapter extends RecyclerView.Adapter<FindMyDeviceViewHolder> {

    private final List<DeviceDTO> searchResults;
    private final FindMyDeviceFragmentInterface deviceFragmentInterface;
    private int totalCount;
    @Getter
    @Setter
    private boolean loading;

    FindMyDeviceRecyclerViewAdapter(FindMyDeviceFragmentInterface deviceFragmentInterface) {
        this(new ArrayList<>(), deviceFragmentInterface);
        this.loading = false;
        this.totalCount = 0;
    }

    @NonNull
    @Override
    public FindMyDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_layout, parent, false);
        return new FindMyDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FindMyDeviceViewHolder holder, int position) {
        DeviceDTO deviceDTO = searchResults.get(position);
        holder.bind(deviceDTO, v -> deviceFragmentInterface.onDeviceSelected(deviceDTO));
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    void clear() {
        searchResults.clear();
        totalCount = 0;
        notifyDataSetChanged();
    }

    boolean hasAllItemsLoaded() {
        return searchResults.size() == totalCount;
    }

    void addAll(List<DeviceDTO> newData, int count) {
        totalCount = count;
        searchResults.addAll(newData);
        notifyDataSetChanged();
    }
}

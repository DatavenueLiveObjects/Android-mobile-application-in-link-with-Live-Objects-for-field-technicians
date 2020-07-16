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

package com.company.iotfortechnicians.activitylog.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.dto.auditlog.AuditLogMessageDTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class ActivityLogListRecyclerViewAdapter extends RecyclerView.Adapter<ActivityLogListViewHolder> {

    private final List<AuditLogMessageDTO> activityLogs;
    private final ActivityLogListFragment activityLogFragment;
    private int totalCount;
    @Getter
    @Setter
    private boolean loading;

    ActivityLogListRecyclerViewAdapter(ActivityLogListFragment activityLogFragment) {
        this(new ArrayList<>(), activityLogFragment);
        this.loading = false;
        this.totalCount = 0;
    }

    @NonNull
    @Override
    public ActivityLogListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_log_item_layout, parent, false);
        return new ActivityLogListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ActivityLogListViewHolder holder, int position) {
        AuditLogMessageDTO activityLog = activityLogs.get(position);
        holder.bind(activityLog, v -> activityLogFragment.onAuditLogSelected(activityLog));
    }

    @Override
    public int getItemCount() {
        return activityLogs.size();
    }

    void clear() {
        activityLogs.clear();
        totalCount = 0;
        notifyDataSetChanged();
    }

    boolean hasAllItemsLoaded() {
        return activityLogs.size() == totalCount;
    }

    void addAll(List<AuditLogMessageDTO> newData, int count) {
        totalCount = count;
        activityLogs.addAll(newData);
        notifyDataSetChanged();
    }
}

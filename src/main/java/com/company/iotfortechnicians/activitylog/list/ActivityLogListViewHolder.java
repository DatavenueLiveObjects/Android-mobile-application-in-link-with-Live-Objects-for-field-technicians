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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.dto.auditlog.AuditLogMessageDTO;
import com.company.iotfortechnicians.common.util.DateUtils;

class ActivityLogListViewHolder extends RecyclerView.ViewHolder {

    private final View mView;
    private final TextView logDate;
    private final TextView logDescription;
    private final TextView logDetailedDescription;

    ActivityLogListViewHolder(View view) {
        super(view);
        mView = view;
        logDate = view.findViewById(R.id.log_date);
        logDescription = view.findViewById(R.id.log_description);
        logDetailedDescription = view.findViewById(R.id.log_detailed_description);
    }

    void bind(AuditLogMessageDTO auditLogMessageDTO, View.OnClickListener onClickListener) {
        String date = DateUtils.parseAndFormatDate(auditLogMessageDTO.getTimestamp());
        logDate.setText(date);
        logDescription.setText(auditLogMessageDTO.getDescription());
        logDetailedDescription.setText(auditLogMessageDTO.getDetailedDescription());
        if (Constants.ERROR.equalsIgnoreCase(auditLogMessageDTO.getLevel())) {
            Drawable drawable = getDrawable();
            logDate.setCompoundDrawables(drawable, null, null, null);
        }

        mView.setOnClickListener(onClickListener);
    }

    @Nullable
    private Drawable getDrawable() {
        Context context = mView.getContext();
        Drawable img = context.getDrawable(R.drawable.warning);
        if (img != null) {
            img.setBounds(0, 0, 30, 30);
        }
        return img;
    }
}
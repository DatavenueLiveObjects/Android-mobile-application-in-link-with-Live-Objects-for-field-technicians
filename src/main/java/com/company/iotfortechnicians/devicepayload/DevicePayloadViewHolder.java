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

package com.company.iotfortechnicians.devicepayload;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.dto.search.SourceDTO;
import com.company.iotfortechnicians.common.dto.search.payload.HitDTO;
import com.company.iotfortechnicians.common.util.DateUtils;
import com.company.iotfortechnicians.common.util.FragmentUtils;
import com.company.iotfortechnicians.common.util.GsonUtils;

class DevicePayloadViewHolder extends RecyclerView.ViewHolder {

    private boolean expanded = false;
    private final View rowHeader;
    private final View copyButton;

    DevicePayloadViewHolder(View view) {
        super(view);
        rowHeader = itemView.findViewById(R.id.more_info);
        copyButton = itemView.findViewById(R.id.copy);
    }

    void bind(HitDTO hitDTO) {
        FragmentUtils.setViewVisibility(itemView, R.id.sub_item, expanded);

        SourceDTO source = hitDTO.getSource();
        if (source != null) {
            displayHeaderDate(source.getTimestamp());
            displayDetails(hitDTO.getPayload());
        }
    }

    void setRowTitleOnClickListener(View.OnClickListener listener) {
        rowHeader.setOnClickListener(listener);
    }

    void setCopyButtonOnClickListener(View.OnClickListener listener) {
        copyButton.setOnClickListener(listener);
    }

    void reverseRowExpansion() {
        expanded = !expanded;
    }

    private void displayDetails(String payload) {
        String jsonPretty = GsonUtils.formatJSONPretty(payload);
        FragmentUtils.setValueToTextView(itemView, R.id.payloadMetadata, jsonPretty);
    }

    private void displayHeaderDate(String timestamp) {
        String text = DateUtils.parseAndFormatDate(timestamp);
        FragmentUtils.setValueToFormattedTextView(itemView, R.id.item_date, text);
    }
}

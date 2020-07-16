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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.dto.search.payload.HitDTO;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

public class DevicePayloadRecyclerViewAdapter extends RecyclerView.Adapter<DevicePayloadViewHolder> {

    private static final String PAYLOAD_MESSAGE = "Payload message";
    private Context context;
    private List<HitDTO> mValues;

    DevicePayloadRecyclerViewAdapter(List<HitDTO> mValues) {
        this.mValues = mValues;
    }

    @NonNull
    @Override
    public DevicePayloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_item_device_payload, parent, false);
        return new DevicePayloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DevicePayloadViewHolder holder, int position) {
        HitDTO hitDTO = mValues.get(position);
        holder.bind(hitDTO);

        holder.setRowTitleOnClickListener(v -> {
            holder.reverseRowExpansion();
            notifyItemChanged(position);
        });

        holder.setCopyButtonOnClickListener(v -> {
            copyToClipboard(hitDTO);
            makeToast();
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    void replaceList(List<HitDTO> newList) {
        mValues = newList;
        notifyDataSetChanged();
    }

    private void copyToClipboard(HitDTO hitDTO) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(PAYLOAD_MESSAGE, hitDTO.getPayload());
        clipboard.setPrimaryClip(clip);
    }

    private void makeToast() {
        Toast.makeText(context, R.string.message_copied, Toast.LENGTH_LONG)
                .show();
    }
}

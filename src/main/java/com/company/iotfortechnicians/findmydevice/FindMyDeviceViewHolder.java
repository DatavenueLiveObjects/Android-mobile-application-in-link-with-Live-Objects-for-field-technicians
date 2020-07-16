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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.InterfaceDTO;
import com.company.iotfortechnicians.common.util.DateUtils;
import com.company.iotfortechnicians.common.util.DeviceUtils;

import java.util.Date;

import static com.company.iotfortechnicians.common.util.DeviceUtils.isLoraConnectivity;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setImageToImageView;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setValueToTextView;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setViewVisibility;

class FindMyDeviceViewHolder extends RecyclerView.ViewHolder {

    private final View mView;
    private final TextView deviceNameView;
    private final TextView deviceIdView;

    FindMyDeviceViewHolder(View view) {
        super(view);
        mView = view;
        deviceNameView = view.findViewById(R.id.device_name);
        deviceIdView = view.findViewById(R.id.device_id);
    }

    void bind(DeviceDTO deviceDTO, View.OnClickListener onClickListener) {
        bindDeviceInformation(deviceDTO);
        if (DeviceUtils.hasInterfaces(deviceDTO)) {
            bindInterfaceInformation(deviceDTO);
        }

        mView.setOnClickListener(onClickListener);
    }

    private void bindDeviceInformation(DeviceDTO deviceDTO) {
        deviceNameView.setText(deviceDTO.getName());
        deviceIdView.setText(deviceDTO.getId());
    }

    private void bindInterfaceInformation(DeviceDTO deviceDTO) {
        InterfaceDTO interfaceDTO = DeviceUtils.getInterfaceDTO(deviceDTO);
        bindDeviceStatusImage(interfaceDTO);
        bindLastCommunicationDateInformation(interfaceDTO);
        bindSignalLevelInformation(interfaceDTO);
    }

    private void bindDeviceStatusImage(InterfaceDTO interfaceDTO) {
        int deviceStatusImage = DeviceUtils.getDeviceStatusImage(interfaceDTO.getStatus());
        Drawable img = getDrawable(deviceStatusImage);
        deviceNameView.setCompoundDrawables(img, null, null, null);
    }

    private void bindLastCommunicationDateInformation(InterfaceDTO interfaceDTO) {
        Date localDate = DateUtils.parseToLocalDate(interfaceDTO.getLastContact());
        setValueToTextView(mView, R.id.last_comm, DateUtils.formatAsAppDate(localDate));
    }

    private void bindSignalLevelInformation(InterfaceDTO interfaceDTO) {
        if (isLoraConnectivity(interfaceDTO)) {
            int signalLevelImage = DeviceUtils.getSignalLevelImage(interfaceDTO);
            setImageToImageView(mView, R.id.network_signal, signalLevelImage);
            setViewVisibility(mView, R.id.network_signal_layout, true);
        } else {
            setViewVisibility(mView, R.id.network_signal_layout, false);
        }
    }

    @Nullable
    private Drawable getDrawable(int deviceStatusImage) {
        Context context = mView.getContext();
        Drawable img = context.getDrawable(deviceStatusImage);
        if (img != null) {
            img.setBounds(0, 0, 30, 30);
        }
        return img;
    }
}

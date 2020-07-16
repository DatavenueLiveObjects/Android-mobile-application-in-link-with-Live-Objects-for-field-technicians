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

package com.company.iotfortechnicians.editname;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;
import com.company.iotfortechnicians.common.service.DeviceManagementService;

import static com.company.iotfortechnicians.common.Constants.DEVICE_DATA_KEY;

public class EditNameFragment extends AbstractIOTApiCallbackFragment<DeviceDTO> {

    private DeviceDTO deviceDTO;
    private TextInputEditText editNameText;

    public static EditNameFragment newInstance(DeviceDTO deviceDTO) {
        EditNameFragment fragment = new EditNameFragment();
        Bundle args = new Bundle();
        args.putSerializable(DEVICE_DATA_KEY, deviceDTO);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deviceDTO = (DeviceDTO) getArguments().getSerializable(DEVICE_DATA_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_edit_name, container, false);

        editNameText = inflatedView.findViewById(R.id.edit_name_text);
        editNameText.setText(deviceDTO.getName());

        ImageView backImage = inflatedView.findViewById(R.id.edit_name_back);
        backImage.setOnClickListener(v -> goToPreviousScreen());

        Button saveButton = inflatedView.findViewById(R.id.save_edit_name);
        saveButton.setOnClickListener(v -> executeSaveName());

        return inflatedView;
    }

    private void goToPreviousScreen() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    private void executeSaveName() {
        DeviceManagementService patchNameService = ApiClient.createApiService(DeviceManagementService.class);
        String deviceName = editNameText.getText().toString();
        deviceDTO.setName(deviceName);
        patchNameService.patchDevice(deviceDTO.getId(), deviceDTO).enqueue(this);
    }

    @Override
    protected void processData(DeviceDTO data) {
        Toast.makeText(getActivity(), R.string.updated_name_info, Toast.LENGTH_LONG).show();
        Intent goBackIntent = new Intent();
        goBackIntent.putExtra(Constants.DEVICE_NAME_KEY, editNameText.getText().toString());
        getActivity().setResult(Activity.RESULT_OK, goBackIntent);
        getActivity().finish();
    }
}

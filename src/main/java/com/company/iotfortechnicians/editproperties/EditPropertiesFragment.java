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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.PatchPropertiesRequestDTO;
import com.company.iotfortechnicians.common.service.DeviceManagementService;
import com.company.iotfortechnicians.common.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.NoArgsConstructor;

import static com.company.iotfortechnicians.common.Constants.DEVICE_DATA_KEY;
import static com.company.iotfortechnicians.editproperties.EditPropertiesRecyclerViewAdapter.PROPERTIES_TO_SKIP;

@NoArgsConstructor
public class EditPropertiesFragment extends AbstractIOTApiCallbackFragment<DeviceDTO> {

    private DeviceDTO deviceDTO;
    private EditPropertiesRecyclerViewAdapter adapter;

    public static EditPropertiesFragment newInstance(DeviceDTO deviceDTO) {
        EditPropertiesFragment fragment = new EditPropertiesFragment();
        Bundle args = new Bundle();
        args.putSerializable(DEVICE_DATA_KEY, deviceDTO);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            deviceDTO = (DeviceDTO) getArguments().getSerializable(DEVICE_DATA_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_edit_properties, container, false);

        RecyclerView recyclerView = inflatedView.findViewById(R.id.properties_list);
        prepareRecyclerView(recyclerView);

        ImageView goBack = inflatedView.findViewById(R.id.edit_props_back);
        goBack.setOnClickListener(v -> goToPreviousScreen());

        Button addNew = inflatedView.findViewById(R.id.add_new_property);
        addNew.setOnClickListener(v -> {
            adapter.addNewItem();
        });

        Button saveButton = inflatedView.findViewById(R.id.save_edit_props_btn);
        saveButton.setOnClickListener(v -> executePropsPatch());

        addGlobalLayoutListener(R.id.abstractFragmentScrollView, null);

        return inflatedView;
    }

    private void goToPreviousScreen() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    private void prepareRecyclerView(RecyclerView recyclerView) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EditPropertiesRecyclerViewAdapter(deviceDTO.getProperties());
        recyclerView.setAdapter(adapter);
    }

    private void executePropsPatch() {
        try {
            PatchPropertiesRequestDTO patchPropertiesRequestDTO = getPatchPropertiesRequestDTO();
            validateProperties(patchPropertiesRequestDTO);
            DeviceManagementService patchPropsService = ApiClient.createApiService(DeviceManagementService.class);
            patchPropsService.patchProperties(deviceDTO.getId(), patchPropertiesRequestDTO).enqueue(this);
        } catch (PropertyNotValidException e) {
            String format = String.format(getString(R.string.empty_name), e.getPropertyValue());
            new AlertDialog.Builder(getActivity())
                    .setMessage(format)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    }

    private void validateProperties(PatchPropertiesRequestDTO patchPropertiesRequestDTO) throws PropertyNotValidException {
        Map<String, String> properties = patchPropertiesRequestDTO.getProperties();
        Set<Map.Entry<String, String>> entries = properties.entrySet();
        for (Map.Entry<String, String> e : entries) {
            if (StringUtils.isEmpty(e.getKey())) {
                throw new PropertyNotValidException(e.getValue());
            }
        }
    }

    private HashMap<String, String> getNewPropertiesMap() {
        HashMap<String, String> properties = adapter.getProperties();
        copySkippedProperties(properties);
        return properties;
    }

    private void copySkippedProperties(Map<String, String> properties) {
        Map<String, String> deviceDTOProperties = deviceDTO.getProperties();
        for (String propertyToSkip : PROPERTIES_TO_SKIP) {
            String property = deviceDTOProperties.get(propertyToSkip);
            if (property != null) {
                properties.put(propertyToSkip, property);
            }
        }
    }

    @NonNull
    private PatchPropertiesRequestDTO getPatchPropertiesRequestDTO() {
        HashMap<String, String> properties = getNewPropertiesMap();
        Map<String, String> deviceDTOProperties = deviceDTO.getProperties();
        for (Map.Entry<String, String> e : deviceDTOProperties.entrySet()) {
            if (!properties.containsKey(e.getKey())) {
                properties.put(e.getKey(), null);
            }
        }

        return new PatchPropertiesRequestDTO(properties);
    }

    @Override
    protected void processData(DeviceDTO data) {
        Toast.makeText(getActivity(), R.string.updated_prop_info, Toast.LENGTH_LONG).show();
        Intent goBackIntent = new Intent();
        goBackIntent.putExtra(Constants.DEVICE_PROPERTIES_KEY, getNewPropertiesMap());
        getActivity().setResult(Activity.RESULT_OK, goBackIntent);
        getActivity().finish();
    }
}

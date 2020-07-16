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

package com.company.iotfortechnicians.deviceinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.InterfaceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.InterfaceStatus;
import com.company.iotfortechnicians.common.dto.devicemanagement.PatchInterfaceStatusRequestDTO;
import com.company.iotfortechnicians.common.service.DeviceManagementService;
import com.company.iotfortechnicians.common.util.DateUtils;
import com.company.iotfortechnicians.common.util.DeviceUtils;
import com.company.iotfortechnicians.common.util.PropsUtils;
import com.company.iotfortechnicians.common.util.StringUtils;
import com.company.iotfortechnicians.editname.EditNameActivity;
import com.company.iotfortechnicians.editproperties.EditPropertiesActivity;
import com.company.iotfortechnicians.edittag.EditTagActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.NoArgsConstructor;

import static android.app.Activity.RESULT_OK;
import static com.company.iotfortechnicians.common.Constants.DEVICE_DATA_KEY;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setImageToImageView;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setValueToTextView;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setViewVisibility;

@NoArgsConstructor
public class DeviceInfoFragment extends AbstractIOTApiCallbackFragment<InterfaceDTO> {

    private static final int DEVICE_NAME_REQUEST = 1;
    private static final int DEVICE_PROPERTIES_REQUEST = 2;
    private static final int DEVICE_TAG_REQUEST = 3;

    private DeviceDTO deviceDTO;
    private View inflatedLayout;

    public static DeviceInfoFragment newInstance(DeviceDTO deviceDTO) {
        DeviceInfoFragment fragment = new DeviceInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(DEVICE_DATA_KEY, deviceDTO);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deviceDTO = (DeviceDTO) getArguments().getSerializable(DEVICE_DATA_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflatedLayout = inflater.inflate(R.layout.fragment_device_info, container, false);
        displayDataFromDeviceDTO();

        TextView editName = inflatedLayout.findViewById(R.id.edit_name);
        editName.setOnClickListener(v -> goToEditName());

        TextView editProperties = inflatedLayout.findViewById(R.id.edit_properties);
        editProperties.setOnClickListener(v -> goToEditProperties());

        TextView editTag = inflatedLayout.findViewById(R.id.edit_tag);
        editTag.setOnClickListener(v -> goToEditTag());

        return inflatedLayout;
    }

    private void goToEditTag() {
        Intent intent = new Intent(getActivity(), EditTagActivity.class);
        intent.putExtra(DEVICE_DATA_KEY, deviceDTO);
        startActivityForResult(intent, DEVICE_TAG_REQUEST);
    }

    private void goToEditName() {
        Intent intent = new Intent(getActivity(), EditNameActivity.class);
        intent.putExtra(DEVICE_DATA_KEY, deviceDTO);
        startActivityForResult(intent, DEVICE_NAME_REQUEST);
    }

    private void goToEditProperties() {
        Intent intent = new Intent(getActivity(), EditPropertiesActivity.class);
        intent.putExtra(DEVICE_DATA_KEY, deviceDTO);
        startActivityForResult(intent, DEVICE_PROPERTIES_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            updateDataOnResult(requestCode, data);
        }
    }

    private void updateDataOnResult(int requestCode, Intent data) {
        switch (requestCode) {
            case DEVICE_NAME_REQUEST:
                String deviceName = data.getStringExtra(Constants.DEVICE_NAME_KEY);
                deviceDTO.setName(deviceName);
                setValueToTextView(inflatedLayout, R.id.device_data_name, deviceName);
                break;
            case DEVICE_PROPERTIES_REQUEST:
                HashMap<String, String> properties = (HashMap<String, String>) data.getSerializableExtra(Constants.DEVICE_PROPERTIES_KEY);
                deviceDTO.setProperties(properties);
                setValueToTextView(inflatedLayout, R.id.properties, getPropertiesString(properties));
                break;
            case DEVICE_TAG_REQUEST:
                List<String> tags = (List<String>) data.getSerializableExtra(Constants.DEVICE_TAGS_KEY);
                deviceDTO.setTags(tags);
                setValueToTextView(inflatedLayout, R.id.tag, StringUtils.join(tags, ","));
            default:
        }
    }

    private void displayDataFromDeviceDTO() {
        setValueToTextView(inflatedLayout, R.id.device_data_name, deviceDTO.getName());
        setValueToTextView(inflatedLayout, R.id.deviceId, deviceDTO.getId());
        setValueToTextView(inflatedLayout, R.id.default_stream_id, deviceDTO.getDefaultDataStreamId());
        setValueToTextView(inflatedLayout, R.id.group, deviceDTO.getGroup().getPath());
        setValueToTextView(inflatedLayout, R.id.tag, StringUtils.join(deviceDTO.getTags(), ","));
        setValueToTextView(inflatedLayout, R.id.properties, getPropertiesString(deviceDTO.getProperties()));
        setValueToTextView(inflatedLayout, R.id.lastUpdateDate, DateUtils.parseAndFormatDate(deviceDTO.getUpdated()));
        setValueToTextView(inflatedLayout, R.id.registrationDate, DateUtils.parseAndFormatDate(deviceDTO.getCreated()));
        showInterfaceInformation();
    }

    private void showInterfaceInformation() {
        if (DeviceUtils.hasInterfaces(deviceDTO)) {
            InterfaceDTO interfaceDTO = DeviceUtils.getInterfaceDTO(deviceDTO);
            InterfaceStatus deviceStatus = interfaceDTO.getStatus();
            int deviceStatusImage = DeviceUtils.getDeviceStatusImage(deviceStatus);
            setValueToTextView(inflatedLayout, R.id.connector, StringUtils.emptyIfNull(interfaceDTO.getConnector()));
            setImageToImageView(inflatedLayout, R.id.device_status_status_img, deviceStatusImage);
            setValueToTextView(inflatedLayout, R.id.device_status_status, deviceStatus.getCapitalizedLabel());
            prepareInterfaceStatusSwitch(interfaceDTO);
        }
    }

    private void prepareInterfaceStatusSwitch(InterfaceDTO interfaceDTO) {
        if (DeviceUtils.isLoraConnectivity(interfaceDTO) || DeviceUtils.isSMSConnectivity(interfaceDTO) || DeviceUtils.isMqttConnectivity(interfaceDTO)) {
            setViewVisibility(inflatedLayout, R.id.interface_status_switch, true);
            updateInterfaceStatusSwitch(interfaceDTO);
        } else {
            setViewVisibility(inflatedLayout, R.id.interface_status_switch, false);
        }
    }

    private void updateInterfaceStatusSwitch(InterfaceDTO interfaceDTO) {
        Switch interfaceStatusSwitch = inflatedLayout.findViewById(R.id.interface_status_switch);
        interfaceStatusSwitch.setChecked(interfaceDTO.isEnabled());
        interfaceStatusSwitch.setOnCheckedChangeListener(getOnCheckedChangeListener(interfaceDTO));
    }

    private CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener(InterfaceDTO interfaceDTO) {
        return (compoundButton, b) -> {
            PatchInterfaceStatusRequestDTO patchRequestDTO = new PatchInterfaceStatusRequestDTO(b, deviceDTO.getId());
            DeviceManagementService deviceManagementService = ApiClient.createApiService(DeviceManagementService.class);
            deviceManagementService.patchInterfaceStatus(deviceDTO.getId(), interfaceDTO.getConnector(), interfaceDTO.getNodeId(), patchRequestDTO)
                    .enqueue(this);
        };
    }

    private String getPropertiesString(Map<String, String> properties) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            if (PropsUtils.hasNoStaticLocationProp(entry)) {
                sb.append(entry.getKey())
                        .append("   ")
                        .append(entry.getValue())
                        .append("\n");

            }
        }
        return sb.toString();
    }

    @Override
    protected void processData(InterfaceDTO data) {
        List<InterfaceDTO> interfaces = deviceDTO.getInterfaces();
        interfaces.set(0, data);
        showInterfaceInformation();
    }
}

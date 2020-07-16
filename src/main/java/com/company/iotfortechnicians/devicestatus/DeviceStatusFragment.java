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

package com.company.iotfortechnicians.devicestatus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.activitylog.list.ActivityLogListActivity;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.DynamicLocationFetchInterface;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.InterfaceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.InterfaceStatus;
import com.company.iotfortechnicians.common.dto.devicemanagement.LocationDTO;
import com.company.iotfortechnicians.common.service.DataFetchService;
import com.company.iotfortechnicians.common.service.DeviceManagementService;
import com.company.iotfortechnicians.common.util.DateUtils;
import com.company.iotfortechnicians.common.util.DeviceUtils;
import com.company.iotfortechnicians.common.util.PropsUtils;
import com.company.iotfortechnicians.deviceinfo.DeviceInfoActivity;
import com.company.iotfortechnicians.devicepayload.DevicePayloadActivity;
import com.company.iotfortechnicians.geotagmydevice.GeotagActivity;
import com.company.iotfortechnicians.networkcoverage.NetworkCoverageActivity;
import com.company.iotfortechnicians.showonmap.ShowOnMapActivity;

import java.util.Date;

import lombok.NoArgsConstructor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.company.iotfortechnicians.common.Constants.TIME_TO_CHANGE_LAST_COMMUNICATION_COLOR_TO_LOW;
import static com.company.iotfortechnicians.common.Constants.TIME_TO_CHANGE_LAST_COMMUNICATION_COLOR_TO_MEDIUM;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setImageToImageView;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setValueToTextView;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setViewBackgroundResource;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setViewEnabled;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setViewVisibility;

@NoArgsConstructor
public class DeviceStatusFragment extends AbstractIOTApiCallbackFragment<DeviceDTO> implements DynamicLocationFetchInterface {

    private static final int DEVICE_LOCATION_REQUEST = 2;

    private DeviceDTO deviceDTO;
    private String deviceId;
    private View inflatedView;

    public static DeviceStatusFragment newInstance(String deviceId) {
        DeviceStatusFragment fragment = new DeviceStatusFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DEVICE_ID_KEY, deviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            deviceId = getArguments().getString(Constants.DEVICE_ID_KEY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        executeGetDevice(deviceId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_device_status, container, false);

        RelativeLayout deviceDetailsBtn = inflatedView.findViewById(R.id.more_info);
        deviceDetailsBtn.setOnClickListener(v -> goToDeviceInfo(deviceDTO));

        RelativeLayout payloadMessageBtn = inflatedView.findViewById(R.id.payload_message_btn);
        payloadMessageBtn.setOnClickListener(v -> goToPayloadMessage());

        TextView editLocationBtn = inflatedView.findViewById(R.id.edit_static_location);
        editLocationBtn.setOnClickListener(v -> goToGeotag());

        Button showOnMap = inflatedView.findViewById(R.id.show_on_map);
        showOnMap.setOnClickListener(v -> goToShowOnMap());

        setViewVisibility(inflatedView, R.id.device_status_signal_button, false);
        setViewVisibility(inflatedView, R.id.activity_log_btn, false);

        return inflatedView;
    }

    private void fetchDynamicLocation() {
        if (deviceId != null) {
            DataFetchService.fetchDynamicLocation(deviceId, this);
        }
    }

    public void onDynamicLocationFetched(LocationDTO location) {
        if (deviceDTO != null && location != null) {
            deviceDTO.setDynamicLocation(location);
            displayDynamicLocation();
        }

        hideLoadingDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh_btn) {
            executeGetDevice(deviceDTO.getId());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setNetworkCoverageOnClickListener(InterfaceDTO interfaceDTO, RelativeLayout networkCoverageBtn) {
        networkCoverageBtn.setOnClickListener(v -> {
            Integer lastSignalLevel = DeviceUtils.getLastSignalLevel(interfaceDTO);
            goToNetworkCoverage(lastSignalLevel);
        });
    }

    private void goToGeotag() {
        Intent intent = new Intent(getActivity(), GeotagActivity.class);
        intent.putExtra(Constants.DEVICE_DATA_KEY, deviceDTO);
        startActivityForResult(intent, DEVICE_LOCATION_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DEVICE_LOCATION_REQUEST && resultCode == RESULT_OK)
            displayLocationInformation();
    }

    private void goToShowOnMap() {
        Intent intent = new Intent(getActivity(), ShowOnMapActivity.class);
        intent.putExtra(Constants.DEVICE_DATA_KEY, deviceDTO);
        startActivity(intent);
    }

    private void executeGetDevice(String deviceId) {
        showLoadingDialog();

        DeviceManagementService dmService = ApiClient.createApiService(DeviceManagementService.class);
        dmService.getDevice(deviceId).enqueue(this);
    }

    private void goToDeviceInfo(DeviceDTO deviceDTO) {
        Intent intent = new Intent(getActivity(), DeviceInfoActivity.class);
        intent.putExtra(Constants.DEVICE_DATA_KEY, deviceDTO);
        startActivity(intent);
    }

    private void goToPayloadMessage() {
        Intent intent = new Intent(getActivity(), DevicePayloadActivity.class);
        intent.putExtra(Constants.DEVICE_ID_KEY, deviceDTO.getId());
        startActivity(intent);
    }

    private void goToActivityLog(InterfaceDTO interfaceDTO) {
        Intent intent = new Intent(getActivity(), ActivityLogListActivity.class);
        intent.putExtra(Constants.NODE_ID_KEY, interfaceDTO.getNodeId());
        startActivity(intent);
    }

    private void goToNetworkCoverage(Integer signalLevel) {
        if (signalLevel == null)
            signalLevel = 0;

        Intent intent = new Intent(getActivity(), NetworkCoverageActivity.class);
        intent.putExtra(Constants.STREAM_ID_KEY, deviceDTO.getDefaultDataStreamId());
        intent.putExtra(Constants.SIGNAL_LEVEL_ID_KEY, signalLevel);
        startActivity(intent);
    }

    private void displayDataFromDeviceDTO() {
        InterfaceDTO interfaceDTO = DeviceUtils.getInterfaceDTO(deviceDTO);
        if (DeviceUtils.isLoraConnectivity(interfaceDTO)) {
            displaySignalLevelButton(interfaceDTO);
            displayActivityLogsButton(interfaceDTO);
        }
        setValueToTextView(inflatedView, R.id.device_status_id, deviceDTO.getId());
        setValueToTextView(inflatedView, R.id.device_status_name, deviceDTO.getName());
        displayStatusFromInterfaceDTO(interfaceDTO);
        displayLastContactInformation(inflatedView, interfaceDTO);
        displaySignalLvlFromInterfaceDTO(interfaceDTO);
        displayLocationInformation();
    }

    private void displaySignalLevelButton(InterfaceDTO interfaceDTO) {
        setViewVisibility(inflatedView, R.id.device_status_signal_button, true);
        RelativeLayout networkCoverageBtn = inflatedView.findViewById(R.id.device_status_signal);
        setNetworkCoverageOnClickListener(interfaceDTO, networkCoverageBtn);
    }

    private void displayActivityLogsButton(InterfaceDTO interfaceDTO) {
        setViewVisibility(inflatedView, R.id.activity_log_btn, true);
        View activityLogBtn = inflatedView.findViewById(R.id.activity_log_btn);
        activityLogBtn.setOnClickListener(v -> goToActivityLog(interfaceDTO));
    }

    private void displayLocationInformation() {
        displayIndoorLocation();
        displayStaticLocation();
        displayDynamicLocation();
    }

    private boolean isLocationAvailable() {
        LocationDTO staticLocation = PropsUtils.getStaticLocationFromProps(this.deviceDTO.getProperties());
        LocationDTO dynamicLocation = deviceDTO.getDynamicLocation();

        return staticLocation != null || dynamicLocation != null;
    }

    private void displayShowOnMap() {
        setViewEnabled(inflatedView, R.id.show_on_map, isLocationAvailable());
    }

    private void displayStaticLocation() {
        String staticLocationText = PropsUtils.getStaticLocationText(deviceDTO.getProperties());
        if (staticLocationText == null)
            staticLocationText = getString(R.string.no_static_location_set);
        setValueToTextView(inflatedView, R.id.device_status_static_location, staticLocationText);
    }

    private void displayDynamicLocation() {
        String dynamicLocationText = getString(R.string.no_dynamic_location_available);

        LocationDTO location = deviceDTO.getDynamicLocation();
        if (location != null)
            dynamicLocationText = location.toString();
        setValueToTextView(inflatedView, R.id.device_status_dynamic_location, dynamicLocationText);
        displayShowOnMap();
    }

    private void displayIndoorLocation() {
        String indoorProp = PropsUtils.getIndoorLocation(deviceDTO.getProperties());
        setValueToTextView(inflatedView, R.id.device_status_indoor_location, indoorProp);
    }

    private void displayStatusFromInterfaceDTO(InterfaceDTO interfaceDTO) {
        if (interfaceDTO != null) {
            InterfaceStatus deviceStatus = interfaceDTO.getStatus();
            int deviceStatusImage = DeviceUtils.getDeviceStatusImage(deviceStatus);
            setImageToImageView(inflatedView, R.id.device_status_status_img, deviceStatusImage);
            setValueToTextView(inflatedView, R.id.device_status_status, deviceStatus.getCapitalizedLabel());
            setValueToTextView(inflatedView, R.id.interface_connectivity, interfaceDTO.getConnector());
        }
    }

    @Override
    protected void processData(DeviceDTO data) {
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            deviceDTO = data;
            displayDataFromDeviceDTO();
            fetchDynamicLocation();
        }
    }

    @Override
    public void onResponse(@NonNull Call<DeviceDTO> call, Response<DeviceDTO> response) {
        if (response.isSuccessful()) {
            DeviceDTO body = response.body();
            processData(body);
        } else {
            int code = response.code();
            ResponseBody responseBody = response.errorBody();
            processResponseErrorBody(code, responseBody);
        }
    }

    private void displaySignalLvlFromInterfaceDTO(InterfaceDTO interfaceDTO) {
        Integer lastSignalLevel = DeviceUtils.getLastSignalLevel(interfaceDTO);
        int signalLevelImage = DeviceUtils.getSignalLevelImage(lastSignalLevel);
        setImageToImageView(inflatedView, R.id.device_status_signal_lvl, signalLevelImage);
    }

    private void displayLastContactInformation(View inflatedView, InterfaceDTO interfaceDTO) {
        if (interfaceDTO != null) {
            Date localDate = DateUtils.parseToLocalDate(interfaceDTO.getLastContact());
            setValueToTextView(inflatedView, R.id.device_status_last_update_date, DateUtils.formatAsAppDate(localDate));
            setBackground(inflatedView, localDate);
        }
    }

    private void setBackground(View inflatedView, Date localDate) {
        Long timeDifference = DateUtils.getTimeDifference(localDate, new Date());
        if (localDate == null)
            setIconAndBackgroundColor(inflatedView, R.color.lastDateGrey, R.drawable.no_connection);
        else if (timeDifference < TIME_TO_CHANGE_LAST_COMMUNICATION_COLOR_TO_MEDIUM) {
            setIconAndBackgroundColor(inflatedView, R.color.lastDateGreen, R.drawable.positive);
        } else if (timeDifference < TIME_TO_CHANGE_LAST_COMMUNICATION_COLOR_TO_LOW) {
            setIconAndBackgroundColor(inflatedView, R.color.lastDateYellow, R.drawable.warning);
        } else {
            setIconAndBackgroundColor(inflatedView, R.color.lastDateRed, R.drawable.critical);
        }
    }

    private void setIconAndBackgroundColor(View inflatedView, int color, int icon) {
        setViewBackgroundResource(inflatedView, R.id.device_last_comm_color, color);
        setViewBackgroundResource(inflatedView, R.id.device_last_comm_icon, icon);
    }
}

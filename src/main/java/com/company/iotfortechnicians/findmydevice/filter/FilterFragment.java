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

package com.company.iotfortechnicians.findmydevice.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.components.select.SelectionItem;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.GroupDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.InterfaceStatus;
import com.company.iotfortechnicians.common.service.DeviceManagementService;
import com.company.iotfortechnicians.common.util.StringUtils;
import com.company.iotfortechnicians.findmydevice.DeviceSearchParameters;
import com.company.iotfortechnicians.findmydevice.FindMyDeviceSearchType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.company.iotfortechnicians.common.Constants.OFFSET;

public class FilterFragment extends AbstractIOTApiCallbackFragment<List<DeviceDTO>> implements OnFilterValueChanged {

    private Button submitButton;
    private boolean valuesFomParameters;
    private DeviceSearchParameters parameters;
    private EnumMap<FindMyDeviceSearchType, AbstractFilterView> filters;

    public FilterFragment() {
        valuesFomParameters = true;
        parameters = new DeviceSearchParameters();
        filters = new EnumMap<>(FindMyDeviceSearchType.class);
    }

    public static FilterFragment newInstance(DeviceSearchParameters parameters) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DEVICE_SEARCH_PARAMETERS_KEY, parameters);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parameters = (DeviceSearchParameters) getSerializable(Constants.DEVICE_SEARCH_PARAMETERS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_filter, container, false);
        initFilters(inflatedView);
        setFilterItems(getInterfaceStatusSelectionFilterItems(), FindMyDeviceSearchType.INTERFACE_STATUS);
        ImageView backImage = inflatedView.findViewById(R.id.filters_back_btn);
        backImage.setOnClickListener(v -> {
            closeKeyboard();
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.finish();
            }
        });
        submitButton = inflatedView.findViewById(R.id.button_submit);
        submitButton.setOnClickListener(view -> collectFilterValuesAndGoBack());
        sendGroupRequest();

        return inflatedView;
    }

    private void initFilters(View inflatedView) {
        filters.put(FindMyDeviceSearchType.DEVICE_ID, inflatedView.findViewById(R.id.device_id));
        filters.put(FindMyDeviceSearchType.DEVICE_NAME, inflatedView.findViewById(R.id.device_name));
        filters.put(FindMyDeviceSearchType.DEVICE_GROUP, inflatedView.findViewById(R.id.device_group));
        filters.put(FindMyDeviceSearchType.INTERFACE_STATUS, inflatedView.findViewById(R.id.interface_status));
        for (AbstractFilterView filter : filters.values()) {
            filter.setOnFilterValueChanged(this);
        }
    }

    private void sendGetDevicesRequest() {
        DeviceManagementService findDeviceService = ApiClient.createApiService(DeviceManagementService.class);
        Map<String, String> options = parameters.toRequestParams();
        options.put(OFFSET, String.valueOf(2));
        findDeviceService.getDevices(options).enqueue(this);
    }

    private void setFilterItems(List<SelectionItem<String>> selectionItems, FindMyDeviceSearchType searchType) {
        SelectionFilterView abstractFilterView = (SelectionFilterView) filters.get(searchType);
        abstractFilterView.setValues(selectionItems);
    }

    private void collectFilterValuesAndGoBack() {
        closeKeyboard();
        parameters.clearFilters();
        for (Map.Entry<FindMyDeviceSearchType, AbstractFilterView> entry : filters.entrySet()) {
            AbstractFilterView filterView = entry.getValue();
            parameters.addFilter(entry.getKey(), filterView.getFilterValue());
        }
        goBack();
    }

    private void goBack() {
        Intent goBackIntent = new Intent();
        goBackIntent.putExtra(Constants.DEVICE_SEARCH_PARAMETERS_KEY, parameters);
        getActivity().setResult(Activity.RESULT_OK, goBackIntent);
        getActivity().finish();
    }

    private void sendGroupRequest() {
        showLoadingDialog();
        DeviceManagementService deviceManagementService = ApiClient.createApiService(DeviceManagementService.class);
        deviceManagementService.getGroups().enqueue(new GroupListCallback(this));
    }

    @Override
    protected void processData(List<DeviceDTO> body) {
        valuesFomParameters = false;
        int xTotalCount = getXTotalCount();
        String string = getString(R.string.button_show_results, xTotalCount);
        submitButton.setText(string);
    }

    void onGroupCallbackResponse(List<GroupDTO> groups) {
        hideLoadingDialog();
        setFilterItems(getGroupSelectionFilterItems(groups), FindMyDeviceSearchType.DEVICE_GROUP);
        setFilterValues();
        sendGetDevicesRequest();
    }

    private void setFilterValues() {
        Map<FindMyDeviceSearchType, String> parametersFilters = parameters.getFilters();
        for (Map.Entry<FindMyDeviceSearchType, String> filter : parametersFilters.entrySet()) {
            AbstractFilterView abstractFilterView = filters.get(filter.getKey());
            abstractFilterView.setFilterValue(filter.getValue());
        }
        valuesFomParameters = false;
    }

    private List<SelectionItem<String>> getGroupSelectionFilterItems(List<GroupDTO> groups) {
        List<SelectionItem<String>> filterItems = new ArrayList<>();
        for (GroupDTO group : groups) {
            SelectionItem item = getGroupSelectionFilterItem(group);
            filterItems.add(item);
        }
        return filterItems;
    }

    private SelectionItem<String> getGroupSelectionFilterItem(GroupDTO group) {
        String path = group.getPath();
        int slashCount = StringUtils.countMatches(path, Constants.SLASH);
        String repeat = StringUtils.repeat(Constants.TAB, slashCount - 1);
        String pathNode = repeat + group.getPathNode();
        String value = Constants.SLASH.equals(path) ? Constants.GROUP_PATH_SUFFIX : path + Constants.GROUP_PATH_SUFFIX;
        return new SelectionItem(pathNode, value);
    }

    private List<SelectionItem<String>> getInterfaceStatusSelectionFilterItems() {
        List<SelectionItem<String>> filterItems = new ArrayList<>();
        for (InterfaceStatus interfaceStatus : InterfaceStatus.values()) {
            filterItems.add(new SelectionItem(interfaceStatus.getStatusLabel(), interfaceStatus.getStatusLabel()));
        }
        return filterItems;
    }

    @Override
    public void onFilterValueChanged(FindMyDeviceSearchType searchType, String newFilterValue) {
        if (!valuesFomParameters && isAdded()) {
            parameters.addFilter(searchType, newFilterValue);
            sendGetDevicesRequest();
        }
    }
}
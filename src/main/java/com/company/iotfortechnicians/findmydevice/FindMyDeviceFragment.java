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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;
import com.company.iotfortechnicians.common.service.DeviceManagementService;
import com.company.iotfortechnicians.devicestatus.DeviceStatusActivity;
import com.company.iotfortechnicians.findmydevice.filter.FilterActivity;
import com.company.iotfortechnicians.findmydevice.sort.SortDialog;

import java.util.List;

public class FindMyDeviceFragment extends AbstractIOTApiCallbackFragment<List<DeviceDTO>>
        implements FindMyDeviceFragmentInterface {

    private static final int SEARCH_PARAMETERS_REQUEST_CODE = 1235;
    private DeviceSearchParameters parameters;
    private FindMyDeviceRecyclerViewAdapter recyclerViewAdapter;
    private TextView filterBadge;
    private TextView sortedBy;
    private TextView itemsCount;

    public FindMyDeviceFragment() {
        parameters = new DeviceSearchParameters();
    }

    public static FindMyDeviceFragment newInstance(DeviceSearchParameters parameters) {
        FindMyDeviceFragment fragment = new FindMyDeviceFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DEVICE_SEARCH_PARAMETERS_KEY, parameters);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            DeviceSearchParameters deviceSearchParameters = (DeviceSearchParameters) arguments
                    .getSerializable(Constants.DEVICE_SEARCH_PARAMETERS_KEY);
            if (deviceSearchParameters != null) {
                this.parameters = deviceSearchParameters;
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        requestGetDevices();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_find_my_device, container, false);

        filterBadge = inflatedView.findViewById(R.id.filter_badge_notification);
        sortedBy = inflatedView.findViewById(R.id.sorted_by);
        itemsCount = inflatedView.findViewById(R.id.items_count);
        RecyclerView recyclerView = inflatedView.findViewById(R.id.results_list);
        prepareRecyclerView(recyclerView);
        prepareFilterButton(inflatedView);
        prepareSortButton(inflatedView);
        setFilterCountToBadge();
        setItemsCount(0);
        setSortedBy();

        return inflatedView;
    }

    private void prepareSortButton(View inflatedView) {
        Button sortButton = inflatedView.findViewById(R.id.button_sort);
        sortButton.setOnClickListener(view -> {
            FragmentManager parentFragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = parentFragmentManager.beginTransaction();
            SortDialog sortDialog = SortDialog.newInstance(parameters);
            sortDialog.setTargetFragment(this, SEARCH_PARAMETERS_REQUEST_CODE);
            sortDialog.show(fragmentTransaction, "dialog");
        });
    }

    private void prepareFilterButton(View inflatedView) {
        Button filterButton = inflatedView.findViewById(R.id.button_filter);
        filterButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), FilterActivity.class);
            intent.putExtra(Constants.DEVICE_SEARCH_PARAMETERS_KEY, parameters);
            startActivityForResult(intent, SEARCH_PARAMETERS_REQUEST_CODE);
        });
    }

    private void setFilterCountToBadge() {
        int count = parameters.getFiltersCount();
        filterBadge.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        filterBadge.setText(String.valueOf(count));
    }

    private void setItemsCount(int xTotalCount) {
        String string = getString(R.string.items_count, xTotalCount);
        itemsCount.setText(string);
    }

    private void setSortedBy() {
        String string = getString(R.string.sorted_by, parameters.getSortBy());
        sortedBy.setText(string);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SEARCH_PARAMETERS_REQUEST_CODE) {
            parameters = (DeviceSearchParameters) data.getSerializableExtra(Constants.DEVICE_SEARCH_PARAMETERS_KEY);
            setFilterCountToBadge();
            setSortedBy();
            requestGetDevices();
        }
    }

    private void prepareRecyclerView(RecyclerView recyclerView) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new FindMyDeviceRecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addOnScrollListener(getRecyclerViewOnScrollListener(layoutManager));
    }

    @NonNull
    private RecyclerView.OnScrollListener getRecyclerViewOnScrollListener(final LinearLayoutManager layout) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerViewAdapter.isLoading() && !recyclerViewAdapter.hasAllItemsLoaded() && layout.findLastVisibleItemPosition() == recyclerViewAdapter.getItemCount() - 1) {
                    recyclerViewAdapter.setLoading(true);
                    showLoadingDialog();
                    executeGetDevicesRequest(recyclerViewAdapter.getItemCount());
                }
            }
        };
    }

    private void requestGetDevices() {
        recyclerViewAdapter.clear();
        recyclerViewAdapter.setLoading(true);
        executeGetDevicesRequest(Constants.OFFSET_VALUE);
    }

    private void executeGetDevicesRequest(int offsetValue) {
        parameters.setOffset(offsetValue);
        showLoadingDialog();
        DeviceManagementService findDeviceService = ApiClient.createApiService(DeviceManagementService.class);
        findDeviceService.getDevices(parameters.toRequestParams()).enqueue(this);
    }

    @Override
    protected void processData(List<DeviceDTO> body) {
        int xTotalCount = getXTotalCount();
        recyclerViewAdapter.setLoading(false);
        setItemsCount(xTotalCount);

        if (isEmptyResponse(body)) {
            showOkDialog(R.string.o_found_device);
        } else {
            recyclerViewAdapter.addAll(body, xTotalCount);
        }
        if (isOneResultResponse(body, xTotalCount)){
            DeviceDTO deviceDTO = body.get(0);
            onDeviceSelected(deviceDTO);
        }
        hideLoadingDialog();
    }

    @Override
    public void onDeviceSelected(DeviceDTO deviceDTO) {
        Intent intent = new Intent(getActivity(), DeviceStatusActivity.class);
        intent.putExtra(Constants.DEVICE_ID_KEY, deviceDTO.getId());
        startActivity(intent);
    }

    private boolean isOneResultResponse(List<DeviceDTO> body, int xTotalCount) {
        return body.size() == 1 && xTotalCount == 1;
    }
}

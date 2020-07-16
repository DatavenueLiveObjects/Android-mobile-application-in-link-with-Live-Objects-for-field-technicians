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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.dto.search.payload.HitDTO;
import com.company.iotfortechnicians.common.dto.search.payload.HitsDTO;
import com.company.iotfortechnicians.common.dto.search.payload.SearchDTO;
import com.company.iotfortechnicians.common.service.DataFetchService;
import com.company.iotfortechnicians.common.util.FragmentUtils;
import com.company.iotfortechnicians.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;

import static com.company.iotfortechnicians.common.Constants.DEVICE_ID_KEY;

@NoArgsConstructor
public class DevicePayloadFragment extends AbstractIOTApiCallbackFragment<SearchDTO> {

    private String deviceId;
    private View inflatedView;
    private DevicePayloadRecyclerViewAdapter payloadAdapter;

    public static DevicePayloadFragment newInstance(String deviceId) {
        DevicePayloadFragment fragment = new DevicePayloadFragment();
        Bundle args = new Bundle();
        args.putSerializable(DEVICE_ID_KEY, deviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deviceId = getArguments().getString(DEVICE_ID_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_device_payload, container, false);
        RecyclerView recyclerView = inflatedView.findViewById(R.id.results_list);
        final LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layout);
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator != null) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        payloadAdapter = new DevicePayloadRecyclerViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(payloadAdapter);
        FragmentUtils.setViewVisibility(inflatedView, R.id.no_payload, false);
        executeGetStreamsRequest();
        return inflatedView;
    }

    private void executeGetStreamsRequest() {
        if (StringUtils.isNoneEmpty(deviceId)) {
            showLoadingDialog();

            DataFetchService.fetchPayload(deviceId, this);
        } else {
            showNoMessageInfo();
        }
    }

    @Override
    protected void processData(SearchDTO data) {
        HitsDTO hits = data.getHits();
        List<HitDTO> hitList = hits.getHits();
        if (!hitList.isEmpty()) {
            displayDataFromHitsList(hitList);
        } else {
            showNoMessageInfo();
        }
    }

    private void displayDataFromHitsList(List<HitDTO> hitList) {
        payloadAdapter.replaceList(hitList);
    }

    private void showNoMessageInfo() {
        FragmentUtils.setViewVisibility(inflatedView, R.id.no_payload, true);
    }
}

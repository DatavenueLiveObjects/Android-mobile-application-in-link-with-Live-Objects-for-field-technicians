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

package com.company.iotfortechnicians.networkcoverage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.stream.LoraDTO;
import com.company.iotfortechnicians.common.dto.stream.MetadataDTO;
import com.company.iotfortechnicians.common.dto.stream.NetworkDTO;
import com.company.iotfortechnicians.common.dto.stream.StoredDataMessageDTO;
import com.company.iotfortechnicians.common.service.StreamService;
import com.company.iotfortechnicians.common.util.DateUtils;
import com.company.iotfortechnicians.common.util.DeviceUtils;
import com.company.iotfortechnicians.common.util.StringUtils;

import java.util.List;

import lombok.NoArgsConstructor;

import static com.company.iotfortechnicians.common.Constants.SIGNAL_LEVEL_ID_KEY;
import static com.company.iotfortechnicians.common.Constants.STREAM_ID_KEY;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setImageToImageView;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setValueToTextView;

@NoArgsConstructor
public class NetworkCoverageFragment extends AbstractIOTApiCallbackFragment<List<StoredDataMessageDTO>> {

    private String streamId;
    private int signalLevel;
    private View inflatedLayout;

    public static NetworkCoverageFragment newInstance(String streamId, int signalLevel) {
        NetworkCoverageFragment fragment = new NetworkCoverageFragment();
        Bundle args = new Bundle();
        args.putSerializable(STREAM_ID_KEY, streamId);
        args.putSerializable(SIGNAL_LEVEL_ID_KEY, signalLevel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            streamId = getArguments().getString(STREAM_ID_KEY);
            signalLevel = getArguments().getInt(SIGNAL_LEVEL_ID_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflatedLayout = inflater.inflate(R.layout.fragment_network_coverage, container, false);
        executeGetStreamsRequest();
        return inflatedLayout;
    }

    private void executeGetStreamsRequest() {
        if (StringUtils.isNoneEmpty(streamId)) {
            showLoadingDialog();
            StreamService streamService = ApiClient.createApiService(StreamService.class);
            streamService.getStreams(streamId).enqueue(this);
        } else {
            displayNoDataMessage();
        }
    }

    @Override
    protected void processData(List<StoredDataMessageDTO> data) {
        if (!data.isEmpty()) {
            displayDataFromStoredMessageDTO(data.get(0));
        } else {
            displayNoDataMessage();
        }
    }

    private void displayNoDataMessage() {
        String errorText = getResources().getString(R.string.no_network_data_available);
        Snackbar.make(inflatedLayout, errorText, Snackbar.LENGTH_INDEFINITE).show();
    }

    private void displayDataFromStoredMessageDTO(StoredDataMessageDTO data) {
        setValueToTextView(inflatedLayout, R.id.last_date, DateUtils.parseAndFormatDate(data.getTimestamp()));
        LoraDTO lora = getLora(data);
        if (lora != null) {
            displaySignalLvlToImageView(inflatedLayout);
            setValueToTextView(inflatedLayout, R.id.rssi, lora.getRssi());
            setValueToTextView(inflatedLayout, R.id.snr, lora.getSnr());
            setValueToTextView(inflatedLayout, R.id.esp, lora.getEsp());
            setValueToTextView(inflatedLayout, R.id.sf, lora.getSf());
            setValueToTextView(inflatedLayout, R.id.gateway_count, lora.getGatewayCnt());
        }
    }

    private void displaySignalLvlToImageView(View inflatedLayout) {
        int signalLevelImage = DeviceUtils.getSignalLevelImage(signalLevel);
        setImageToImageView(inflatedLayout, R.id.network_signal, signalLevelImage);
    }

    private LoraDTO getLora(StoredDataMessageDTO data) {
        try {
            MetadataDTO metadata = data.getMetadata();
            NetworkDTO network = metadata.getNetwork();
            return network.getLora();
        } catch (NullPointerException ex) {
            return null;
        }
    }
}

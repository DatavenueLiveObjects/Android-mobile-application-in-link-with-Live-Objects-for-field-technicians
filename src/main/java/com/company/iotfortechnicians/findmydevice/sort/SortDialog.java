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

package com.company.iotfortechnicians.findmydevice.sort;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.findmydevice.DeviceSearchParameters;
import com.company.iotfortechnicians.findmydevice.FindMyDeviceSortBy;

import java.util.HashMap;
import java.util.Map;

public class SortDialog extends DialogFragment implements View.OnClickListener {

    private DeviceSearchParameters parameters;
    private Map<Integer, FindMyDeviceSortBy> radioButtonIdToSortBy = new HashMap<>();

    public static SortDialog newInstance(DeviceSearchParameters parameters) {
        SortDialog fragment = new SortDialog();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DEVICE_SEARCH_PARAMETERS_KEY, parameters);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            parameters = (DeviceSearchParameters) arguments.getSerializable(Constants.DEVICE_SEARCH_PARAMETERS_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.sort_dialog_layout, container, false);
        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        RadioGroup mRgAllButtons = inflate.findViewById(R.id.sort_types);

        for (FindMyDeviceSortBy sort : FindMyDeviceSortBy.values()) {
            SortRadioButton button = getSortRadioButton(sort);
            mRgAllButtons.addView(button);
            radioButtonIdToSortBy.put(button.getId(), sort);
        }
    }

    private SortRadioButton getSortRadioButton(FindMyDeviceSortBy sort) {
        SortRadioButton button = new SortRadioButton(getActivity());
        button.setText(sort.toString());
        button.setOnClickListener(this);
        button.setChecked(sort.equals(parameters.getSortBy()));
        return button;
    }

    @Override
    public void onClick(View v) {
        FindMyDeviceSortBy sortBy = radioButtonIdToSortBy.get(v.getId());
        parameters.setSortBy(sortBy);
        sendResult();
    }

    private void sendResult() {
        if (getTargetFragment() == null) {
            dismiss();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.DEVICE_SEARCH_PARAMETERS_KEY, parameters);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();
    }
}

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

package com.company.iotfortechnicians.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.company.iotfortechnicians.IOTApplication;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.Session;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.users.UserDTO;
import com.company.iotfortechnicians.common.service.UsersService;
import com.company.iotfortechnicians.common.util.StringUtils;
import com.company.iotfortechnicians.findmydevice.FindMyDeviceActivity;
import com.company.iotfortechnicians.scanqrcode.ScanQRCodeActivity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MenuFragment extends AbstractIOTApiCallbackFragment<UserDTO> {

    private boolean reloadUserData;

    public static MenuFragment newInstance(boolean reloadUserData) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.RELOAD_USERDATA_KEY, reloadUserData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reloadUserData = getArguments().getBoolean(Constants.RELOAD_USERDATA_KEY, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_menu, container, false);

        CardView buttonFindSelectedDevice = inflatedView.findViewById(R.id.buttonFindSelectedDevice);
        buttonFindSelectedDevice.setOnClickListener(getSearchForDeviceOnClickListener());

        CardView buttonScanQRCode = inflatedView.findViewById(R.id.buttonScanQRCode);
        buttonScanQRCode.setOnClickListener(getScanQRCodeOnClickListener());

        if (reloadUserData) {
            sendReloadUserDataRequest();
        }

        return inflatedView;
    }

    private View.OnClickListener getSearchForDeviceOnClickListener() {
        return view -> {
            Intent intent = new Intent(getActivity(), FindMyDeviceActivity.class);
            startActivity(intent);
        };
    }

    private View.OnClickListener getScanQRCodeOnClickListener() {
        return view -> {
            Intent intent = new Intent(getActivity(), ScanQRCodeActivity.class);
            startActivity(intent);
        };
    }

    private void sendReloadUserDataRequest() {
        showSessionRestoringDialog();
        UsersService usersService = ApiClient.createApiService(UsersService.class);
        usersService.getMe().enqueue(this);
    }

    protected void storeUserDataToSession(UserDTO data) {
        if (data != null && StringUtils.isNoneEmpty(data.getLogin())) {
            Session session = IOTApplication.getSession();
            session.setLogin(data.getLogin());
            session.setEmail(data.getEmail());
            IOTApplication.storeSession();
        }
    }

    @Override
    protected void processData(UserDTO data) {
        storeUserDataToSession(data);
    }
}

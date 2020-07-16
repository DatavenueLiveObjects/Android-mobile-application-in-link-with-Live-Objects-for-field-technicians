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

package com.company.iotfortechnicians.agreeterms;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.company.iotfortechnicians.BuildConfig;
import com.company.iotfortechnicians.IOTApplication;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.Session;
import com.company.iotfortechnicians.common.util.SharedPreferencesUtils;
import com.company.iotfortechnicians.login.LoginActivity;
import com.company.iotfortechnicians.menu.MenuActivity;

public class AgreeTermsFragment extends AbstractIOTFragment {

    private View inflatedView;
    private Button acceptButton;

    public static AgreeTermsFragment newInstance() {
        return new AgreeTermsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_agree_terms, container, false);

        acceptButton = inflatedView.findViewById(R.id.accept_btn);
        acceptButton.setEnabled(false);
        acceptButton.setOnClickListener(v -> {
            saveAcceptanceOfTerms();
            goToLogin();
        });

        Button refuseButton = inflatedView.findViewById(R.id.refuse_btn);
        refuseButton.setOnClickListener(v -> {
            System.exit(0);
        });

        CheckBox acceptTermsCheckbox = inflatedView.findViewById(R.id.accept_terms_checkbox);
        acceptTermsCheckbox.setOnClickListener(this::onCheckboxClicked);

        return inflatedView;
    }

    private void saveAcceptanceOfTerms() {
        SharedPreferencesUtils.putInt(getActivity(), Constants.ACCEPTED_TERMS_KEY, BuildConfig.VERSION_CODE);
    }

    private void goToLogin() {
        Session session = IOTApplication.getSession();

        Intent mainIntent;
        if (session.isAuthenticated()) {
            mainIntent = new Intent(getActivity(), MenuActivity.class);
            mainIntent.putExtra(Constants.RELOAD_USERDATA_KEY, true);
        } else {
            mainIntent = LoginActivity.getActivityIntent(getActivity());
        }

        startActivity(mainIntent);
        getActivity().finish();
    }

    private void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        if (view.getId() == R.id.accept_terms_checkbox) {
            acceptButton.setEnabled(checked);
        }
    }
}

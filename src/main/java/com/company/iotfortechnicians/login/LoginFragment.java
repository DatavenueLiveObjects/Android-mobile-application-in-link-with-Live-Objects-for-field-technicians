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

package com.company.iotfortechnicians.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.company.iotfortechnicians.IOTApplication;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.Session;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.auth.ApiKeyDTO;
import com.company.iotfortechnicians.common.dto.auth.AuthRequestDTO;
import com.company.iotfortechnicians.common.dto.auth.AuthResponseDTO;
import com.company.iotfortechnicians.common.dto.error.ErrorDTO;
import com.company.iotfortechnicians.common.service.AuthService;
import com.company.iotfortechnicians.common.util.NetworkUtils;
import com.company.iotfortechnicians.common.util.StringUtils;
import com.company.iotfortechnicians.menu.MenuActivity;

import lombok.NoArgsConstructor;
import okhttp3.ResponseBody;

import static com.company.iotfortechnicians.common.util.DialogUtils.showNoConnectionAvailableDialog;

@NoArgsConstructor
public class LoginFragment extends AbstractIOTApiCallbackFragment<AuthResponseDTO> implements TextWatcher {

    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonLogin;
    private String login;

    public static LoginFragment newInstance(String login) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(Constants.USER_LOGIN_KEY, login);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            login = getArguments().getString(Constants.USER_LOGIN_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_login, container, false);

        editTextLogin = inflatedView.findViewById(R.id.editTextLogin);
        editTextLogin.setText(login);
        editTextLogin.addTextChangedListener(this);

        editTextPassword = inflatedView.findViewById(R.id.editTextPassword);
        setupEditTextPasswortListeners();

        buttonLogin = inflatedView.findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(getOnClickListener());
        buttonLogin.setEnabled(false);

        checkConnection();
        View loginTopLayout = inflatedView.findViewById(R.id.loginTopLayout);
        addGlobalLayoutListener(R.id.loginLayoutScroll, loginTopLayout);
        setPasswordLayoutTypeface(inflatedView);

        return inflatedView;
    }

    private void setupEditTextPasswortListeners() {
        editTextPassword.addTextChangedListener(this);
        editTextPassword.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    private void setPasswordLayoutTypeface(View inflatedView) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Typeface typeface = ResourcesCompat.getFont(activity, R.font.helveticaneue_bold);
            TextInputLayout editTextPasswordLayout = inflatedView.findViewById(R.id.editTextPasswordLayout);
            editTextPasswordLayout.setTypeface(typeface);
        }
    }

    private void checkConnection() {
        if (NetworkUtils.isOffline(getActivity())) {
            showNoConnectionAvailableDialog(getActivity());
        }
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Not needed
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        checkRequiredFields();
    }

    public void afterTextChanged(Editable editable) {
        //Not needed
    }

    @NonNull
    private View.OnClickListener getOnClickListener() {
        return view -> {
            showLoadingDialog();
            AuthRequestDTO loginRequestDTO = getLoginRequestDTO();
            AuthService authService = ApiClient.createApiService(AuthService.class);
            authService.login(loginRequestDTO).enqueue(LoginFragment.this);
        };
    }

    private AuthRequestDTO getLoginRequestDTO() {
        login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();

        return new AuthRequestDTO(login, password);
    }

    @Override
    protected void processData(AuthResponseDTO data) {
        Session session = getSession(data);
        IOTApplication.setSession(session);
        IOTApplication.storeSession();
        Intent intent = new Intent(getActivity(), MenuActivity.class);
        startActivity(intent);
    }

    private Session getSession(AuthResponseDTO data) {
        ApiKeyDTO apiKey = data.getApiKey();
        return new Session(apiKey.getValue(), login, apiKey.getUserId(), apiKey.getTenantId());
    }

    private void checkRequiredFields() {
        boolean enabled = isNotEmpty(editTextLogin) && isNotEmpty(editTextPassword);
        buttonLogin.setEnabled(enabled);
    }

    private boolean isNotEmpty(EditText editText) {
        return !editText.getText().toString().isEmpty();
    }

    private String getErrorResponseCode(ErrorDTO errorDTO) {
        return errorDTO != null ? errorDTO.getCode() : StringUtils.EMPTY;
    }

    private boolean isUserAuthenticationException(ResponseBody responseBody) {
        ErrorDTO errorDTO = getErrorDTO(responseBody);
        String code = getErrorResponseCode(errorDTO);
        return Constants.USER_AUTHENTICATION_EXCEPTION.equalsIgnoreCase(code);
    }

    @Override
    protected void processUnauthorizedResponse(ResponseBody responseBody) {
        if (isUserAuthenticationException(responseBody)) {
            showOkDialog(R.string.user_authentication_failed);
        } else {
            super.processUnauthorizedResponse(responseBody);
        }
    }
}

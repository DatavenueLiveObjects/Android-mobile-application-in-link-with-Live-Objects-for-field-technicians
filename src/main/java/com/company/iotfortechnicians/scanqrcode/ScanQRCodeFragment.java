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

package com.company.iotfortechnicians.scanqrcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;
import com.company.iotfortechnicians.common.service.DeviceManagementService;
import com.company.iotfortechnicians.common.util.PermissionUtils;
import com.company.iotfortechnicians.common.util.StringUtils;
import com.company.iotfortechnicians.devicestatus.DeviceStatusActivity;
import com.company.iotfortechnicians.findmydevice.DeviceSearchParameters;
import com.company.iotfortechnicians.findmydevice.FindMyDeviceActivity;
import com.company.iotfortechnicians.findmydevice.FindMyDeviceSearchType;

import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class ScanQRCodeFragment extends AbstractIOTApiCallbackFragment<List<DeviceDTO>> implements BarcodeCallback {

    private String deviceId;
    private DecoratedBarcodeView barcodeScannerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedLayout = inflater.inflate(R.layout.fragment_scan_qr_code, container, false);
        barcodeScannerView = inflatedLayout.findViewById(R.id.zxing_barcode_scanner);
        return inflatedLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getContext(), CAMERA) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{CAMERA}, Constants.CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startScan();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }

    @Override
    public void barcodeResult(BarcodeResult result) {
        String resultText = result.getText();

        if (StringUtils.isNoneEmpty(resultText)) {
            deviceId = extractDeviceId(resultText);
            sendGetDevicesRequest();
        } else {
            showNoPossibleToScanCodeDialog();
        }
    }

    private String extractDeviceId(String text) {
         if(text.contains(":") && text.startsWith("LW")){
            String[] codeParts = text.split(":");
            return codeParts[3].toLowerCase();
        }else{
            String[] codeParts = text.split(";");
            return codeParts[codeParts.length - 1];
        }
    }

    @Override
    public void possibleResultPoints(List<ResultPoint> resultPoints) {
        //Not needed
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != Constants.CAMERA_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, CAMERA)) {
            startScan();
        } else {
            PermissionUtils.showRequestPermissionRationaleDialogs(this, requestCode, permissions);
        }
    }

    @Override
    protected void processData(List<DeviceDTO> data) {
        hideLoadingDialog();
        if (isEmptyResponse(data)) {
            showZeroDevicesFoundDialog();
        } else if (data.size() == 1) {
            redirectToDeviceStatusScreen(data);
        } else {
            redirectToFindMyDeviceScreen();
        }
    }

    private void showZeroDevicesFoundDialog() {
        showOkDialog(R.string.o_found_device, getOnClickListener());
    }

    private void showNoPossibleToScanCodeDialog() {
        showOkDialog(R.string.no_possible_to_scan_code, getOnClickListener());
    }

    private void redirectToFindMyDeviceScreen() {
        DeviceSearchParameters parameters = getDeviceSearchParameters();
        Intent intent = new Intent(getActivity(), FindMyDeviceActivity.class);
        intent.putExtra(Constants.DEVICE_SEARCH_PARAMETERS_KEY, parameters);
        startActivity(intent);
    }

    private void redirectToDeviceStatusScreen(List<DeviceDTO> data) {
        DeviceDTO deviceDTO = data.get(0);
        Intent intent = new Intent(getActivity(), DeviceStatusActivity.class);
        intent.putExtra(Constants.DEVICE_ID_KEY, deviceDTO.getId());
        startActivity(intent);
    }

    private void sendGetDevicesRequest() {
        DeviceSearchParameters parameters = getDeviceSearchParameters();
        parameters.setLimit(2);
        showLoadingDialog();
        DeviceManagementService findDeviceService = ApiClient.createApiService(DeviceManagementService.class);
        findDeviceService.getDevices(parameters.toRequestParams()).enqueue(this);
    }

    private DialogInterface.OnClickListener getOnClickListener() {
        return (dialogInterface, i) -> barcodeScannerView.decodeSingle(this);
    }

    private DeviceSearchParameters getDeviceSearchParameters() {
        DeviceSearchParameters deviceSearchParameters = new DeviceSearchParameters();
        deviceSearchParameters.addFilter(FindMyDeviceSearchType.DEVICE_ID, deviceId);
        return deviceSearchParameters;
    }

    private void startScan() {
        barcodeScannerView.resume();
        barcodeScannerView.decodeSingle(this);
    }
}

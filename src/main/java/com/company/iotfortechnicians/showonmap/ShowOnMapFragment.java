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

package com.company.iotfortechnicians.showonmap;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTMapFragment;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.InterfaceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.LocationDTO;
import com.company.iotfortechnicians.common.util.DateUtils;
import com.company.iotfortechnicians.common.util.DeviceUtils;
import com.company.iotfortechnicians.common.util.PropsUtils;
import com.company.iotfortechnicians.common.util.StringUtils;

import java.util.Date;

import lombok.NoArgsConstructor;

import static com.company.iotfortechnicians.common.Constants.DEVICE_DATA_KEY;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setValueToTextView;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setViewVisibility;

@NoArgsConstructor
public class ShowOnMapFragment extends AbstractIOTMapFragment {

    private DeviceDTO deviceDTO;
    private int locationSelected = 0;

    public static ShowOnMapFragment newInstance(DeviceDTO deviceDTO) {
        ShowOnMapFragment fragment = new ShowOnMapFragment();
        Bundle args = new Bundle();
        args.putSerializable(DEVICE_DATA_KEY, deviceDTO);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deviceDTO = (DeviceDTO) getArguments().getSerializable(DEVICE_DATA_KEY);
        }
    }

    private void mapSetup() {
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFrag != null) {
            mapFrag.getMapAsync(this);
        }
    }

    public String formatStringTemplate(int p, String... values) {
        String staticLocationTemplate = getString(p);
        return String.format(staticLocationTemplate, (Object[]) values);
    }

    private String getStaticLocationText() {
        return PropsUtils.getStaticLocationText(deviceDTO.getProperties());
    }

    private String getDynamicLocationText() {
        LocationDTO dynamicLocation = deviceDTO.getDynamicLocation();
        if (dynamicLocation == null)
            return null;
        else
            return dynamicLocation.toString();
    }

    private String getLocationText() {
        String staticLocationText = getStaticLocationText();
        String dynamicLocationText = getDynamicLocationText();

        if (areDynamicAndStaticLocationsBoth())
            return null;

        if (staticLocationText != null)
            return formatStringTemplate(R.string.static_location_template, staticLocationText);
        else if (dynamicLocationText != null)
            return formatStringTemplate(R.string.dynamic_location_template, dynamicLocationText);
        else
            return null;
    }

    private boolean areDynamicAndStaticLocationsBoth() {
        String staticLocationText = getStaticLocationText();
        String dynamicLocationText = getDynamicLocationText();

        return staticLocationText != null && dynamicLocationText != null;
    }

    private boolean isOneLocationOnly() {
        if (!isLocationSet())
            return false;

        return !areDynamicAndStaticLocationsBoth();
    }

    private String getLastCommunicationText() {
        InterfaceDTO interfaceDTO = DeviceUtils.getInterfaceDTO(deviceDTO);
        Date localDate = DateUtils.parseToLocalDate(interfaceDTO.getLastContact());
        if (localDate != null) {
            String date = DateUtils.formatAsAppDate(localDate);
            return formatStringTemplate(R.string.last_communication_date_template, date);
        } else
            return null;
    }

    private boolean hasLastCommunication() {
        if (!DeviceUtils.hasInterfaces(deviceDTO))
            return false;

        InterfaceDTO interfaceDTO = DeviceUtils.getInterfaceDTO(deviceDTO);

        String lastContact = interfaceDTO.getLastContact();
        if (!StringUtils.isNoneEmpty(lastContact))
            return false;

        Date localDate = DateUtils.parseToLocalDate(lastContact);
        return localDate != null;
    }

    private void displayDeviceInformation(View inflatedView) {
        setValueToTextView(inflatedView, R.id.deviceNameTextView, deviceDTO.getName());

        boolean lastComm = hasLastCommunication();
        setViewVisibility(inflatedView, R.id.lastCommDate, lastComm);

        if (lastComm) {
            String lastCommunicationText = getLastCommunicationText();
            setValueToTextView(inflatedView, R.id.lastCommDate, lastCommunicationText);
        }

        setViewVisibility(inflatedView, R.id.locationTextView, isOneLocationOnly());

        if (isOneLocationOnly()) {
            String locautonText = getLocationText();
            setValueToTextView(inflatedView, R.id.locationTextView, locautonText);
        }

        View goToDevice = inflatedView.findViewById(R.id.go_to_device_status);
        goToDevice.setOnClickListener(v -> goToDeviceStatus());
    }

    public void goToDeviceStatus() {
        getActivity().finish();
    }

    private void setButtonActive(View inflatedView, int btnId, boolean active) {
        Drawable background = getResources().getDrawable(
                active
                        ? R.drawable.rounded_rectangle_active
                        : R.drawable.rounded_rectangle_inactive,
                null
        );

        View staticBtn = inflatedView.findViewById(btnId);
        int pL = staticBtn.getPaddingLeft();
        int pT = staticBtn.getPaddingTop();
        int pR = staticBtn.getPaddingRight();
        int pB = staticBtn.getPaddingBottom();
        staticBtn.setBackground(background);
        staticBtn.setPadding(pL, pT, pR, pB);
    }

    private void updateButtonsSelection(View inflatedView) {
        if (locationSelected == 0) {
            setButtonActive(inflatedView, R.id.staticLocationBtn, true);
            setButtonActive(inflatedView, R.id.dynamicLocationBtn, false);
        } else if (locationSelected == 1) {
            setButtonActive(inflatedView, R.id.staticLocationBtn, false);
            setButtonActive(inflatedView, R.id.dynamicLocationBtn, true);
        }
    }

    private void displayLocations(View inflatedView) {
        setViewVisibility(inflatedView, R.id.lcationDivider, areDynamicAndStaticLocationsBoth());
        setViewVisibility(inflatedView, R.id.lcationSelector, areDynamicAndStaticLocationsBoth());

        if (areDynamicAndStaticLocationsBoth()) {
            View staticLocationBtn = inflatedView.findViewById(R.id.staticLocationBtn);
            View dynamicLocationBtn = inflatedView.findViewById(R.id.dynamicLocationBtn);

            staticLocationBtn.setOnClickListener(v -> {
                selectLocation(inflatedView, 0);
            });
            dynamicLocationBtn.setOnClickListener(v ->{
                selectLocation(inflatedView, 1);
            });

            String staticLocationText = formatStringTemplate(R.string.static_location_short_template, getStaticLocationText());
            String dynamicLocationText = formatStringTemplate(R.string.dynamic_location_short_template, getDynamicLocationText());

            setValueToTextView(inflatedView, R.id.staticLocationText, staticLocationText);
            setValueToTextView(inflatedView, R.id.dynamicLocationText, dynamicLocationText);

            updateButtonsSelection(inflatedView);
        }
    }

    private void selectLocation(View inflatedView, int selected) {
        locationSelected = selected;
        updateButtonsSelection(inflatedView);

        LatLng selectedLocation = getSelectedLatLng();
        moveCamera(selectedLocation);
        getMarkers().get(0).setPosition(selectedLocation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_show_on_map, container, false);

        displayDeviceInformation(inflatedView);

        displayLocations(inflatedView);

        mapSetup();

        return inflatedView;
    }

    private LatLng getStaticLatLng() {
        LocationDTO staticLocation = PropsUtils.getStaticLocationFromProps(deviceDTO.getProperties());

        if (staticLocation != null)
            return new LatLng(staticLocation.getLat(), staticLocation.getLon());
        else
            return null;
    }

    private LatLng getDynamicLatLng() {
        LocationDTO dynamicLocation = deviceDTO.getDynamicLocation();

        if (dynamicLocation != null)
            return new LatLng(dynamicLocation.getLat(), dynamicLocation.getLon());
        else
            return null;
    }

    private LatLng getSelectedLatLng() {
        LatLng staticLatLng = getStaticLatLng();
        LatLng dynamicLatLng = getDynamicLatLng();

        if (areDynamicAndStaticLocationsBoth()) {
            if (locationSelected == 0) {
                return staticLatLng;
            } else if (locationSelected == 1) {
                return dynamicLatLng;
            }
        }

        if (staticLatLng != null)
            return staticLatLng;
        else
            return dynamicLatLng;
    }

    @Override
    public void showDataOnMap() {
        clearMap();

        if (isLocationSet()) {
            LatLng selectedLocation = getSelectedLatLng();
            addMarker(selectedLocation, deviceDTO.getName());
            moveCamera(selectedLocation);
        }
    }

    private boolean isLocationSet() {
        String staticLocationText = getStaticLocationText();
        String dynamicLocationText = getDynamicLocationText();

        return staticLocationText != null || dynamicLocationText != null;
    }
}

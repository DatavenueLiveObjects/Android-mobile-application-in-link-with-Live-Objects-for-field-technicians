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

package com.company.iotfortechnicians.geotagmydevice;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.EnableGpsDialog;
import com.company.iotfortechnicians.common.LocationAvailability;
import com.company.iotfortechnicians.common.LocationAvailabilityImpl;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.LocationDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.PatchPropertiesRequestDTO;
import com.company.iotfortechnicians.common.service.DeviceManagementService;
import com.company.iotfortechnicians.common.util.PermissionUtils;
import com.company.iotfortechnicians.common.util.PropsUtils;

import java.util.Map;

import lombok.NoArgsConstructor;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.company.iotfortechnicians.common.Constants.DEVICE_DATA_KEY;
import static com.company.iotfortechnicians.common.Constants.INDOOR_LOCATION_KEY;
import static com.company.iotfortechnicians.common.Constants.LOCATION_PERMISSION_REQUEST_CODE;
import static com.company.iotfortechnicians.common.Constants.OUTDOOR_LOCATION_KEY;
import static com.company.iotfortechnicians.common.util.FragmentUtils.getValueFromTextView;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setValueToTextView;
import static com.company.iotfortechnicians.common.util.IndoorProps.BUILDING;
import static com.company.iotfortechnicians.common.util.IndoorProps.FLOOR;
import static com.company.iotfortechnicians.common.util.IndoorProps.ROOM;
import static com.company.iotfortechnicians.common.util.PropsUtils.retrieveValueFrom;

@NoArgsConstructor
public class GeotagFragment extends AbstractIOTApiCallbackFragment<DeviceDTO> implements LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private DeviceDTO deviceDTO;
    private View inflatedView;

    public static GeotagFragment newInstance(DeviceDTO deviceDTO) {
        GeotagFragment fragment = new GeotagFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_geotag_my_device, container, false);

        displayDataFromProps();

        setValueToTextView(inflatedView, R.id.device_status_name, deviceDTO.getName());
        setValueToTextView(inflatedView, R.id.device_status_id, deviceDTO.getId());

        Button saveProps = inflatedView.findViewById(R.id.save_props_btn);
        saveProps.setOnClickListener(v -> executePropsPatch());

        Button position = inflatedView.findViewById(R.id.position);
        position.setOnClickListener(v -> enableMyLocation());

        return inflatedView;
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LocationAvailability locationAvailability = new LocationAvailabilityImpl(getContext());
            if (locationAvailability.isLocationDisabled()) {
                EnableGpsDialog.newInstance(GeotagFragment.this);
            }
            // Access to the location has been granted to the app.
            Criteria criteria = getCriteria();
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                locationManager.requestSingleUpdate(criteria, this, null);
            }
        }
    }

    @NonNull
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setSpeedRequired(false);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        return criteria;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            PermissionUtils.showRequestPermissionRationaleDialogs(this, requestCode, permissions);
        }
    }

    private void executePropsPatch() {
        DeviceManagementService patchPropsService = ApiClient.createApiService(DeviceManagementService.class);
        Map<String, String> props = deviceDTO.getProperties();
        props.put(INDOOR_LOCATION_KEY, getIndoorLocation());
        addOutdoorLocation(props);
        patchPropsService.patchProperties(deviceDTO.getId(), new PatchPropertiesRequestDTO(props)).enqueue(this);
    }

    @NonNull
    private String getIndoorLocation() {
        return "appAndroid;" +
                getValueFromTextView(inflatedView, R.id.geotag_indoor_building) +
                ";" +
                getValueFromTextView(inflatedView, R.id.geotag_indoor_floor) +
                ";" +
                getValueFromTextView(inflatedView, R.id.geotag_indoor_room);
    }

    private void addOutdoorLocation(Map<String, String> props) {
        String lat = getValueFromTextView(inflatedView, R.id.geotag_outdoor_lat);
        String lon = getValueFromTextView(inflatedView, R.id.geotag_outdoor_lon);
        String alt = getValueFromTextView(inflatedView, R.id.geotag_outdoor_alt);
        if (allLocationCoordinatesAreDoubles(lat, lon, alt)) {
            props.put(OUTDOOR_LOCATION_KEY, "appAndroid;" + lat + ";" + lon + ";" + alt);
        }
    }

    private boolean allLocationCoordinatesAreDoubles(String... args) {
        try {
            for (String a : args)
                Double.parseDouble(a);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void displayDataFromProps() {
        LocationDTO outdoorStaticLocation = PropsUtils.getStaticLocationFromProps(deviceDTO.getProperties());
        if (outdoorStaticLocation != null) {
            setValueToTextView(inflatedView, R.id.geotag_outdoor_lat, outdoorStaticLocation.getLat());
            setValueToTextView(inflatedView, R.id.geotag_outdoor_lon, outdoorStaticLocation.getLon());
            setValueToTextView(inflatedView, R.id.geotag_outdoor_alt, outdoorStaticLocation.getAlt());
        } else {
            setValueToTextView(inflatedView, R.id.geotag_outdoor_lat, getString(R.string.no_value));
            setValueToTextView(inflatedView, R.id.geotag_outdoor_lon, getString(R.string.no_value));
            setValueToTextView(inflatedView, R.id.geotag_outdoor_alt, getString(R.string.no_value));
        }

        String indoorProp = deviceDTO.getProperties().get(INDOOR_LOCATION_KEY);
        String building = retrieveValueFrom(indoorProp, BUILDING);
        String floor = retrieveValueFrom(indoorProp, FLOOR);
        String room = retrieveValueFrom(indoorProp, ROOM);

        setValueToTextView(inflatedView, R.id.geotag_indoor_building, building);
        setValueToTextView(inflatedView, R.id.geotag_indoor_floor, floor);
        setValueToTextView(inflatedView, R.id.geotag_indoor_room, room);
    }

    @Override
    protected void processData(DeviceDTO data) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, R.string.updated_static_prop_info, Toast.LENGTH_LONG).show();
            Intent goBackIntent = new Intent();
            goBackIntent.putExtra(DEVICE_DATA_KEY, data);
            activity.setResult(Activity.RESULT_OK, goBackIntent);
            activity.finish();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        setValueToTextView(inflatedView, R.id.geotag_outdoor_lat, location.getLatitude());
        setValueToTextView(inflatedView, R.id.geotag_outdoor_lon, location.getLongitude());
        setValueToTextView(inflatedView, R.id.geotag_outdoor_alt, location.getAltitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //Not needed
    }

    @Override
    public void onProviderEnabled(String s) {
        //Not needed
    }

    @Override
    public void onProviderDisabled(String s) {
        //Not needed
    }

}

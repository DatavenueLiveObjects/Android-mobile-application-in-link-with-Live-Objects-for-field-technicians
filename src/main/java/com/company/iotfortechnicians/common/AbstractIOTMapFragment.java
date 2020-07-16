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

package com.company.iotfortechnicians.common;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.company.iotfortechnicians.common.util.PermissionUtils;

import java.util.LinkedList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.company.iotfortechnicians.common.Constants.LOCATION_PERMISSION_REQUEST_CODE;

public abstract class AbstractIOTMapFragment extends AbstractIOTFragment implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {

    private GoogleMap mMap;

    private List<Marker> markers = new LinkedList<>();

    protected abstract void showDataOnMap();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            enableMyLocation();
            showDataOnMap();
        } catch (Exception e) {
          Log.e(AbstractIOTMapFragment.class.getSimpleName(), e.getMessage());
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            LocationAvailability locationAvailability = new LocationAvailabilityImpl(getContext());
            if (locationAvailability.isLocationDisabled()) {
                EnableGpsDialog.newInstance(AbstractIOTMapFragment.this);
            }

            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
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
        criteria.setAltitudeRequired(false);
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

    protected void addMarker(LatLng latLng, String markerTitle) {
        MarkerOptions title = new MarkerOptions()
                .position(latLng)
                .title(markerTitle);
        markers.add(mMap.addMarker(title));
    }

    protected void moveCamera(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(Constants.CAMERA_ZOOM)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
    }

    public List<Marker> getMarkers() {
        return markers;
    }

    protected void clearMap() {
        mMap.clear();
    }

    @Override
    public void onLocationChanged(Location location) {
        //Not needed
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

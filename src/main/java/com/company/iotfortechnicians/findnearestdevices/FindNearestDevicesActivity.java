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

package com.company.iotfortechnicians.findnearestdevices;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTLocationActivity;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.devicemanagement.LocationDTO;
import com.company.iotfortechnicians.common.dto.search.SourceDTO;
import com.company.iotfortechnicians.common.service.DataSearchService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindNearestDevicesActivity extends AbstractIOTLocationActivity implements Callback<List<SourceDTO>> {

    private static final String TAG = "FindNearestDevices";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_nearest_devices_layout);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);

        Button buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(view -> {
            mMap.clear();
            Projection projection = mMap.getProjection();
            VisibleRegion visibleRegion = projection.getVisibleRegion();

            DataSearchService findDeviceService = ApiClient.createApiService(DataSearchService.class);
            String b = "{\"size\":2000,\"query\":{\"bool\":{\"must\":[{\"range\":{\"location.lat\":{\"gte\":" + visibleRegion.nearLeft.latitude + ",\"lte\":" + visibleRegion.farLeft.latitude + "}}},{\"range\":{\"location.lon\":{\"gte\":" + visibleRegion.farLeft.longitude + ",\"lte\":" + visibleRegion.farRight.longitude + "}}}]}}}";
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), b);
            findDeviceService.searchHits(body).enqueue(FindNearestDevicesActivity.this);
        });
    }


    @Override
    public void onResponse(Call<List<SourceDTO>> call, Response<List<SourceDTO>> response) {
        List<SourceDTO> body = response.body();
        if (body != null) {
            if (body.isEmpty()) {
                showNoDataFoundToast();
            }
            for (SourceDTO device : body) {
                LocationDTO location = device.getLocation();
                MarkerOptions title = new MarkerOptions()
                        .position(new LatLng(location.getLat(), location.getLon()))
                        .title(device.getId());
                mMap.addMarker(title);
            }
            Log.i(TAG, String.valueOf(body.size()));
        } else {
            showUnableToGetDataToast();
        }
    }

    @Override
    public void onFailure(Call<List<SourceDTO>> call, Throwable t) {
        Log.e(TAG, t.getMessage(), t);
        showUnableToGetDataToast();
    }

    private void showNoDataFoundToast() {
        showToast(R.string.no_data_found);
    }

    private void showUnableToGetDataToast() {
        showToast(R.string.unable_to_get_data);
    }

    private void showToast(int resId) {
        Toast toast = Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG);
        toast.show();
    }
}

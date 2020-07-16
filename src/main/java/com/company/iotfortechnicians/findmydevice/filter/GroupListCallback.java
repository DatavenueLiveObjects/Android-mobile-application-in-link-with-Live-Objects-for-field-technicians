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

package com.company.iotfortechnicians.findmydevice.filter;

import android.content.Context;

import com.company.iotfortechnicians.IOTApplication;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.dto.devicemanagement.GroupDTO;

import java.util.List;

import lombok.RequiredArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiredArgsConstructor
public class GroupListCallback implements Callback<List<GroupDTO>> {

    private final FilterFragment fragment;

    @Override
    public void onResponse(Call<List<GroupDTO>> call, Response<List<GroupDTO>> response) {
        if (response.isSuccessful()) {
            List<GroupDTO> groups = getGroupsWithRootGroupName(response);
            fragment.onGroupCallbackResponse(groups);
        } else {
            fragment.onResponseError(response.code(), response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<List<GroupDTO>> call, Throwable t) {
        fragment.onFailure(t);
    }

    private String getRootGroupName() {
        Context appContext = IOTApplication.getAppContext();
        return appContext.getString(R.string.root_group_name);
    }

    private List<GroupDTO> getGroupsWithRootGroupName(Response<List<GroupDTO>> response) {
        String rootGroupName = getRootGroupName();
        List<GroupDTO> groups = response.body();
        if (groups != null) {
            GroupDTO groupDTO = groups.get(0);
            groupDTO.setPathNode(rootGroupName);
        }
        return groups;
    }
}

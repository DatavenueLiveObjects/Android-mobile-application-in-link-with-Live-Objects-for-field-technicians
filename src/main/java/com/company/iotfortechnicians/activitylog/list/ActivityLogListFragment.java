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

package com.company.iotfortechnicians.activitylog.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.activitylog.details.ActivityLogDetailsActivity;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.auditlog.AuditLogMessageDTO;
import com.company.iotfortechnicians.common.service.AuditLogService;
import com.company.iotfortechnicians.common.util.FragmentUtils;

import java.util.HashMap;
import java.util.List;

import static com.company.iotfortechnicians.common.Constants.LIMIT;
import static com.company.iotfortechnicians.common.Constants.LIMIT_VALUE;
import static com.company.iotfortechnicians.common.Constants.NODE_ID_KEY;
import static com.company.iotfortechnicians.common.Constants.OFFSET;

public class ActivityLogListFragment extends AbstractIOTApiCallbackFragment<List<AuditLogMessageDTO>> {

    private String nodeId;
    private View inflatedView;
    private ActivityLogListRecyclerViewAdapter recyclerViewAdapter;

    public static ActivityLogListFragment newInstance(String nodeId) {
        ActivityLogListFragment fragment = new ActivityLogListFragment();
        Bundle args = new Bundle();
        args.putSerializable(NODE_ID_KEY, nodeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nodeId = getArguments().getString(NODE_ID_KEY);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        requestGetAuditLogs();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_activity_log_list, container, false);
        RecyclerView recyclerView = inflatedView.findViewById(R.id.results_list);
        prepareRecyclerView(recyclerView);
        return inflatedView;
    }

    private void prepareRecyclerView(RecyclerView recyclerView) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new ActivityLogListRecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addOnScrollListener(getRecyclerViewOnScrollListener(layoutManager));
    }

    @NonNull
    private RecyclerView.OnScrollListener getRecyclerViewOnScrollListener(final LinearLayoutManager layout) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerViewAdapter.isLoading() && !recyclerViewAdapter.hasAllItemsLoaded() && layout.findLastVisibleItemPosition() == recyclerViewAdapter.getItemCount() - 1) {
                    recyclerViewAdapter.setLoading(true);
                    showLoadingDialog();
                    executeGetAuditLogsRequest(recyclerViewAdapter.getItemCount());
                }
            }
        };
    }

    private void requestGetAuditLogs() {
        recyclerViewAdapter.clear();
        recyclerViewAdapter.setLoading(true);
        executeGetAuditLogsRequest(Constants.OFFSET_VALUE);
    }

    private void executeGetAuditLogsRequest(int offsetValue) {

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put(LIMIT, String.valueOf(LIMIT_VALUE));
        paramsMap.put(OFFSET, String.valueOf(offsetValue));
        paramsMap.put("source.nodeId", nodeId);

        showLoadingDialog();
        AuditLogService auditLogService = ApiClient.createApiService(AuditLogService.class);
        auditLogService.getMessages(paramsMap).enqueue(this);
    }

    @Override
    protected void processData(List<AuditLogMessageDTO> body) {
        recyclerViewAdapter.setLoading(false);
        hideLoadingDialog();
        if (!body.isEmpty()) {
            recyclerViewAdapter.addAll(body, getXTotalCount());
        } else {
            showNoMessageInfo();
        }
    }

    private void showNoMessageInfo() {
        FragmentUtils.setViewVisibility(inflatedView, R.id.no_activity_log, true);
    }

    void onAuditLogSelected(AuditLogMessageDTO auditLogMessageDTO) {
        Intent intent = new Intent(getActivity(), ActivityLogDetailsActivity.class);
        intent.putExtra(Constants.AUDIT_LOG_MESSAGE_KEY, auditLogMessageDTO);
        startActivity(intent);
    }
}

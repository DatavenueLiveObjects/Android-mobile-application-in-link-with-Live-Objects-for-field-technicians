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

package com.company.iotfortechnicians.activitylog.details;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.dto.auditlog.AuditLogMessageDTO;
import com.company.iotfortechnicians.common.util.DeviceUtils;
import com.company.iotfortechnicians.common.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.company.iotfortechnicians.common.Constants.AUDIT_LOG_MESSAGE_KEY;
import static com.company.iotfortechnicians.common.util.FragmentUtils.setViewVisibility;

public class ActivityLogDetailsFragment extends AbstractIOTFragment {

    private static final String SIGNAL = "signal";
    private static final String SIGNAL_LEVEL = "signallevel";
    private static final String DEF_TYPE_STRING = "string";
    private AuditLogMessageDTO logMessageDTO;
    private Map<String, String> connectivityInformation;
    private Map<String, String> messageInformation;

    public ActivityLogDetailsFragment() {
        connectivityInformation = new LinkedHashMap<>();
        messageInformation = new LinkedHashMap<>();
    }

    public static ActivityLogDetailsFragment newInstance(AuditLogMessageDTO logMessageDTO) {
        ActivityLogDetailsFragment fragment = new ActivityLogDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(AUDIT_LOG_MESSAGE_KEY, logMessageDTO);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logMessageDTO = (AuditLogMessageDTO) getSerializable(AUDIT_LOG_MESSAGE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedLayout = inflater.inflate(R.layout.fragment_activity_log_details, container, false);
        messageInformation.put(Constants.TIMESTAMP, logMessageDTO.getTimestamp());
        messageInformation.put(Constants.CREATED, logMessageDTO.getCreated());
        messageInformation.put(Constants.TYPE, logMessageDTO.getType());
        parseMessageContent();
        initMessageTable(inflatedLayout);
        initConnectivityTable(inflatedLayout);

        return inflatedLayout;
    }

    private void parseMessageContent() {
        JsonParser jp = new JsonParser();
        JsonElement parse = jp.parse(logMessageDTO.getContent());
        processJsonObject(null, parse.getAsJsonObject());
    }

    private void initMessageTable(View inflatedLayout) {
        TableLayout messageTable = inflatedLayout.findViewById(R.id.message_table);
        initTable(messageTable, messageInformation);
    }

    private void initConnectivityTable(View inflatedLayout) {
        TableLayout connectivityTable = inflatedLayout.findViewById(R.id.connectivity_table);
        initTable(connectivityTable, connectivityInformation);
    }

    private void initTable(TableLayout table, Map<String, String> rows) {
        LayoutInflater layoutInflater = getLayoutInflater();

        for (Map.Entry<String, String> e : rows.entrySet()) {
            final TableRow tableRow = getTableRow(layoutInflater, e);
            table.addView(tableRow);
        }
    }

    private TableRow getTableRow(LayoutInflater layoutInflater, Map.Entry<String, String> e) {
        TableRow tableRow = (TableRow) layoutInflater.inflate(R.layout.activity_log_row_item, null);
        showLabelView(e, tableRow);
        showValueView(e, tableRow);
        return tableRow;
    }

    private void showValueView(Map.Entry<String, String> e, TableRow tableRow) {
        String value = StringUtils.removeQuotes(e.getValue());
        if (isSignalLevelRow(e)) {
            showSignalLevelView(tableRow, value);
        } else {
            showNormalValueView(tableRow, value);
        }
    }

    private boolean isSignalLevelRow(Map.Entry<String, String> e) {
        return SIGNAL_LEVEL.equalsIgnoreCase(e.getKey());
    }

    private void showNormalValueView(TableRow tableRow, String value) {
        TextView valueView = tableRow.findViewById(R.id.value);
        String text = getText(value, getContext());
        valueView.setText(text);
    }

    private void showSignalLevelView(TableRow tableRow, String value) {
        int signalLevelImage = DeviceUtils.getSignalLevelImage(Integer.parseInt(value));
        ImageView signalValueView = tableRow.findViewById(R.id.signal_value);
        signalValueView.setImageResource(signalLevelImage);

        setViewVisibility(tableRow, R.id.value, false);
        setViewVisibility(tableRow, R.id.wrapper, true);
    }

    private void showLabelView(Map.Entry<String, String> e, TableRow tableRow) {
        String label = StringUtils.capitalizeFirstLetter(e.getKey());
        TextView labelView = tableRow.findViewById(R.id.label);
        String text = getText(label, getContext());
        labelView.setText(text);
    }

    private String getText(String text, Context context) {
        try {
            Resources resources = getResources();
            int identifier = resources.getIdentifier(text, DEF_TYPE_STRING, context.getPackageName());
            return resources.getString(identifier);
        } catch (Resources.NotFoundException e) {
            return text;
        }
    }

    private void processJsonObject(String key, JsonObject o) {
        Map<String, String> mapToStore = getMapToStore(key);
        Set<Map.Entry<String, JsonElement>> members = o.entrySet();
        for (Map.Entry<String, JsonElement> e : members) {
            processJsonElement(mapToStore, e.getKey(), e.getValue());
        }
    }

    private Map<String, String> getMapToStore(String key) {
        return SIGNAL.equalsIgnoreCase(key) ? connectivityInformation : messageInformation;
    }

    private void processJsonElement(Map<String, String> mapToStore, String key, JsonElement e) {
        if (e.isJsonArray()) {
            processJsonArray(mapToStore, key, e.getAsJsonArray());
        } else if (e.isJsonNull()) {
            processJsonNull(mapToStore, key, e.getAsJsonNull());
        } else if (e.isJsonObject()) {
            processJsonObject(key, e.getAsJsonObject());
        } else if (e.isJsonPrimitive()) {
            processJsonPrimitive(mapToStore, key, e.getAsJsonPrimitive());
        }
    }

    private void processJsonArray(Map<String, String> mapToStore, String key, JsonArray a) {
        for (JsonElement e : a) {
            processJsonElement(mapToStore, key, e);
        }
    }

    private void processJsonNull(Map<String, String> mapToStore, String key, JsonNull n) {
        mapToStore.put(key, String.valueOf(n));
    }

    private void processJsonPrimitive(Map<String, String> mapToStore, String key, JsonPrimitive p) {
        mapToStore.put(key, String.valueOf(p));
    }
}

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


import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.company.iotfortechnicians.common.AbstractIOTAppBarActivity;
import com.company.iotfortechnicians.common.dto.auditlog.AuditLogMessageDTO;

import static com.company.iotfortechnicians.common.Constants.AUDIT_LOG_MESSAGE_KEY;

public class ActivityLogDetailsActivity extends AbstractIOTAppBarActivity {

    @Override
    protected Fragment getFragment() {
        Intent intent = getIntent();
        AuditLogMessageDTO auditLogMessageDTO = (AuditLogMessageDTO) intent.getSerializableExtra(AUDIT_LOG_MESSAGE_KEY);
        return ActivityLogDetailsFragment.newInstance(auditLogMessageDTO);
    }
}

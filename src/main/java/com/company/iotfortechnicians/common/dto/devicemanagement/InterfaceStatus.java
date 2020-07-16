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

package com.company.iotfortechnicians.common.dto.devicemanagement;

import com.company.iotfortechnicians.common.util.StringUtils;

import lombok.Getter;

@Getter
public enum InterfaceStatus {
    REGISTERED("REGISTERED"),
    INITIALIZING("INITIALIZING"),
    INITIALIZED("INITIALIZED"),
    ONLINE("ONLINE"),
    OFFLINE("OFFLINE"),
    ACTIVATED("ACTIVATED"),
    REACTIVATED("REACTIVATED"),
    DEACTIVATED("DEACTIVATED"),
    CONNECTIVITY_ERROR("CONNECTIVITY_ERROR"),
    DELETED("DELETED");

    private String statusLabel;

    InterfaceStatus(String status) {
        this.statusLabel = status;
    }

    public String getCapitalizedLabel() {
        return StringUtils.capitalize(statusLabel);
    }

}

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

package com.company.iotfortechnicians.common.dto.auditlog;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuditLogMessageDTO implements Serializable {

    private static final long serialVersionUID = 5995262939758257446L;

    private String timestamp;
    private String level;
    private String type;
    private String description;
    private String detailedDescription;
    private String content;
    private String created;
}

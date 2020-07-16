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

import com.company.iotfortechnicians.common.util.StringUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Session implements Serializable {

    private static final long serialVersionUID = -7948374183278936259L;

    private String userId;
    @Setter
    private String login;
    @Setter
    private String email;
    private String tenantId;
    private final String sessionKey;
    private long timestamp;

    public Session(String sessionKey) {
        this(sessionKey, null, null, null);
    }

    public Session(String sessionKey, String login) {
        this(sessionKey, login, null, null);
    }

    public Session(String sessionKey, String login, String userId, String tenantId) {
        this(sessionKey, login, userId, tenantId, new Date());
    }

    public Session(String sessionKey, String login, String userId, String tenantId, Date timestamp) {
        this.sessionKey = sessionKey;
        this.login = login;
        this.userId = userId;
        this.tenantId = tenantId;
        this.timestamp = timestamp.getTime();
    }

    boolean isExpired() {
        if (timestamp == 0)
            return true;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date expiredBefore = calendar.getTime();

        return (new Date(timestamp)).before(expiredBefore);
    }

    public boolean isAuthenticated() {
        if (isExpired())
            return false;

        return StringUtils.isNoneEmpty(getSessionKey());
    }
}

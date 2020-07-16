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

package com.company.iotfortechnicians;

import android.app.Application;
import android.content.Context;

import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.Session;
import com.company.iotfortechnicians.common.util.SharedPreferencesUtils;
import com.company.iotfortechnicians.common.util.StringUtils;

public class IOTApplication extends Application {

    private static Application application;
    private static Session session;

    private static Application getApplication() {
        return application;
    }

    public static Context getAppContext() {
        return getApplication().getApplicationContext();
    }

    private static void setApplication(Application application) {
        IOTApplication.application = application;
    }

    public static Session getSession() {
        return session;
    }

    public static void setSession(Session session) {
        IOTApplication.session = session;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setApplication(this);
        restoreSession();
    }

    private void restoreSession() {
        Session restoredSession = SharedPreferencesUtils.getObject(getAppContext(), Constants.SESSION_KEY, Session.class);
        Session sessionToStore = restoredSession != null ? restoredSession : new Session(StringUtils.EMPTY);
        setSession(sessionToStore);
    }

    public static void storeSession() {
        SharedPreferencesUtils.putObject(getAppContext(), Constants.SESSION_KEY, session);
    }


    public static void clearSession() {
        IOTApplication.setSession(new Session(StringUtils.EMPTY, session.getLogin()));
        IOTApplication.storeSession();
    }
}
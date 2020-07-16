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

package com.company.iotfortechnicians.common.api;

import androidx.annotation.NonNull;

import com.company.iotfortechnicians.IOTApplication;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.Session;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class HeadersInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request build = getRequest(original);
        return chain.proceed(build);
    }

    @NonNull
    private Request getRequest(Request original) {
        Request.Builder request = original.newBuilder()
                .header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON)
                .header(Constants.ACCEPT, Constants.APPLICATION_JSON)
                .header(Constants.ACCEPT, Constants.APPLICATION_JSON)
                .header(Constants.X_TOTAL_COUNT, Boolean.TRUE.toString())
                .method(original.method(), original.body());

        Session session = IOTApplication.getSession();
        if (session.isAuthenticated()) {
            request.header(Constants.X_API_KEY, session.getSessionKey());
        }

        return request.build();
    }
}

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.company.iotfortechnicians.BuildConfig;
import com.company.iotfortechnicians.IOTApplication;
import com.company.iotfortechnicians.common.dto.auditlog.AuditLogMessageDTO;
import com.company.iotfortechnicians.common.dto.search.payload.HitDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.company.iotfortechnicians.common.Constants.LIVE_OBJECTS_API;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiClient {

    @Setter
    private static RetrofitInstance instance;

    private static RetrofitInstance getInstance() {
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(LIVE_OBJECTS_API)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .client(createOkHttpClient())
                    .build();
            instance = new RetrofitInstance(retrofit);
        }
        return instance;
    }

    @NonNull
    private static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(HitDTO.class, new PayloadHitDTODeserializer())
                .registerTypeAdapter(AuditLogMessageDTO.class, new AuditLogMessageDTODeserializer())
                .create();
    }

    @NonNull
    private static OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(new HeadersInterceptor())
                .addInterceptor(new ConnectivityInterceptor(IOTApplication.getAppContext()))
                .addInterceptor(interceptor)
                .build();
    }

    public static <T> T createApiService(final Class<T> service) {
        RetrofitInstance instance = getInstance();
        return instance.create(service);
    }

    private static HttpLoggingInterceptor.Level getLoggingLevel(){
        if ("release".equals(BuildConfig.BUILD_TYPE)) {
            return HttpLoggingInterceptor.Level.BASIC;
        } else {
            return HttpLoggingInterceptor.Level.BODY;
        }
    }

    @AllArgsConstructor
    public static class RetrofitInstance {
        private Retrofit instance;

        public <T> T create(final Class<T> service) {
            return instance.create(service);
        }
    }
}

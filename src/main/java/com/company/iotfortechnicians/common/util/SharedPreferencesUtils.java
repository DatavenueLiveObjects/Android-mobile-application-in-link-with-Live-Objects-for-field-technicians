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

package com.company.iotfortechnicians.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;

import androidx.preference.PreferenceManager;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.security.crypto.MasterKeys;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.security.GeneralSecurityException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SharedPreferencesUtils {

    private static SharedPreferences sharedPreferences = null;

    private static SharedPreferences getDefaultSharedPreferences(Context context){
        if(sharedPreferences == null) {
            try {
                MasterKey masterKeyAlias = new MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
                sharedPreferences = EncryptedSharedPreferences
                        .create(context, "iotSharedPreferences.txt",
                                masterKeyAlias,
                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        );
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sharedPreferences;
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences preferences = getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences preferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences preferences = getDefaultSharedPreferences(context);
        return preferences.getInt(key, defaultValue);
    }

    public static void putObject(Context context, String key, Object obj) {
        SharedPreferences preferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        String json = new Gson().toJson(obj);
        edit.putString(key, json);
        edit.apply();
    }

    public static <T> T getObject(Context context, String key, Class<T> classOfT) {
        SharedPreferences preferences = getDefaultSharedPreferences(context);
        String string = preferences.getString(key, StringUtils.EMPTY);
        try {
            return StringUtils.isNoneEmpty(string) ? new Gson().fromJson(string, classOfT) : null;
        } catch (JsonParseException e) {
            return null;
        }
    }
}

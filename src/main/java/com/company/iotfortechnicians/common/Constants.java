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

import java.util.TimeZone;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final int APP_ANDROID_FLAG_LENGTH = 11;
    private static final String LIVE_OBJECTS_SERVER = "https://liveobjects.orange-business.com";
    public static final String LIVE_OBJECTS_API = LIVE_OBJECTS_SERVER + "/api/";
    public static final long TIME_TO_CHANGE_LAST_COMMUNICATION_COLOR_TO_MEDIUM = 30L * 60 * 1000; // 30 minutes
    public static final long TIME_TO_CHANGE_LAST_COMMUNICATION_COLOR_TO_LOW = 3L * 24 * 60 * 60 * 1000; // 3 days
    public static final Float CAMERA_ZOOM = 14.0f;

    public static final String DEVICE_DATA_KEY = "DEVICE_DATA";
    public static final String DEVICE_NAME_KEY = "DEVICE_NAME";
    public static final String DEVICE_PROPERTIES_KEY = "DEVICE_PROPERTIES_KEY";
    public static final String DEVICE_TAGS_KEY = "DEVICE_TAGS_KEY";
    public static final String DEVICE_ID_KEY = "DEVICE_ID";
    public static final String NODE_ID_KEY = "NODE_ID_KEY";
    public static final String STREAM_ID_KEY = "STREAM_ID";
    public static final String SIGNAL_LEVEL_ID_KEY = "SIGNAL_LEVEL_ID";
    public static final String RELOAD_USERDATA_KEY = "RELOAD_USERDATA_KEY";
    public static final String USER_LOGIN_KEY = "USER_LOGIN_KEY";
    public static final String DEVICE_SEARCH_PARAMETERS_KEY = "DEVICE_SEARCH_PARAMETERS_KEY";
    public static final String AUDIT_LOG_MESSAGE_KEY = "AUDIT_LOG_MESSAGE_KEY";
    public static final String LIMIT = "limit";
    public static final int LIMIT_VALUE = 20;
    public static final String OFFSET = "offset";
    public static final String SORT = "sort";
    public static final int OFFSET_VALUE = 0;

    public static final String API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String API_SMS_DEVICE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String API_TIME_ZONE_ID = "Z";
    public static final TimeZone API_TIME_ZONE = TimeZone.getTimeZone(Constants.API_TIME_ZONE_ID);
    public static final String APP_DATE_FORMAT = "MM/dd/yyyy h:mm:ss a";

    public static final String LORA_CONNECTOR = "lora";
    public static final String SMS_CONNECTOR = "sms";
    public static final String MQTT_CONNECTOR = "mqtt";

    public static final double KEYBOARD_MAGIC_PARAM = 0.15;

    public static final String ACCEPTED_TERMS_KEY = "ACCEPTED_TERMS_KEY";
    public static final int ACCEPTED_TERMS_DEFAULT_VALUE = 1;

    public static final String INDOOR_LOCATION_KEY = "_indoor_source_building_floor_room";
    public static final String OUTDOOR_LOCATION_KEY = "_outdoor_source_alt_lat_lon";


    public static final String USER_AUTHENTICATION_EXCEPTION = "USER_AUTHENTICATION_EXCEPTION";
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 91;
    public static final String TENANT_ID = "tenantId";
    public static final int SPLASH_TIME = 2000;
    public static final int PAYLOAD_MESSAGES_COUNT = 10;
    public static final String SESSION_KEY = "Session";
    public static final String X_TOTAL_COUNT = "X-Total-Count";
    public static final String X_API_KEY = "X-API-KEY";
    public static final String ACCEPT = "Accept";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";

    public static final String TIMESTAMP = "timestamp";
    public static final String TYPE = "type";
    public static final String CREATED = "created";

    public static final String SLASH = "/";
    public static final String TAB = "    ";
    public static final String STAR = "*";
    public static final String GROUP_PATH_SUFFIX = "/*";
    public static final String ERROR = "error";
}

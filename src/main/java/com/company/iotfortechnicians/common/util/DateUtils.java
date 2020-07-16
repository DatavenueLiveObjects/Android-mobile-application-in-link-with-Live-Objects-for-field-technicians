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

import android.util.Log;

import com.company.iotfortechnicians.common.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    private static final String TAG = "DateUtils";

    public static String parseAndFormatDate(String dateString) {
        Date date = parseToLocalDate(dateString);
        return formatAsAppDate(date);
    }

    public static Date parseToLocalDate(String dateString) {
        try {
            Date parse = parseDateFomApiDateString(dateString);
            return convertTimeZone(parse, Constants.API_TIME_ZONE, TimeZone.getDefault());
        } catch (ParseException | NullPointerException e) {
            Log.e(TAG, "Date parsing problem");
        }
        return null;
    }

    public static String formatAsAppDate(Date date) {
        return formatDate(date, Constants.APP_DATE_FORMAT);
    }

    public static String formatAsApiDate(Date date) {
        return formatDate(date, Constants.API_DATE_FORMAT);
    }

    private static String formatDate(Date date, String dateFormat) {
        SimpleDateFormat dateParser = getSimpleDateFormat(dateFormat);
        return date != null ? dateParser.format(date) : StringUtils.EMPTY;
    }

    public static Date addMinutes(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes); //minus number would decrement the days
        return cal.getTime();
    }

    public static Long getTimeDifference(Date dateOne, Date dateTwo) {
        if (dateOne != null && dateTwo != null) {
            return dateTwo.getTime() - dateOne.getTime();
        } else {
            return null;
        }
    }

    public static Date getCurrentApiDate() {
        return convertTimeZone(new Date(), TimeZone.getDefault(), Constants.API_TIME_ZONE);
    }

    private static Date parseDateFomApiDateString(String dateString) throws ParseException {
        String dateFormat = dateString.contains(".") ? Constants.API_DATE_FORMAT : Constants.API_SMS_DEVICE_DATE_FORMAT;
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(dateFormat);
        return simpleDateFormat.parse(dateString);
    }

    private static SimpleDateFormat getSimpleDateFormat(String dateFormat) {
        return new SimpleDateFormat(dateFormat);
    }

    private static Date convertTimeZone(Date date, TimeZone fromTimeZone, TimeZone toTimeZone) {
        long fromTimeZoneOffset = getTimeZoneUTCAndDSTOffset(date, fromTimeZone);
        long toTimeZoneOffset = getTimeZoneUTCAndDSTOffset(date, toTimeZone);

        return new Date(date.getTime() + (toTimeZoneOffset - fromTimeZoneOffset));
    }

    private static long getTimeZoneUTCAndDSTOffset(Date date, TimeZone timeZone) {
        long timeZoneDSTOffset = 0;
        if (timeZone.inDaylightTime(date)) {
            timeZoneDSTOffset = timeZone.getDSTSavings();
        }

        return timeZone.getRawOffset() + timeZoneDSTOffset;
    }
}

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

import okhttp3.ResponseBody;

public interface OnFailure {
    void onFailure(Throwable t);

    void onResponseError(int code, ResponseBody responseBody);
}

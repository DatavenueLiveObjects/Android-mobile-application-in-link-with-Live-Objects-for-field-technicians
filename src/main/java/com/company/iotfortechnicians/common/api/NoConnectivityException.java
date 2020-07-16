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

import java.io.IOException;

public class NoConnectivityException extends IOException {

    private static final long serialVersionUID = 2213913658052216464L;

    @Override
    public String getMessage() {
        return "No connectivity exception";
    }
}

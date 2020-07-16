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

package com.company.iotfortechnicians.editproperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PropertyNotValidException extends Exception {

    private static final long serialVersionUID = 2213913611053316464L;

    private final String propertyValue;

    @Override
    public String getMessage() {
        return "Invalid name for property " + propertyValue;
    }

    public String getPropertyValue() {
        return propertyValue;
    }
}

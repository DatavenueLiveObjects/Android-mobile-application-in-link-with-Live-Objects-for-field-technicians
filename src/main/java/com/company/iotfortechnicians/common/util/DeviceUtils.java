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

import androidx.annotation.DrawableRes;

import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.InterfaceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.InterfaceStatus;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceUtils {

    public static InterfaceDTO getInterfaceDTO(List<InterfaceDTO> interfaceDTOList) {
        return hasInterfaces(interfaceDTOList) ? interfaceDTOList.get(0) : null;
    }

    public static InterfaceDTO getInterfaceDTO(DeviceDTO deviceDTO) {
        List<InterfaceDTO> interfaces = deviceDTO.getInterfaces();
        return getInterfaceDTO(interfaces);
    }

    public static boolean hasInterfaces(DeviceDTO deviceDTO) {
        List<InterfaceDTO> interfaces = deviceDTO.getInterfaces();
        return !interfaces.isEmpty() && interfaces.get(0) != null;
    }

    private static boolean hasInterfaces(List<InterfaceDTO> interfaceDTOList) {
        return !interfaceDTOList.isEmpty() && interfaceDTOList.get(0) != null;
    }

    public static boolean isLoraConnectivity(InterfaceDTO interfaceDTO) {
        return isConnectivityType(interfaceDTO, Constants.LORA_CONNECTOR);
    }

    public static boolean isSMSConnectivity(InterfaceDTO interfaceDTO) {
        return isConnectivityType(interfaceDTO, Constants.SMS_CONNECTOR);
    }

    public static boolean isMqttConnectivity(InterfaceDTO interfaceDTO) {
        return isConnectivityType(interfaceDTO, Constants.MQTT_CONNECTOR);
    }

    private static boolean isConnectivityType(InterfaceDTO interfaceDTO, String connectorType) {
        return interfaceDTO != null && connectorType.equalsIgnoreCase(interfaceDTO.getConnector());
    }

    public static Integer getLastSignalLevel(InterfaceDTO interfaceDTO) {
        return hasSignalLevelField(interfaceDTO) ? interfaceDTO.getActivity().getLastSignalLevel() : 0;
    }

    private static boolean hasSignalLevelField(InterfaceDTO interfaceDTO) {
        return interfaceDTO != null && interfaceDTO.getActivity() != null &&
                interfaceDTO.getActivity().getLastSignalLevel() != null;
    }

    @DrawableRes
    public static int getDeviceStatusImage(InterfaceStatus interfaceStatus) {
        switch (interfaceStatus) {
            case REGISTERED:
                return R.drawable.icons_status_green_empty;
            case INITIALIZING:
                return R.drawable.icons_status_green_dot_empty;
            case INITIALIZED:
                return R.drawable.icons_status_green_dot_full;
            case ACTIVATED:
            case ONLINE:
                return R.drawable.icons_status_green_full;
            case REACTIVATED:
                return R.drawable.icons_status_green_questionmark;
            case DELETED:
            case CONNECTIVITY_ERROR:
                return R.drawable.icons_status_red;
            case DEACTIVATED:
            case OFFLINE:
            default:
                return R.drawable.icons_status_gray;
        }
    }

    @DrawableRes
    public static int getSignalLevelImage(InterfaceDTO interfaceDTO) {
        Integer lastSignalLevel = getLastSignalLevel(interfaceDTO);
        return getSignalLevelImage(lastSignalLevel);
    }

    @DrawableRes
    public static int getSignalLevelImage(int signalLvl) {
        switch (signalLvl) {
            case 1:
                return R.drawable.signal_20;
            case 2:
                return R.drawable.signal_40;
            case 3:
                return R.drawable.signal_60;
            case 4:
                return R.drawable.signal_80;
            case 5:
                return R.drawable.signal_100;
            default:
                return R.drawable.signal_0;
        }
    }

}

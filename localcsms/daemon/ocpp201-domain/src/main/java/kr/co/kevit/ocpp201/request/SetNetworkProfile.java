/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.NetworkConnectionProfileType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class SetNetworkProfile {
    
    /**
     * required
     * "description": "Slot in which the configuration should be stored.\r\n",
     */
    private int configurationSlot;
    
    /**
     * required
     */
    private NetworkConnectionProfileType connectionData;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public int getConfigurationSlot() {
        return configurationSlot;
    }

    public void setConfigurationSlot(int configurationSlot) {
        this.configurationSlot = configurationSlot;
    }

    public NetworkConnectionProfileType getConnectionData() {
        return connectionData;
    }

    public void setConnectionData(NetworkConnectionProfileType connectionData) {
        this.connectionData = connectionData;
    }

}

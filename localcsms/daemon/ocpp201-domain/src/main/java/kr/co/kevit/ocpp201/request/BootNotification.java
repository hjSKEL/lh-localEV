/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.ChargingStationType;
import kr.co.kevit.ocpp201.enumtype.BootReasonEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class BootNotification {
    
    /**
     * required
     */
    private ChargingStationType chargingStation;
    
    /**
     * required
     */
    private BootReasonEnumType reason;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public ChargingStationType getChargingStation() {
        return chargingStation;
    }

    public void setChargingStation(ChargingStationType chargingStation) {
        this.chargingStation = chargingStation;
    }

    public BootReasonEnumType getReason() {
        return reason;
    }

    public void setReason(BootReasonEnumType reason) {
        this.reason = reason;
    }
}

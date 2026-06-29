/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.ChargingProfilePurposeEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class ClearChargingProfileType {
    
    private Integer evseId;
    
    private ChargingProfilePurposeEnumType chargingProfilePurpose;
    
    private Integer stackLevel;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Integer getEvseId() {
        return evseId;
    }

    public void setEvseId(Integer evseId) {
        this.evseId = evseId;
    }

    public ChargingProfilePurposeEnumType getChargingProfilePurpose() {
        return chargingProfilePurpose;
    }

    public void setChargingProfilePurpose(ChargingProfilePurposeEnumType chargingProfilePurpose) {
        this.chargingProfilePurpose = chargingProfilePurpose;
    }

    public Integer getStackLevel() {
        return stackLevel;
    }

    public void setStackLevel(Integer stackLevel) {
        this.stackLevel = stackLevel;
    }

}
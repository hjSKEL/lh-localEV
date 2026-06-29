/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.ChargingProfileType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class SetChargingProfile {
    
    /**
     * required
     */
    private int evseId;
    
    /**
     * required
     */
    private ChargingProfileType chargingProfile;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public int getEvseId() {
        return evseId;
    }

    public void setEvseId(int evseId) {
        this.evseId = evseId;
    }

    public ChargingProfileType getChargingProfile() {
        return chargingProfile;
    }

    public void setChargingProfile(ChargingProfileType chargingProfile) {
        this.chargingProfile = chargingProfile;
    }

}

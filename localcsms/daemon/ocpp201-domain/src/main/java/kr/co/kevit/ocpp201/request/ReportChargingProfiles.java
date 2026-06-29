/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.ChargingProfileType;
import kr.co.kevit.ocpp201.enumtype.ChargingLimitSourceEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class ReportChargingProfiles {
    
    /**
     * required
     */
    private int requestId;
    
    /**
     * required
     */
    private ChargingLimitSourceEnumType chargingLimitSource;
    
    /**
     * required
     * "minItems": 1
     */
    private List<ChargingProfileType> chargingProfile;
    
    private boolean tbc = false;
    
    /**
     * required
     */
    private int evseId;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public ChargingLimitSourceEnumType getChargingLimitSource() {
        return chargingLimitSource;
    }

    public void setChargingLimitSource(ChargingLimitSourceEnumType chargingLimitSource) {
        this.chargingLimitSource = chargingLimitSource;
    }

    public List<ChargingProfileType> getChargingProfile() {
        return chargingProfile;
    }

    public void setChargingProfile(List<ChargingProfileType> chargingProfile) {
        this.chargingProfile = chargingProfile;
    }

    public boolean isTbc() {
        return tbc;
    }

    public void setTbc(boolean tbc) {
        this.tbc = tbc;
    }

    public int getEvseId() {
        return evseId;
    }

    public void setEvseId(int evseId) {
        this.evseId = evseId;
    }

}

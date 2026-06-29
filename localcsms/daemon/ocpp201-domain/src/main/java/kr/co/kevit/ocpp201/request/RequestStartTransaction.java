/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.ChargingProfileType;
import kr.co.kevit.ocpp201.domain.IdTokenType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class RequestStartTransaction {
    
    private Integer evseId;
    
    private IdTokenType groupIdToken;
    
    /**
     * required
     */
    private IdTokenType idToken;
    
    /**
     * required
     */
    private int remoteStartId;
    
    private ChargingProfileType chargingProfile;

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

    public IdTokenType getGroupIdToken() {
        return groupIdToken;
    }

    public void setGroupIdToken(IdTokenType groupIdToken) {
        this.groupIdToken = groupIdToken;
    }

    public IdTokenType getIdToken() {
        return idToken;
    }

    public void setIdToken(IdTokenType idToken) {
        this.idToken = idToken;
    }

    public int getRemoteStartId() {
        return remoteStartId;
    }

    public void setRemoteStartId(int remoteStartId) {
        this.remoteStartId = remoteStartId;
    }

    public ChargingProfileType getChargingProfile() {
        return chargingProfile;
    }

    public void setChargingProfile(ChargingProfileType chargingProfile) {
        this.chargingProfile = chargingProfile;
    }
}

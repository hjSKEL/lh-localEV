/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.ChargingLimitSourceEnumType;
import kr.co.kevit.ocpp201.enumtype.ChargingProfilePurposeEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class ChargingProfileCriterionType {
    
    /**
     * 
     */
    private ChargingProfilePurposeEnumType chargingProfilePurpose;
    
    private Integer stackLevel;
    
    /**
     * "minItems": 1
     */
    private List<Integer> chargingProfileId;
    
    /**
     * "minItems": 1,"maxItems": 4
     */
    private List<ChargingLimitSourceEnumType> chargingLimitSource;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
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

    public List<Integer> getChargingProfileId() {
        return chargingProfileId;
    }

    public void setChargingProfileId(List<Integer> chargingProfileId) {
        this.chargingProfileId = chargingProfileId;
    }

    public List<ChargingLimitSourceEnumType> getChargingLimitSource() {
        return chargingLimitSource;
    }

    public void setChargingLimitSource(List<ChargingLimitSourceEnumType> chargingLimitSource) {
        this.chargingLimitSource = chargingLimitSource;
    }
    
}
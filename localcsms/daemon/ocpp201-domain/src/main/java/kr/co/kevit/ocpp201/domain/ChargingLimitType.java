/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.ChargingLimitSourceEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class ChargingLimitType {
    
    /**
     * required
     */
    private ChargingLimitSourceEnumType chargingLimitSource;
    
    /**
     * "type": "boolean"
     */
    private Boolean isGridCritical;

    /**
     * (2.1)
     */
    private Boolean isLocalGeneration;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public ChargingLimitSourceEnumType getChargingLimitSource() {
        return chargingLimitSource;
    }

    public void setChargingLimitSource(ChargingLimitSourceEnumType chargingLimitSource) {
        this.chargingLimitSource = chargingLimitSource;
    }

    public Boolean getIsGridCritical() {
        return isGridCritical;
    }

    public void setIsGridCritical(Boolean isGridCritical) {
        this.isGridCritical = isGridCritical;
    }

    public Boolean getIsLocalGeneration() {
        return isLocalGeneration;
    }

    public void setIsLocalGeneration(Boolean isLocalGeneration) {
        this.isLocalGeneration = isLocalGeneration;
    }

}

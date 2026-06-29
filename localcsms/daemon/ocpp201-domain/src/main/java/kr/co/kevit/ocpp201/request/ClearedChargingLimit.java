/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.ChargingLimitSourceEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class ClearedChargingLimit {
    
    /**
     * required
     */
    private ChargingLimitSourceEnumType chargingLimitSource;
    
    /**
     * 
     */
    private Integer evseId;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    /**
     * Get chargingLimitSource
     * @return chargingLimitSource
     */
    public ChargingLimitSourceEnumType getChargingLimitSource() {
        return chargingLimitSource;
    }

    /**
     * Set chargingLimitSource
     * @param chargingLimitSource
     */
    public void setChargingLimitSource(ChargingLimitSourceEnumType chargingLimitSource) {
        this.chargingLimitSource = chargingLimitSource;
    }

    /**
     * Get evseId
     * @return evseId
     */
    public Integer getEvseId() {
        return evseId;
    }

    /**
     * Set evseId
     * @param evseId
     */
    public void setEvseId(Integer evseId) {
        this.evseId = evseId;
    }
}

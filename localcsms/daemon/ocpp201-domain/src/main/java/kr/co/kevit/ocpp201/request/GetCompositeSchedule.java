/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.ChargingRateUnitEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class GetCompositeSchedule {
    
    /**
     * required
     */
    private Integer duration;
    
    private ChargingRateUnitEnumType chargingRateUnit;
    
    /**
     * required
     */
    private Integer evseId;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ChargingRateUnitEnumType getChargingRateUnit() {
        return chargingRateUnit;
    }

    public void setChargingRateUnit(ChargingRateUnitEnumType chargingRateUnit) {
        this.chargingRateUnit = chargingRateUnit;
    }

    public Integer getEvseId() {
        return evseId;
    }

    public void setEvseId(Integer evseId) {
        this.evseId = evseId;
    }
}

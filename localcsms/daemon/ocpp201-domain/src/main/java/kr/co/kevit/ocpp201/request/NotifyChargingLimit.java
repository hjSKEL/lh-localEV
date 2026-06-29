/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.ChargingLimitType;
import kr.co.kevit.ocpp201.domain.ChargingScheduleType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class NotifyChargingLimit {
    
    /**
     * "minItems": 1
     */
    private List<ChargingScheduleType> chargingSchedule;
    
    private Integer evseId;
    
    /**
     * required
     */
    private ChargingLimitType chargingLimit;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<ChargingScheduleType> getChargingSchedule() {
        return chargingSchedule;
    }

    public void setChargingSchedule(List<ChargingScheduleType> chargingSchedule) {
        this.chargingSchedule = chargingSchedule;
    }

    public Integer getEvseId() {
        return evseId;
    }

    public void setEvseId(Integer evseId) {
        this.evseId = evseId;
    }

    public ChargingLimitType getChargingLimit() {
        return chargingLimit;
    }

    public void setChargingLimit(ChargingLimitType chargingLimit) {
        this.chargingLimit = chargingLimit;
    }
    

}

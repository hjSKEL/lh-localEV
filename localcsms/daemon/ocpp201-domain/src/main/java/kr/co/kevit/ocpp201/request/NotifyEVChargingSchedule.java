/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.ChargingScheduleType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class NotifyEVChargingSchedule {
    
    /**
     * required
     * "type": "string","format": "date-time"
     */
    private String timeBase;
    
    /**
     * required
     */
    private ChargingScheduleType chargingSchedule;
    
    /**
     * required
     * "type": "integer"
     */
    private int evseId;

    /**
     * (2.1)
     */
    private Integer selectedChargingScheduleId;

    /**
     * (2.1)
     */
    private Boolean powerToleranceAcceptance;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getTimeBase() {
        return timeBase;
    }

    public void setTimeBase(String timeBase) {
        this.timeBase = timeBase;
    }

    public ChargingScheduleType getChargingSchedule() {
        return chargingSchedule;
    }

    public void setChargingSchedule(ChargingScheduleType chargingSchedule) {
        this.chargingSchedule = chargingSchedule;
    }

    public int getEvseId() {
        return evseId;
    }

    public void setEvseId(int evseId) {
        this.evseId = evseId;
    }

    public Integer getSelectedChargingScheduleId() {
        return selectedChargingScheduleId;
    }

    public void setSelectedChargingScheduleId(Integer selectedChargingScheduleId) {
        this.selectedChargingScheduleId = selectedChargingScheduleId;
    }

    public Boolean getPowerToleranceAcceptance() {
        return powerToleranceAcceptance;
    }

    public void setPowerToleranceAcceptance(Boolean powerToleranceAcceptance) {
        this.powerToleranceAcceptance = powerToleranceAcceptance;
    }

}
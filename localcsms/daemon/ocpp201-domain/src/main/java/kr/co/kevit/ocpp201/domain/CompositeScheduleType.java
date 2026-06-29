/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.ChargingRateUnitEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class CompositeScheduleType {
    
    /**
     * required
     */
    private List<ChargingSchedulePeriodType> chargingSchedulePeriod;
    
    /**
     * required
     */
    private int evseId;
    
    /**
     * required
     */
    private int duration;
    
    /**
     * required
     */
    private String scheduleStart;
    
    /**
     * required
     */
    private ChargingRateUnitEnumType chargingRateUnit;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    /**
     * Get chargingSchedulePeriod
     * @return chargingSchedulePeriod
     */
    public List<ChargingSchedulePeriodType> getChargingSchedulePeriod() {
        return chargingSchedulePeriod;
    }

    /**
     * Set chargingSchedulePeriod
     * @param chargingSchedulePeriod
     */
    public void setChargingSchedulePeriod(List<ChargingSchedulePeriodType> chargingSchedulePeriod) {
        this.chargingSchedulePeriod = chargingSchedulePeriod;
    }

    /**
     * Get evseId
     * @return evseId
     */
    public int getEvseId() {
        return evseId;
    }

    /**
     * Set evseId
     * @param evseId
     */
    public void setEvseId(int evseId) {
        this.evseId = evseId;
    }

    /**
     * Get duration
     * @return duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Set duration
     * @param duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Get scheduleStart
     * @return scheduleStart
     */
    public String getScheduleStart() {
        return scheduleStart;
    }

    /**
     * Set scheduleStart
     * @param scheduleStart
     */
    public void setScheduleStart(String scheduleStart) {
        this.scheduleStart = scheduleStart;
    }

    /**
     * Get chargingRateUnit
     * @return chargingRateUnit
     */
    public ChargingRateUnitEnumType getChargingRateUnit() {
        return chargingRateUnit;
    }

    /**
     * Set chargingRateUnit
     * @param chargingRateUnit
     */
    public void setChargingRateUnit(ChargingRateUnitEnumType chargingRateUnit) {
        this.chargingRateUnit = chargingRateUnit;
    }
}
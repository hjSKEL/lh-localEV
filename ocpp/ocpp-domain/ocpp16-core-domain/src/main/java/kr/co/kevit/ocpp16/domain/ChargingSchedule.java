/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.domain;

import java.util.List;

import kr.co.kevit.ocpp16.enumtype.ChargingRateUnitEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class ChargingSchedule {
    
    /**
     * required
     */
    private ChargingRateUnitEnum chargingRateUnit;
    
    /**
     * required
     */
    private List<ChargingSchedulePeriod> chargingSchedulePeriod;
    
    private Integer duration;
    
    /**
     * "format": "date-time"
     */
    private String startSchedule;
    
    /**
     * "type": "number",
        "multipleOf" : 0.1
     */
    private double minChargingRate;

    public ChargingRateUnitEnum getChargingRateUnit() {
        return chargingRateUnit;
    }

    public void setChargingRateUnit(ChargingRateUnitEnum chargingRateUnit) {
        this.chargingRateUnit = chargingRateUnit;
    }

    public List<ChargingSchedulePeriod> getChargingSchedulePeriod() {
        return chargingSchedulePeriod;
    }

    public void setChargingSchedulePeriod(List<ChargingSchedulePeriod> chargingSchedulePeriod) {
        this.chargingSchedulePeriod = chargingSchedulePeriod;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getStartSchedule() {
        return startSchedule;
    }

    public void setStartSchedule(String startSchedule) {
        this.startSchedule = startSchedule;
    }

    public double getMinChargingRate() {
        return minChargingRate;
    }

    public void setMinChargingRate(double minChargingRate) {
        this.minChargingRate = minChargingRate;
    }

}
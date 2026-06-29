/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.domain;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class ChargingSchedulePeriod {

    /**
     * required
     */
    private Integer startPeriod;
    
    /**
     * required
     */
    private Integer numberPhases;
    
    /**
     * "type": "number","multipleOf" : 0.1
     */
    private double limit;

    public Integer getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(Integer startPeriod) {
        this.startPeriod = startPeriod;
    }

    public Integer getNumberPhases() {
        return numberPhases;
    }

    public void setNumberPhases(Integer numberPhases) {
        this.numberPhases = numberPhases;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }
    
}
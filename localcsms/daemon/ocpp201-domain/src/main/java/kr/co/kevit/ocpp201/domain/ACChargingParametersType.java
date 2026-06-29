/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * EV AC charging parameters.
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class ACChargingParametersType {
    
    /**
     * required
     */
    private int energyAmount;
    
    /**
     * required
     */
    private int evMinCurrent;
    
    /**
     * required
     */
    private int evMaxCurrent;
    
    /**
     * required
     */
    private int evMaxVoltage;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public int getEnergyAmount() {
        return energyAmount;
    }

    public void setEnergyAmount(int energyAmount) {
        this.energyAmount = energyAmount;
    }

    public int getEvMinCurrent() {
        return evMinCurrent;
    }

    public void setEvMinCurrent(int evMinCurrent) {
        this.evMinCurrent = evMinCurrent;
    }

    public int getEvMaxCurrent() {
        return evMaxCurrent;
    }

    public void setEvMaxCurrent(int evMaxCurrent) {
        this.evMaxCurrent = evMaxCurrent;
    }

    public int getEvMaxVoltage() {
        return evMaxVoltage;
    }

    public void setEvMaxVoltage(int evMaxVoltage) {
        this.evMaxVoltage = evMaxVoltage;
    }
}

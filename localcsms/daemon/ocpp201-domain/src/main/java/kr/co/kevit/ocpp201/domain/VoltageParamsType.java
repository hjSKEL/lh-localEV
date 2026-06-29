/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import kr.co.kevit.ocpp201.enumtype.PowerDuringCessationEnumType;

import java.util.Map;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 21.
 */
public class VoltageParamsType {
    //
    private Map<String, Object> customData;

    /**
     * EN 50549-1 chapter 4.9.3.4
     * Voltage threshold for the 10 min time window mean value monitoring.
     * The 10 min mean is recalculated up to every 3 s.
     * If the present voltage is above this threshold for more than the time defined by OverVoltage10MinMeanTripDelay, the EV must trip.
     * This value is mandatory if OverVoltage10MinMeanTripDelay is set.
     */
    private Double hvMeanValue10Min;

    /**
     * Time for which the voltage is allowed to stay above the 10 min mean value.
     * After this time, the EV must trip.
     * This value is mandatory if OverVoltageMeanValue10min is set.
     */
    private Double hv10MinMeanTripDelay;

    private PowerDuringCessationEnumType powerDuringCessation;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Double getHvMeanValue10Min() {
        return hvMeanValue10Min;
    }

    public void setHvMeanValue10Min(Double hvMeanValue10Min) {
        this.hvMeanValue10Min = hvMeanValue10Min;
    }

    public Double getHv10MinMeanTripDelay() {
        return hv10MinMeanTripDelay;
    }

    public void setHv10MinMeanTripDelay(Double hv10MinMeanTripDelay) {
        this.hv10MinMeanTripDelay = hv10MinMeanTripDelay;
    }

    public PowerDuringCessationEnumType getPowerDuringCessation() {
        return powerDuringCessation;
    }

    public void setPowerDuringCessation(PowerDuringCessationEnumType powerDuringCessation) {
        this.powerDuringCessation = powerDuringCessation;
    }
}

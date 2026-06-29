/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 21.
 */
public class HysteresisType {
    //
    private Map<String, Object> customData;

    /**
     * High value for return to normal operation after a grid event, in absolute value. This value adopts the same unit as defined by yUnit
     */
    private double hysteresisHigh;

    /**
     * Low value for return to normal operation after a grid event, in absolute value. This value adopts the same unit as defined by yUnit
     */
    private double hysteresisLow;

    /**
     * Delay in seconds, once grid parameter within HysteresisLow and HysteresisHigh, for the EV to return to normal operation after a grid event.
     */
    private double hysteresisDelay;

    /**
     * Set default rate of change (ramp rate %/s) for the EV to return to normal operation after a grid event
     */
    private double hysteresisGradient;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public double getHysteresisHigh() {
        return hysteresisHigh;
    }

    public void setHysteresisHigh(double hysteresisHigh) {
        this.hysteresisHigh = hysteresisHigh;
    }

    public double getHysteresisLow() {
        return hysteresisLow;
    }

    public void setHysteresisLow(double hysteresisLow) {
        this.hysteresisLow = hysteresisLow;
    }

    public double getHysteresisDelay() {
        return hysteresisDelay;
    }

    public void setHysteresisDelay(double hysteresisDelay) {
        this.hysteresisDelay = hysteresisDelay;
    }

    public double getHysteresisGradient() {
        return hysteresisGradient;
    }

    public void setHysteresisGradient(double hysteresisGradient) {
        this.hysteresisGradient = hysteresisGradient;
    }
}

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
public class FixedPFType {
    //
    private Map<String, Object> customData;

    /**
     * required
     * Priority of setting (0=highest)
     * minimum": 0.0
     */
    private double priority;

    /**
     * required
     * Power factor, cos(phi), as value between 0..1.
     */
    private int displacement;

    /**
     * required
     * True when absorbing reactive power (under-excited), false when injecting reactive power (over-excited).
     */
    private int excitation;

    /**
     * Time when this setting becomes active
     */
    private String startTime;

    /**
     * Duration in seconds that this setting is active.
     */
    private int duration;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public int getDisplacement() {
        return displacement;
    }

    public void setDisplacement(int displacement) {
        this.displacement = displacement;
    }

    public int getExcitation() {
        return excitation;
    }

    public void setExcitation(int excitation) {
        this.excitation = excitation;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

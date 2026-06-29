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
public class FreqDroopType {
    //
    private Map<String, Object> customData;

    /**
     * required
     * Priority of setting (0=highest)
     * minimum: 0.0
     */
    private double priority;

    /**
     * Over-frequency start of droop
     * required
     */
    private int overFreq;

    /**
     * Under-frequency start of droop
     * required
     */
    private int underFreq;

    /**
     * Over-frequency droop per unit, oFDroop
     * required
     */
    private int overDroop;

    /**
     * Under-frequency droop per unit, uFDroop
     * required
     */
    private int underDroop;

    /**
     * Open loop response time in seconds
     * required
     */
    private int responseTime;

    /**
     * Time when this setting becomes active
     */
    private String startTime;

    /**
     * Duration in seconds that this setting is active
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

    public int getOverFreq() {
        return overFreq;
    }

    public void setOverFreq(int overFreq) {
        this.overFreq = overFreq;
    }

    public int getUnderFreq() {
        return underFreq;
    }

    public void setUnderFreq(int underFreq) {
        this.underFreq = underFreq;
    }

    public int getOverDroop() {
        return overDroop;
    }

    public void setOverDroop(int overDroop) {
        this.overDroop = overDroop;
    }

    public int getUnderDroop() {
        return underDroop;
    }

    public void setUnderDroop(int underDroop) {
        this.underDroop = underDroop;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }
}

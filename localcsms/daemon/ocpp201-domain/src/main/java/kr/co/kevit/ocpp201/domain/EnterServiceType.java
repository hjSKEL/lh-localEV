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
public class EnterServiceType {
    //
    private Map<String, Object> customData;

    /**
     * required
     * Priority of setting (0=highest)
     * minimum": 0.0
     */
    private double priority;

    /**
     * Enter service voltage high
     * required
     */
    private int highVoltage;

    /**
     * Enter service voltage low
     * required
     */
    private int lowVoltage;

    /**
     * Enter service frequency high
     * required
     */
    private int highFreq;

    /**
     * Enter service frequency low
     * required
     */
    private int lowFreq;

    /**
     * Enter service delay
     */
    private int delay;

    /**
     * Enter service randomized delay
     */
    private int randomDelay;

    /**
     * Enter service ramp rate in seconds
     */
    private int rampRate;

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

    public int getHighVoltage() {
        return highVoltage;
    }

    public void setHighVoltage(int highVoltage) {
        this.highVoltage = highVoltage;
    }

    public int getLowVoltage() {
        return lowVoltage;
    }

    public void setLowVoltage(int lowVoltage) {
        this.lowVoltage = lowVoltage;
    }

    public int getHighFreq() {
        return highFreq;
    }

    public void setHighFreq(int highFreq) {
        this.highFreq = highFreq;
    }

    public int getLowFreq() {
        return lowFreq;
    }

    public void setLowFreq(int lowFreq) {
        this.lowFreq = lowFreq;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getRandomDelay() {
        return randomDelay;
    }

    public void setRandomDelay(int randomDelay) {
        this.randomDelay = randomDelay;
    }

    public int getRampRate() {
        return rampRate;
    }

    public void setRampRate(int rampRate) {
        this.rampRate = rampRate;
    }
}

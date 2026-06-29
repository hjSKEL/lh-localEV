/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import kr.co.kevit.ocpp201.enumtype.DERUnitEnumType;

import java.util.Map;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 21.
 */
public class FixedVarType {
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
     * The value specifies a target var output interpreted as a signed percentage (-100 to 100).
     * A negative value refers to charging, whereas a positive one refers to discharging.
     * The value type is determined by the unit field.
     */
    private int setpoint;
    /**
     * required
     */
    private DERUnitEnumType unit;

    /**
     * Time when this setting becomes active.
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

    public int getSetpoint() {
        return setpoint;
    }

    public void setSetpoint(int setpoint) {
        this.setpoint = setpoint;
    }

    public DERUnitEnumType getUnit() {
        return unit;
    }

    public void setUnit(DERUnitEnumType unit) {
        this.unit = unit;
    }
}

/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.DERUnitEnumType;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 21.
 */
public class DERCurveType {
    //
    private Map<String, Object> customData;

    /**
     * required
     */
    private List<DERCurvePointsType> curveData;

    private HysteresisType hysteresis;

    /**
     * required
     * Priority of curve (0=highest)
     */
    private int priority;

    private ReactivePowerParamsType reactivePowerParams;

    private VoltageParamsType voltageParams;

    /**
     * required
     */
    private DERUnitEnumType yUnit;

    /**
     * Open loop response time, the time to ramp up to 90% of the new target in response to the change in voltage, in seconds.
     * A value of 0 is used to mean no limit. When not present, the device should follow its default behavior.
     */
    private Double responseTime;

    /**
     * Point in time when this curve will become activated. Only absent when _default_ is true.
     */
    private String startTime;

    /**
     * Duration in seconds that this curve will be active. Only absent when _default_ is true.
     */
    private Double duration;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<DERCurvePointsType> getCurveData() {
        return curveData;
    }

    public void setCurveData(List<DERCurvePointsType> curveData) {
        this.curveData = curveData;
    }

    public HysteresisType getHysteresis() {
        return hysteresis;
    }

    public void setHysteresis(HysteresisType hysteresis) {
        this.hysteresis = hysteresis;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public ReactivePowerParamsType getReactivePowerParams() {
        return reactivePowerParams;
    }

    public void setReactivePowerParams(ReactivePowerParamsType reactivePowerParams) {
        this.reactivePowerParams = reactivePowerParams;
    }

    public VoltageParamsType getVoltageParams() {
        return voltageParams;
    }

    public void setVoltageParams(VoltageParamsType voltageParams) {
        this.voltageParams = voltageParams;
    }

    public DERUnitEnumType getyUnit() {
        return yUnit;
    }

    public void setyUnit(DERUnitEnumType yUnit) {
        this.yUnit = yUnit;
    }

    public Double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Double responseTime) {
        this.responseTime = responseTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }
}

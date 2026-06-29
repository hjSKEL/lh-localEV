/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import kr.co.kevit.ocpp201.enumtype.OperationModeEnumType;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class ChargingSchedulePeriodType {
    
    /**
     * required
     */
    private Integer startPeriod;
    
    /**
     * required
     * type :number
     */
    private Integer limit;

    /**
     * *(2.1)* Charging rate limit on phase L2  in the applicable _chargingRateUnit_.
     */
    private Integer limit_L2;

    /**
     * *(2.1)* Charging rate limit on phase L3  in the applicable _chargingRateUnit_.
     */
    private Integer limit_L3;

    /**
     * "type": "integer"
     */
    private Integer numberPhases;
    
    /**
     * "type": "integer"
     */
    private Integer phaseToUse;

    /**
     * *(2.1)* Limit in _chargingRateUnit_ that the EV is allowed to discharge with.
     * Note, these are negative values in order to be consistent with _setpoint_, which can be positive and negative.
     * For AC this field represents the sum of all phases, unless values are provided for L2 and L3, in which case this field represents phase L1.
     */
    private Integer dischargeLimit;

    /**
     * *(2.1)* Limit in _chargingRateUnit_ on phase L2 that the EV is allowed to discharge with.
     */
    private Integer dischargeLimit_L2;

    /**
     * *(2.1)* Limit in _chargingRateUnit_ on phase L3 that the EV is allowed to discharge with.
     */
    private Integer dischargeLimit_L3;

    /**
     * *(2.1)* Setpoint in _chargingRateUnit_ that the EV should follow as close as possible. Use negative values for discharging.
     * When a limit and/or _dischargeLimit_ are given the overshoot when following _setpoint_ must remain within these values.
     * This field represents the sum of all phases, unless values are provided for L2 and L3, in which case this field represents phase L1.
     */
    private Integer setpoint;

    /**
     * *(2.1)* Setpoint in _chargingRateUnit_ that the EV should follow on phase L2 as close as possible.
     */
    private Integer setpoint_L2;

    /**
     * *(2.1)* Setpoint in _chargingRateUnit_ that the EV should follow on phase L3 as close as possible.
     */
    private Integer setpoint_L3;

    /**
     * *(2.1)* Setpoint for reactive power (or current) in _chargingRateUnit_ that the EV should follow as closely as possible.
     * Positive values for inductive, negative for capacitive reactive power or current.
     * This field represents the sum of all phases, unless values are provided for L2 and L3, in which case this field represents phase L1.
     */
    private Integer setpointReactive;

    /**
     * *(2.1)* Setpoint for reactive power (or current) in _chargingRateUnit_ that the EV should follow on phase L2 as closely as possible.
     */
    private Integer setpointReactive_L2;

    /**
     * *(2.1)* Setpoint for reactive power (or current) in _chargingRateUnit_ that the EV should follow on phase L3 as closely as possible.
     */
    private Integer setpointReactive_L3;

    /**
     * *(2.1)* Charging operation mode to use during this time interval. When absent defaults to `ChargingOnly`.
     */
    private OperationModeEnumType operationMode;

    /**
     * *(2.1)* Of true, the EVSE must turn off power electronics/modules associated with this transaction. Default value when absent is false.
     */
    private Boolean evseSleep;

    /**
     * *(2.1)* Power value that, when present, is used as a baseline on top of which values from _v2xFreqWattCurve_ and _v2xSignalWattCurve_ are added.
     */
    private Integer v2xBaseline;

    /**
     * *(2.1)* If  true, the EV should attempt to keep the BMS preconditioned for this time interval.
     */
    private Boolean preconditioningRequest;

    /**
     * *(2.1)* A point of a frequency-watt curve.
     *  "minItems": 1,
     *  "maxItems": 20
     */
    private List<V2XFreqWattPointType> v2xFreqWattCurve;

    /**
     * *(2.1)* A point of a signal-watt curve
     *  "minItems": 1,
     *  "maxItems": 20
     */
    private List<V2XSignalWattPointType> v2xSignalWattCurve;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Integer getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(Integer startPeriod) {
        this.startPeriod = startPeriod;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getNumberPhases() {
        return numberPhases;
    }

    public void setNumberPhases(Integer numberPhases) {
        this.numberPhases = numberPhases;
    }

    public Integer getPhaseToUse() {
        return phaseToUse;
    }

    public void setPhaseToUse(Integer phaseToUse) {
        this.phaseToUse = phaseToUse;
    }

    public Integer getLimit_L2() {
        return limit_L2;
    }

    public void setLimit_L2(Integer limit_L2) {
        this.limit_L2 = limit_L2;
    }

    public Integer getLimit_L3() {
        return limit_L3;
    }

    public void setLimit_L3(Integer limit_L3) {
        this.limit_L3 = limit_L3;
    }

    public Integer getDischargeLimit() {
        return dischargeLimit;
    }

    public void setDischargeLimit(Integer dischargeLimit) {
        this.dischargeLimit = dischargeLimit;
    }

    public Integer getDischargeLimit_L2() {
        return dischargeLimit_L2;
    }

    public void setDischargeLimit_L2(Integer dischargeLimit_L2) {
        this.dischargeLimit_L2 = dischargeLimit_L2;
    }

    public Integer getDischargeLimit_L3() {
        return dischargeLimit_L3;
    }

    public void setDischargeLimit_L3(Integer dischargeLimit_L3) {
        this.dischargeLimit_L3 = dischargeLimit_L3;
    }

    public Integer getSetpoint() {
        return setpoint;
    }

    public void setSetpoint(Integer setpoint) {
        this.setpoint = setpoint;
    }

    public Integer getSetpoint_L2() {
        return setpoint_L2;
    }

    public void setSetpoint_L2(Integer setpoint_L2) {
        this.setpoint_L2 = setpoint_L2;
    }

    public Integer getSetpoint_L3() {
        return setpoint_L3;
    }

    public void setSetpoint_L3(Integer setpoint_L3) {
        this.setpoint_L3 = setpoint_L3;
    }

    public Integer getSetpointReactive() {
        return setpointReactive;
    }

    public void setSetpointReactive(Integer setpointReactive) {
        this.setpointReactive = setpointReactive;
    }

    public Integer getSetpointReactive_L2() {
        return setpointReactive_L2;
    }

    public void setSetpointReactive_L2(Integer setpointReactive_L2) {
        this.setpointReactive_L2 = setpointReactive_L2;
    }

    public Integer getSetpointReactive_L3() {
        return setpointReactive_L3;
    }

    public void setSetpointReactive_L3(Integer setpointReactive_L3) {
        this.setpointReactive_L3 = setpointReactive_L3;
    }

    public OperationModeEnumType getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(OperationModeEnumType operationMode) {
        this.operationMode = operationMode;
    }

    public Boolean getEvseSleep() {
        return evseSleep;
    }

    public void setEvseSleep(Boolean evseSleep) {
        this.evseSleep = evseSleep;
    }

    public Integer getV2xBaseline() {
        return v2xBaseline;
    }

    public void setV2xBaseline(Integer v2xBaseline) {
        this.v2xBaseline = v2xBaseline;
    }

    public Boolean getPreconditioningRequest() {
        return preconditioningRequest;
    }

    public void setPreconditioningRequest(Boolean preconditioningRequest) {
        this.preconditioningRequest = preconditioningRequest;
    }

    public List<V2XFreqWattPointType> getV2xFreqWattCurve() {
        return v2xFreqWattCurve;
    }

    public void setV2xFreqWattCurve(List<V2XFreqWattPointType> v2xFreqWattCurve) {
        this.v2xFreqWattCurve = v2xFreqWattCurve;
    }

    public List<V2XSignalWattPointType> getV2xSignalWattCurve() {
        return v2xSignalWattCurve;
    }

    public void setV2xSignalWattCurve(List<V2XSignalWattPointType> v2xSignalWattCurve) {
        this.v2xSignalWattCurve = v2xSignalWattCurve;
    }
}

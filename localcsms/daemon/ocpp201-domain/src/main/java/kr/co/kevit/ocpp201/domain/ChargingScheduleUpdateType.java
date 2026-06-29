/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * Updates to a ChargingSchedulePeriodType for dynamic charging profiles.
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 12. 20.
 */
public class ChargingScheduleUpdateType {
    
    /**
     * Optional only when not required by the _operationMode_, as in CentralSetpoint, ExternalSetpoint, ExternalLimits, LocalFrequency,  LocalLoadBalancing.
     * Charging rate limit during the schedule period, in the applicable _chargingRateUnit_.
     * This SHOULD be a non-negative value; a negative value is only supported for backwards compatibility with older systems that use a negative value to specify a discharging limit.
     * For AC this field represents the sum of all phases, unless values are provided for L2 and L3, in which case this field represents phase L1
     */
    private Double limit;

    /**
     * *(2.1)* Charging rate limit on phase L2  in the applicable _chargingRateUnit_.
     */
    private Double limit_L2;

    /**
     * *(2.1)* Charging rate limit on phase L3  in the applicable _chargingRateUnit_.
     */
    private Double limit_L3;

    /**
     * *(2.1)* Limit in _chargingRateUnit_ that the EV is allowed to discharge with. Note, these are negative values in order to be consistent with _setpoint_, which can be positive and negative.
     * For AC this field represents the sum of all phases, unless values are provided for L2 and L3, in which case this field represents phase L1.
     */
    private Double dischargeLimit;

    /**
     * *(2.1)* Limit in _chargingRateUnit_ on phase L2 that the EV is allowed to discharge with.
     */
    private Double dischargeLimit_L2;

    /**
     * *(2.1)* Limit in _chargingRateUnit_ on phase L3 that the EV is allowed to discharge with.
     */
    private Double dischargeLimit_L3;

    /**
     * *(2.1)* Setpoint in _chargingRateUnit_ that the EV should follow as close as possible. Use negative values for discharging.
     * When a limit and/or _dischargeLimit_ are given the overshoot when following _setpoint_ must remain within these values.
     * This field represents the sum of all phases, unless values are provided for L2 and L3, in which case this field represents phase L1.
     */
    private Double setpoint;

    /**
     * *(2.1)* Setpoint in _chargingRateUnit_ that the EV should follow on phase L2 as close as possible.
     */
    private Double setpoint_L2;

    /**
     * *(2.1)* Setpoint in _chargingRateUnit_ that the EV should follow on phase L3 as close as possible.
     */
    private Double setpoint_L3;

    /**
     * *(2.1)* Setpoint for reactive power (or current) in _chargingRateUnit_ that the EV should follow as closely as possible. Positive values for inductive, negative for capacitive reactive power or current.
     * This field represents the sum of all phases, unless values are provided for L2 and L3, in which case this field represents phase L1.
     */
    private Double setPointReactive;

    /**
     * *(2.1)* Setpoint for reactive power (or current) in _chargingRateUnit_ that the EV should follow on phase L2 as closely as possible.
     */
    private Double setPointReactive_L2;

    /**
     * *(2.1)* Setpoint for reactive power (or current) in _chargingRateUnit_ that the EV should follow on phase L3 as closely as possible.
     */
    private Double setPointReactive_L3;

    private Map<String, Object> customData;

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public Double getLimit_L2() {
        return limit_L2;
    }

    public void setLimit_L2(Double limit_L2) {
        this.limit_L2 = limit_L2;
    }

    public Double getLimit_L3() {
        return limit_L3;
    }

    public void setLimit_L3(Double limit_L3) {
        this.limit_L3 = limit_L3;
    }

    public Double getDischargeLimit() {
        return dischargeLimit;
    }

    public void setDischargeLimit(Double dischargeLimit) {
        this.dischargeLimit = dischargeLimit;
    }

    public Double getDischargeLimit_L2() {
        return dischargeLimit_L2;
    }

    public void setDischargeLimit_L2(Double dischargeLimit_L2) {
        this.dischargeLimit_L2 = dischargeLimit_L2;
    }

    public Double getDischargeLimit_L3() {
        return dischargeLimit_L3;
    }

    public void setDischargeLimit_L3(Double dischargeLimit_L3) {
        this.dischargeLimit_L3 = dischargeLimit_L3;
    }

    public Double getSetpoint() {
        return setpoint;
    }

    public void setSetpoint(Double setpoint) {
        this.setpoint = setpoint;
    }

    public Double getSetpoint_L2() {
        return setpoint_L2;
    }

    public void setSetpoint_L2(Double setpoint_L2) {
        this.setpoint_L2 = setpoint_L2;
    }

    public Double getSetpoint_L3() {
        return setpoint_L3;
    }

    public void setSetpoint_L3(Double setpoint_L3) {
        this.setpoint_L3 = setpoint_L3;
    }

    public Double getSetPointReactive() {
        return setPointReactive;
    }

    public void setSetPointReactive(Double setPointReactive) {
        this.setPointReactive = setPointReactive;
    }

    public Double getSetPointReactive_L2() {
        return setPointReactive_L2;
    }

    public void setSetPointReactive_L2(Double setPointReactive_L2) {
        this.setPointReactive_L2 = setPointReactive_L2;
    }

    public Double getSetPointReactive_L3() {
        return setPointReactive_L3;
    }

    public void setSetPointReactive_L3(Double setPointReactive_L3) {
        this.setPointReactive_L3 = setPointReactive_L3;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}

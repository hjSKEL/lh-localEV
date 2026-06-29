/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 23.
 */
public class OverstayRuleType {
    //
    private Map<String, Object> customData;

    /**
     * required
     */
    private RationalNumberType overstayFee;

    /**
     * Human readable string to identify the overstay rule.
     */
    private String overstayRuleDescription;

    /**
     * required
     * Time in seconds after trigger of the parent Overstay Rules for this particular fee to apply.
     */
    private Integer startTime;

    /**
     * required
     * Time till overstay will be reapplied
     */
    private Integer overstayFeePeriod;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public RationalNumberType getOverstayFee() {
        return overstayFee;
    }

    public void setOverstayFee(RationalNumberType overstayFee) {
        this.overstayFee = overstayFee;
    }

    public String getOverstayRuleDescription() {
        return overstayRuleDescription;
    }

    public void setOverstayRuleDescription(String overstayRuleDescription) {
        this.overstayRuleDescription = overstayRuleDescription;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getOverstayFeePeriod() {
        return overstayFeePeriod;
    }

    public void setOverstayFeePeriod(Integer overstayFeePeriod) {
        this.overstayFeePeriod = overstayFeePeriod;
    }
}

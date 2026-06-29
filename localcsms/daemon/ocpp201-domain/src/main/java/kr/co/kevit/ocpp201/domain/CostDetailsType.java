/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

/**
 * (2.1)
 */
public class CostDetailsType {

    private List<ChargingPeriodType> chargingPeriods;

    private TotalCostType totalCost;

    private TotalUsageType totalUsage;

    private Boolean failureToCalculate;

    private String failureReason;

    private Map<String, Object> customData;

    public List<ChargingPeriodType> getChargingPeriods() {
        return chargingPeriods;
    }

    public void setChargingPeriods(List<ChargingPeriodType> chargingPeriods) {
        this.chargingPeriods = chargingPeriods;
    }

    public TotalCostType getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(TotalCostType totalCost) {
        this.totalCost = totalCost;
    }

    public TotalUsageType getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(TotalUsageType totalUsage) {
        this.totalUsage = totalUsage;
    }

    public Boolean getFailureToCalculate() {
        return failureToCalculate;
    }

    public void setFailureToCalculate(Boolean failureToCalculate) {
        this.failureToCalculate = failureToCalculate;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}

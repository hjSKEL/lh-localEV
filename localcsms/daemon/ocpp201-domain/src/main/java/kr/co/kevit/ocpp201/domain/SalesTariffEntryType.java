/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class SalesTariffEntryType {
    
    /**
     * required
     */
    private RelativeTimeIntervalType relativeTimeInterval;
    
    /**
     * "type": "integer", "minimum": 0
     */
    private int ePriceLevel;
    
    /**
     * "minItems": 1,"maxItems": 3
     */
    private List<ConsumptionCostType> consumptionCost;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public RelativeTimeIntervalType getRelativeTimeInterval() {
        return relativeTimeInterval;
    }

    public void setRelativeTimeInterval(RelativeTimeIntervalType relativeTimeInterval) {
        this.relativeTimeInterval = relativeTimeInterval;
    }

    public int getePriceLevel() {
        return ePriceLevel;
    }

    public void setePriceLevel(int ePriceLevel) {
        this.ePriceLevel = ePriceLevel;
    }

    public List<ConsumptionCostType> getConsumptionCost() {
        return consumptionCost;
    }

    public void setConsumptionCost(List<ConsumptionCostType> consumptionCost) {
        this.consumptionCost = consumptionCost;
    }

}

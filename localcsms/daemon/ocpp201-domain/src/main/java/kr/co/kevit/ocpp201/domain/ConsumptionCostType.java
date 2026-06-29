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
public class ConsumptionCostType {
    
    /**
     * required
     * "type": "number"
     */
    private Integer startValue;
    
    /**
     * required
     * "minItems": 1,
      "maxItems": 3
     */
    private List<CostType> cost;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Integer getStartValue() {
        return startValue;
    }

    public void setStartValue(Integer startValue) {
        this.startValue = startValue;
    }

    public List<CostType> getCost() {
        return cost;
    }

    public void setCost(List<CostType> cost) {
        this.cost = cost;
    }
    
}
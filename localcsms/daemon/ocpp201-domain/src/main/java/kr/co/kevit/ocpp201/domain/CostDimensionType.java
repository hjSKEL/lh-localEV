/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.CostDimensionEnumType;

/**
 * (2.1)
 */
public class CostDimensionType {

    /**
     * required
     */
    private CostDimensionEnumType type;

    /**
     * required
     */
    private double volume;

    private Map<String, Object> customData;

    public CostDimensionEnumType getType() {
        return type;
    }

    public void setType(CostDimensionEnumType type) {
        this.type = type;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}

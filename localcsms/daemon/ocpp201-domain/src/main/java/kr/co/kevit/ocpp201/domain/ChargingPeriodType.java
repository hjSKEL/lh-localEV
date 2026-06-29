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
public class ChargingPeriodType {

    /**
     * required
     */
    private List<CostDimensionType> dimensions;

    private String tariffId;

    /**
     * required
     */
    private String startPeriod;

    private Map<String, Object> customData;

    public List<CostDimensionType> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<CostDimensionType> dimensions) {
        this.dimensions = dimensions;
    }

    public String getTariffId() {
        return tariffId;
    }

    public void setTariffId(String tariffId) {
        this.tariffId = tariffId;
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}

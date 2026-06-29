/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * (2.1)
 */
public class TariffTimePriceType {

    /**
     * required
     */
    private double priceMinute;

    private TariffConditionsType conditions;

    private Map<String, Object> customData;

    public double getPriceMinute() {
        return priceMinute;
    }

    public void setPriceMinute(double priceMinute) {
        this.priceMinute = priceMinute;
    }

    public TariffConditionsType getConditions() {
        return conditions;
    }

    public void setConditions(TariffConditionsType conditions) {
        this.conditions = conditions;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}

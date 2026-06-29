/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * (2.1)
 */
public class TariffFixedPriceType {

    /**
     * required
     */
    private double priceFixed;

    private TariffConditionsFixedType conditions;

    private Map<String, Object> customData;

    public double getPriceFixed() {
        return priceFixed;
    }

    public void setPriceFixed(double priceFixed) {
        this.priceFixed = priceFixed;
    }

    public TariffConditionsFixedType getConditions() {
        return conditions;
    }

    public void setConditions(TariffConditionsFixedType conditions) {
        this.conditions = conditions;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}

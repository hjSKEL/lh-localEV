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
public class PriceType {

    private Double exclTax;

    private Double inclTax;

    private List<TaxRateType> taxRates;

    private Map<String, Object> customData;

    public Double getExclTax() {
        return exclTax;
    }

    public void setExclTax(Double exclTax) {
        this.exclTax = exclTax;
    }

    public Double getInclTax() {
        return inclTax;
    }

    public void setInclTax(Double inclTax) {
        this.inclTax = inclTax;
    }

    public List<TaxRateType> getTaxRates() {
        return taxRates;
    }

    public void setTaxRates(List<TaxRateType> taxRates) {
        this.taxRates = taxRates;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}

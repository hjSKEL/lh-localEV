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
public class SalesTariffType {
    
    /**
     * required
     */
    private Integer id;
    
    /**
     * "maxLength": 32
     */
    private String salesTariffDescription;
    
    private Integer numEPriceLevels;
    
    /**
     * required
     * "minItems": 1,"maxItems": 1024
     */
    private List<SalesTariffEntryType> salesTariffEntry;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSalesTariffDescription() {
        return salesTariffDescription;
    }

    public void setSalesTariffDescription(String salesTariffDescription) {
        this.salesTariffDescription = salesTariffDescription;
    }

    public Integer getNumEPriceLevels() {
        return numEPriceLevels;
    }

    public void setNumEPriceLevels(Integer numEPriceLevels) {
        this.numEPriceLevels = numEPriceLevels;
    }

    public List<SalesTariffEntryType> getSalesTariffEntry() {
        return salesTariffEntry;
    }

    public void setSalesTariffEntry(List<SalesTariffEntryType> salesTariffEntry) {
        this.salesTariffEntry = salesTariffEntry;
    }

}
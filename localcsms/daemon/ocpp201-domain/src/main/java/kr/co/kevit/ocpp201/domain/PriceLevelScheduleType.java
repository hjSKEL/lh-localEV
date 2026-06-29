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
 * @since 2020. 1. 15.
 */
public class PriceLevelScheduleType {
    
    /**
     * required
     */
    private List<PriceLevelScheduleEntryType> priceLevelScheduleEntries;
    
    /**
     * required
     * Starting point of this price schedule.
     */
    private String timeAnchor;

    /**
     * required
     * Unique ID of this price schedule.
     * "minimum": 0.0
     */
    private Integer priceScheduleId;

    /**
     * Description of the price schedule.
     * "maxLength": 32
     */
    private String priceScheduleDescription;

    /**
     * required
     * Defines the overall number of distinct price level elements used across all PriceLevelSchedules.
     * "minimum": 0.0
     */
    private Integer numberOfPriceLevels;


    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<PriceLevelScheduleEntryType> getPriceLevelScheduleEntries() {
        return priceLevelScheduleEntries;
    }

    public void setPriceLevelScheduleEntries(List<PriceLevelScheduleEntryType> priceLevelScheduleEntries) {
        this.priceLevelScheduleEntries = priceLevelScheduleEntries;
    }

    public String getTimeAnchor() {
        return timeAnchor;
    }

    public void setTimeAnchor(String timeAnchor) {
        this.timeAnchor = timeAnchor;
    }

    public Integer getPriceScheduleId() {
        return priceScheduleId;
    }

    public void setPriceScheduleId(Integer priceScheduleId) {
        this.priceScheduleId = priceScheduleId;
    }

    public String getPriceScheduleDescription() {
        return priceScheduleDescription;
    }

    public void setPriceScheduleDescription(String priceScheduleDescription) {
        this.priceScheduleDescription = priceScheduleDescription;
    }

    public Integer getNumberOfPriceLevels() {
        return numberOfPriceLevels;
    }

    public void setNumberOfPriceLevels(Integer numberOfPriceLevels) {
        this.numberOfPriceLevels = numberOfPriceLevels;
    }
}
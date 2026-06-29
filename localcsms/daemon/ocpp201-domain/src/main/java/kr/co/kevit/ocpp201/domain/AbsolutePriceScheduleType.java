/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;

/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2020. 1. 15.
 */
public class AbsolutePriceScheduleType {
    
    /**
     * Starting point of price schedule.
     */
    private String timeAnchor;
    
    /**
     * Unique ID of price schedule
     */
    private Integer priceScheduleID;

    /**
     * Description of the price schedule.
     */
    private String priceScheduleDescription;

    /**
     * Currency according to ISO 4217.
     */
    private String currency;

    /**
     * String that indicates what language is used for the human readable strings in the price schedule. Based on ISO 639.
     */
    private String language;

    /**
     * A string in URN notation which shall uniquely identify an algorithm that defines how to compute an energy fee sum
     * for a specific power profile based on the EnergyFee information from the PriceRule elements.
     */
    private String priceAlgorithm;

    private RationalNumberType minimumCost;

    private RationalNumberType maximumCost;

    /**
     *    "minItems": 1,
     *    "maxItems": 1024
     */
    private List<PriceRuleStackType> priceRuleStacks;

    /**
     *    "minItems": 1,
     *    "maxItems": 10
     */
    private TaxRuleType taxRules;

    private OverstayRuleListType overstayRuleList;

    /**
     *    "minItems": 1,
     *    "maxItems": 5
     */
    private AdditionalSelectedServicesType additionalSelectedServices;

    public String getTimeAnchor() {
        return timeAnchor;
    }

    public void setTimeAnchor(String timeAnchor) {
        this.timeAnchor = timeAnchor;
    }

    public Integer getPriceScheduleID() {
        return priceScheduleID;
    }

    public void setPriceScheduleID(Integer priceScheduleID) {
        this.priceScheduleID = priceScheduleID;
    }

    public String getPriceScheduleDescription() {
        return priceScheduleDescription;
    }

    public void setPriceScheduleDescription(String priceScheduleDescription) {
        this.priceScheduleDescription = priceScheduleDescription;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPriceAlgorithm() {
        return priceAlgorithm;
    }

    public void setPriceAlgorithm(String priceAlgorithm) {
        this.priceAlgorithm = priceAlgorithm;
    }

    public RationalNumberType getMinimumCost() {
        return minimumCost;
    }

    public void setMinimumCost(RationalNumberType minimumCost) {
        this.minimumCost = minimumCost;
    }

    public RationalNumberType getMaximumCost() {
        return maximumCost;
    }

    public void setMaximumCost(RationalNumberType maximumCost) {
        this.maximumCost = maximumCost;
    }

    public List<PriceRuleStackType> getPriceRuleStacks() {
        return priceRuleStacks;
    }

    public void setPriceRuleStacks(List<PriceRuleStackType> priceRuleStacks) {
        this.priceRuleStacks = priceRuleStacks;
    }

    public TaxRuleType getTaxRules() {
        return taxRules;
    }

    public void setTaxRules(TaxRuleType taxRules) {
        this.taxRules = taxRules;
    }

    public OverstayRuleListType getOverstayRuleList() {
        return overstayRuleList;
    }

    public void setOverstayRuleList(OverstayRuleListType overstayRuleList) {
        this.overstayRuleList = overstayRuleList;
    }

    public AdditionalSelectedServicesType getAdditionalSelectedServices() {
        return additionalSelectedServices;
    }

    public void setAdditionalSelectedServices(AdditionalSelectedServicesType additionalSelectedServices) {
        this.additionalSelectedServices = additionalSelectedServices;
    }
}

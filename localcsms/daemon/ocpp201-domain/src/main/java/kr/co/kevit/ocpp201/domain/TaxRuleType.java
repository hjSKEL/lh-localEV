/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 *
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 23.
 */
public class TaxRuleType {
    
    /**
     * required
     * Id for the tax rule.
     * "minimum": 0.0
     */
    private double taxRuleID;
    
    /**
     * required
     * Human readable string to identify the tax rule.
     * "maxLength": 100
     */
    private String taxRuleName;

    /**
     * Indicates whether the tax is included in any price or not.
     */
    private boolean taxIncludedInPrice;

    /**
     * required
     * Indicates whether this tax applies to Energy Fees.
     */
    private boolean appliesToEnergyFee;

    /**
     * required
     * Indicates whether this tax applies to Parking Fees.
     */
    private boolean appliesToParkingFee;

    /**
     * required
     * Indicates whether this tax applies to Overstay Fees.
     */
    private boolean appliesToOverstayFee;

    /**
     * required
     * Indicates whether this tax applies to Minimum/Maximum Cost.
     */
    private boolean appliesToMinimumMaximumCost;

    /**
     * required
     */
    private RationalNumberType taxRate;


    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public double getTaxRuleID() {
        return taxRuleID;
    }

    public void setTaxRuleID(double taxRuleID) {
        this.taxRuleID = taxRuleID;
    }

    public String getTaxRuleName() {
        return taxRuleName;
    }

    public void setTaxRuleName(String taxRuleName) {
        this.taxRuleName = taxRuleName;
    }

    public boolean isTaxIncludedInPrice() {
        return taxIncludedInPrice;
    }

    public void setTaxIncludedInPrice(boolean taxIncludedInPrice) {
        this.taxIncludedInPrice = taxIncludedInPrice;
    }

    public boolean isAppliesToEnergyFee() {
        return appliesToEnergyFee;
    }

    public void setAppliesToEnergyFee(boolean appliesToEnergyFee) {
        this.appliesToEnergyFee = appliesToEnergyFee;
    }

    public boolean isAppliesToParkingFee() {
        return appliesToParkingFee;
    }

    public void setAppliesToParkingFee(boolean appliesToParkingFee) {
        this.appliesToParkingFee = appliesToParkingFee;
    }

    public boolean isAppliesToOverstayFee() {
        return appliesToOverstayFee;
    }

    public void setAppliesToOverstayFee(boolean appliesToOverstayFee) {
        this.appliesToOverstayFee = appliesToOverstayFee;
    }

    public boolean isAppliesToMinimumMaximumCost() {
        return appliesToMinimumMaximumCost;
    }

    public void setAppliesToMinimumMaximumCost(boolean appliesToMinimumMaximumCost) {
        this.appliesToMinimumMaximumCost = appliesToMinimumMaximumCost;
    }

    public RationalNumberType getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(RationalNumberType taxRate) {
        this.taxRate = taxRate;
    }
}
/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

/**
 * *(2.1)* Price schedule of EV energy offer. currently is not able to offer energy to discharge.
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public class EVAbsolutePriceScheduleType {
    //
    private Map<String, Object> customData;

    /**
     * required
     * Starting point in time of the EVEnergyOffer.
     */
    private String timeAnchor;

    /**
     * required
     * Currency code according to ISO 4217.
     */
    private String currency;

    /**
     * required
     */
    private List<EVAbsolutePriceScheduleEntryType> evAbsolutePriceScheduleEntries;

    /**
     * required
     * ISO 15118-20 URN of price algorithm: Power, PeakPower, StackedEnergy.
     */
    private String priceAlgorithm;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getTimeAnchor() {
        return timeAnchor;
    }

    public void setTimeAnchor(String timeAnchor) {
        this.timeAnchor = timeAnchor;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<EVAbsolutePriceScheduleEntryType> getEvAbsolutePriceScheduleEntries() {
        return evAbsolutePriceScheduleEntries;
    }

    public void setEvAbsolutePriceScheduleEntries(List<EVAbsolutePriceScheduleEntryType> evAbsolutePriceScheduleEntries) {
        this.evAbsolutePriceScheduleEntries = evAbsolutePriceScheduleEntries;
    }

    public String getPriceAlgorithm() {
        return priceAlgorithm;
    }

    public void setPriceAlgorithm(String priceAlgorithm) {
        this.priceAlgorithm = priceAlgorithm;
    }
}

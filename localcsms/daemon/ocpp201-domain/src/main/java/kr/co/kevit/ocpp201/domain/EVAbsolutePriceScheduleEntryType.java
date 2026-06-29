/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

/**
 * *(2.1)* An entry in price schedule over time for which EV is willing to discharge.
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public class EVAbsolutePriceScheduleEntryType {
    //
    private Map<String, Object> customData;

    /**
     * required
     * Starting point in time of the EVEnergyOffer.
     */
    private Integer duration;

    /**
     * The amount of seconds of this entry.
     */
    private String currency;

    /**
     * required
     * minItems": 1,
     * "maxItems": 8
     */
    private List<EVPriceRuleType> evPriceRule;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<EVPriceRuleType> getEvPriceRule() {
        return evPriceRule;
    }

    public void setEvPriceRule(List<EVPriceRuleType> evPriceRule) {
        this.evPriceRule = evPriceRule;
    }
}

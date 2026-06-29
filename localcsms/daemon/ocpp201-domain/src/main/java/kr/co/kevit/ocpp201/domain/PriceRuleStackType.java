/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

/**
 *
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 23.
 */
public class PriceRuleStackType {
    
    /**
     * required
     *   "minItems": 1,
     *   "maxItems": 8
     */
    private List<PriceRuleType> priceRule;
    
    /**
     * required
     * Duration of the stack of price rules.  he amount of seconds that define the duration of the given PriceRule(s).
     */
    private Integer duration;
    

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<PriceRuleType> getPriceRule() {
        return priceRule;
    }

    public void setPriceRule(List<PriceRuleType> priceRule) {
        this.priceRule = priceRule;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
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
public class MeterValueType {
    
    /**
     * required
     * "minItems": 1
     */
    private List<SampledValueType> sampledValue;
    
    /**
     * required
     * type": "string","format": "date-time"
     */
    private String timestamp;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<SampledValueType> getSampledValue() {
        return sampledValue;
    }

    public void setSampledValue(List<SampledValueType> sampledValue) {
        this.sampledValue = sampledValue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}

/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.MeterValueType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class MeterValues {
    
    /**
     * required
     */
    private int evseId;
    
    /**
     * required
     * "minItems": 1
     */
    private List<MeterValueType> meterValue;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public int getEvseId() {
        return evseId;
    }

    public void setEvseId(int evseId) {
        this.evseId = evseId;
    }

    public List<MeterValueType> getMeterValue() {
        return meterValue;
    }

    public void setMeterValue(List<MeterValueType> meterValue) {
        this.meterValue = meterValue;
    }

}

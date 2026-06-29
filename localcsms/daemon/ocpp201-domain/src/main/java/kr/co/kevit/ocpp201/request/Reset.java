/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.ResetEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class Reset {
    
    /**
     * required
     */
    private ResetEnumType type;
    
    private Integer evseId;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public ResetEnumType getType() {
        return type;
    }

    public void setType(ResetEnumType type) {
        this.type = type;
    }

    public Integer getEvseId() {
        return evseId;
    }

    public void setEvseId(Integer evseId) {
        this.evseId = evseId;
    }

}
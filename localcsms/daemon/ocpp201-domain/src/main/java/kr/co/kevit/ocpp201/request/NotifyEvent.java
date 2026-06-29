/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.EventDataType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class NotifyEvent {
    
    /**
     * required
     * "type": "string","format": "date-time"
     */
    private String generatedAt;
    
    private boolean tbc = false;
    
    /**
     * required
     */
    private int seqNo;
    
    /**
     * required
     * "minItems": 1
     */
    private List<EventDataType> eventData;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }

    public boolean isTbc() {
        return tbc;
    }

    public void setTbc(boolean tbc) {
        this.tbc = tbc;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public List<EventDataType> getEventData() {
        return eventData;
    }

    public void setEventData(List<EventDataType> eventData) {
        this.eventData = eventData;
    }

}

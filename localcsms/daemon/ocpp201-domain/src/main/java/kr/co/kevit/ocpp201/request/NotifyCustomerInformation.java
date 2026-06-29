/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class NotifyCustomerInformation {
    
    /**
     * required
     * "type": "string","maxLength": 512
     */
    private String data;
    
    /**
     * "type": "boolean","default": false
     */
    private boolean tbc = false;
    
    /**
     * required
     * "type": "integer"
     */
    private Integer seqNo;
    
    /**
     * required
     * "type": "string","format": "date-time"
     */
    private String generatedAt;
    
    /**
     * required
     * "type": "integer"
     */
    private int requestId;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isTbc() {
        return tbc;
    }

    public void setTbc(boolean tbc) {
        this.tbc = tbc;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

}

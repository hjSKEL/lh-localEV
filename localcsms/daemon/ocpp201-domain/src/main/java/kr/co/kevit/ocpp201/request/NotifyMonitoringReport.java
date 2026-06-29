/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.MonitoringDataType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class NotifyMonitoringReport {
    
    /**
     * "minItems": 1
     */
    private List<MonitoringDataType> monitor;
    
    /**
     * required
     * "type": "integer"
     */
    private int requestId;
    
    private boolean tbc = false;
    
    /**
     * required
     */
    private int seqNo;
    
    /**
     * required
     * "type": "string","format": "date-time"
     */
    private String generatedAt;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<MonitoringDataType> getMonitor() {
        return monitor;
    }

    public void setMonitor(List<MonitoringDataType> monitor) {
        this.monitor = monitor;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
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

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }

}
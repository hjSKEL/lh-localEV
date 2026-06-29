/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class LogParametersType {
    
    /**
     * required
     * "type": "string","maxLength": 512
     */
    private String remoteLocation;
    
    /**
     * "type": "string","format": "date-time"
     */
    private String oldestTimestamp;
    
    /**
     * "type": "string","format": "date-time"
     */
    private String latestTimestamp;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getRemoteLocation() {
        return remoteLocation;
    }

    public void setRemoteLocation(String remoteLocation) {
        this.remoteLocation = remoteLocation;
    }

    public String getOldestTimestamp() {
        return oldestTimestamp;
    }

    public void setOldestTimestamp(String oldestTimestamp) {
        this.oldestTimestamp = oldestTimestamp;
    }

    public String getLatestTimestamp() {
        return latestTimestamp;
    }

    public void setLatestTimestamp(String latestTimestamp) {
        this.latestTimestamp = latestTimestamp;
    }
}

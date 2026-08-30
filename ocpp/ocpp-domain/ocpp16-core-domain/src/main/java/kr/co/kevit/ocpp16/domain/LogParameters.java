/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.domain;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 8. 17.
 */
public class LogParameters {
    
    /**
     * required
     * "maxLength": 512
     */
    private String remoteLocation;
    
    /**
     * "format": "date-time"
     */
    private String oldestTimestamp;
    
    /**
     * "format": "date-time"
     */
    private String latestTimestamp;

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
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
public class PublishFirmware {
    
    /**
     * required
     * "type": "string","maxLength": 512
     */
    private String location;
    
    /**
     * This specifies how many times Charging Station must try to download the firmware before giving up. 
     * If this field is not present, it is left to Charging Station to decide how many times it wants to retry.
     */
    private Integer retries;
    
    /**
     * required
     * "maxLength": 32
     */
    private String checksum;
    
    /**
     * required
     */
    private int requestId;
    
    /**
     * The interval in seconds after which a retry may be attempted.
     * If this field is not present, it is left to Charging Station to decide how long to wait between attempts.
     */
    private Integer retryInterval;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Integer getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
    }
}
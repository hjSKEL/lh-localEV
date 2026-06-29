/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 6. 28.
 */
public class UpdateFirmware {

    /**
     * required "format": "uri"
     */
    private String location;

    /**
     * required "format": "date-time"
     */
    private String retrieveDate;

    /**
     * "type": "number"
     */
    private Integer retries;

    /**
     * "type": "number"
     */
    private Integer retryInterval;
    
    /**
     * for KEVIT
     */
    private Integer connectorId;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRetrieveDate() {
        return retrieveDate;
    }

    public void setRetrieveDate(String retrieveDate) {
        this.retrieveDate = retrieveDate;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
    }

    /**
     * Get connectorId
     * @return connectorId
     */
    public Integer getConnectorId() {
        return connectorId;
    }

    /**
     * Set connectorId
     * @param connectorId
     */
    public void setConnectorId(Integer connectorId) {
        this.connectorId = connectorId;
    }
    
}

/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 26.
 */
public class StartTransaction {
    
    /**
     * required
     */
    private Integer connectorId;
    
    /**
     * required
     * "maxLength": 20
     */
    private String idTag;
    
    /**
     * required
     */
    private Integer meterStart;
    
    /**
     * 
     */
    private Integer reservationId;
    
    /**
     * required
     * "format": "date-time"
     */
    private String timestamp;

    public Integer getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Integer connectorId) {
        this.connectorId = connectorId;
    }

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }

    public Integer getMeterStart() {
        return meterStart;
    }

    public void setMeterStart(Integer meterStart) {
        this.meterStart = meterStart;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
}

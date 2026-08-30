/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import kr.co.kevit.ocpp16.enumtype.StatusNotificationErrorCodeEnum;
import kr.co.kevit.ocpp16.enumtype.StatusNotificationStatusEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 6. 28.
 */
public class StatusNotification {

    /**
     * required
     */
    private Integer connectorId;
    
    /**
     * required
     */
    private StatusNotificationErrorCodeEnum errorCode;
    
    /**
     * required
     */
    private StatusNotificationStatusEnum status;
    
    /**
     * "maxLength": 50
     */
    private String info;
    
    /**
     * "format": "date-time"
     */
    private String timestamp;
    
    /**
     *  "maxLength": 255
     */
    private String vendorId;
    
    /**
     * "maxLength": 50
     */
    private String vendorErrorCode;

    public Integer getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Integer connectorId) {
        this.connectorId = connectorId;
    }

    public StatusNotificationErrorCodeEnum getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(StatusNotificationErrorCodeEnum errorCode) {
        this.errorCode = errorCode;
    }

    public StatusNotificationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusNotificationStatusEnum status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorErrorCode() {
        return vendorErrorCode;
    }

    public void setVendorErrorCode(String vendorErrorCode) {
        this.vendorErrorCode = vendorErrorCode;
    }
}
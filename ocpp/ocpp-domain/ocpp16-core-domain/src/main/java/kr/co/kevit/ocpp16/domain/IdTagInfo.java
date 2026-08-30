/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.domain;

import kr.co.kevit.ocpp16.enumtype.IdTagInfoStatus;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 26.
 */
public class IdTagInfo {
    
    /**
     * 
     */
    private IdTagInfoStatus status;
    
    /**
     * "format": "date-time"
     */
    private String expiryDate;
    
    /**
     * "maxLength": 20
     */
    private String parentIdTag;

    /**
     * "maxLength": 2
     */
    private String customerBid;

    /**
     *
     */
    private String customerName;

    public IdTagInfoStatus getStatus() {
        return status;
    }

    public void setStatus(IdTagInfoStatus status) {
        this.status = status;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getParentIdTag() {
        return parentIdTag;
    }

    public void setParentIdTag(String parentIdTag) {
        this.parentIdTag = parentIdTag;
    }

    public String getCustomerBid() {
        return customerBid;
    }

    public void setCustomerBid(String customerBid) {
        this.customerBid = customerBid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.domain;

import kr.co.kevit.ocpp16.enumtype.AuthorizationStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class IdTokenInfoType {
    
    /**
     * required
     */
    private AuthorizationStatusEnumType status;
    
    /**
     * "type": "string","format": "date-time"
     */
    private String cacheExpiryDateTime;
    
    public AuthorizationStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(AuthorizationStatusEnumType status) {
        this.status = status;
    }

    public String getCacheExpiryDateTime() {
        return cacheExpiryDateTime;
    }

    public void setCacheExpiryDateTime(String cacheExpiryDateTime) {
        this.cacheExpiryDateTime = cacheExpiryDateTime;
    }

}
/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.IdTokenType;
import kr.co.kevit.ocpp201.enumtype.ConnectorEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class ReserveNow {
    
    /**
     * required
     */
    private int id;
    
    /**
     * required
     * "type": "string","format": "date-time"
     */
    private String expiryDateTime;
    
    private ConnectorEnumType connectorType;
    
    /**
     * required
     */
    private IdTokenType idToken;
    
    private Integer evseId;
    
    private IdTokenType groupIdToken;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpiryDateTime() {
        return expiryDateTime;
    }

    public void setExpiryDateTime(String expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }

    public ConnectorEnumType getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(ConnectorEnumType connectorType) {
        this.connectorType = connectorType;
    }

    public IdTokenType getIdToken() {
        return idToken;
    }

    public void setIdToken(IdTokenType idToken) {
        this.idToken = idToken;
    }

    public Integer getEvseId() {
        return evseId;
    }

    public void setEvseId(Integer evseId) {
        this.evseId = evseId;
    }

    public IdTokenType getGroupIdToken() {
        return groupIdToken;
    }

    public void setGroupIdToken(IdTokenType groupIdToken) {
        this.groupIdToken = groupIdToken;
    }
    
}
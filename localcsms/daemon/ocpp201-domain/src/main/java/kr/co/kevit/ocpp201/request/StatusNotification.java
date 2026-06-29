/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.ConnectorStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class StatusNotification {
    
    /**
     * required
     * "description": "The time for which the status is reported. If absent time of receipt of the message will be assumed.\r\n",
     * "type": "string","format": "date-time"
     */
    private String timestamp;
    
    /**
     * required
     */
    private ConnectorStatusEnumType connectorStatus;
    
    /**
     * required
     * "description": "The id of the EVSE to which the connector belongs for which the the status is reported.\r\n",
     */
    private int evseId;
    
    /**
     * required
     * "description": "The id of the connector within the EVSE for which the status is reported.\r\n",
     */
    private int connectorId;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ConnectorStatusEnumType getConnectorStatus() {
        return connectorStatus;
    }

    public void setConnectorStatus(ConnectorStatusEnumType connectorStatus) {
        this.connectorStatus = connectorStatus;
    }

    public int getEvseId() {
        return evseId;
    }

    public void setEvseId(int evseId) {
        this.evseId = evseId;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(int connectorId) {
        this.connectorId = connectorId;
    }

}

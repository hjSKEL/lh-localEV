/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.store;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 7. 27.
 */
public class Action{
    private String id;
    private String name;
    private String connectorId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Get connectorId
     * @return connectorId
     */
    public String getConnectorId() {
        return connectorId;
    }
    /**
     * Set connectorId
     * @param connectorId
     */
    public void setConnectorId(String connectorId) {
        this.connectorId = connectorId;
    }
    
}
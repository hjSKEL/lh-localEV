/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import kr.co.kevit.ocpp16.enumtype.ChangeAvailabilityType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class ChangeAvailability {
    
    /**
     * required
     */
    private Integer connectorId;
    
    /**
     * required
     */
    private ChangeAvailabilityType type;

    public Integer getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Integer connectorId) {
        this.connectorId = connectorId;
    }

    public ChangeAvailabilityType getType() {
        return type;
    }

    public void setType(ChangeAvailabilityType type) {
        this.type = type;
    }

}

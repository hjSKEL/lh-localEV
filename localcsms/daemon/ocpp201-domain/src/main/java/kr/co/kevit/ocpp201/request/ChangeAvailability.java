/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.EVSEType;
import kr.co.kevit.ocpp201.enumtype.OperationalStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class ChangeAvailability {
    
    /**
     * 
     */
    private EVSEType evse;
    
    /**
     * required
     */
    private OperationalStatusEnumType operationalStatus;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public EVSEType getEvse() {
        return evse;
    }

    public void setEvse(EVSEType evse) {
        this.evse = evse;
    }

    public OperationalStatusEnumType getOperationalStatus() {
        return operationalStatus;
    }

    public void setOperationalStatus(OperationalStatusEnumType operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

}

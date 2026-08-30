/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import kr.co.kevit.ocpp16.enumtype.ChargingProfilePurposeEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class ClearChargingProfile {
    
    private Integer id;
    
    private Integer connectorId;
    
    private Integer stackLevel;
    
    private ChargingProfilePurposeEnum chargingProfilePurpose;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Integer connectorId) {
        this.connectorId = connectorId;
    }

    public Integer getStackLevel() {
        return stackLevel;
    }

    public void setStackLevel(Integer stackLevel) {
        this.stackLevel = stackLevel;
    }

    public ChargingProfilePurposeEnum getChargingProfilePurpose() {
        return chargingProfilePurpose;
    }

    public void setChargingProfilePurpose(ChargingProfilePurposeEnum chargingProfilePurpose) {
        this.chargingProfilePurpose = chargingProfilePurpose;
    }

}
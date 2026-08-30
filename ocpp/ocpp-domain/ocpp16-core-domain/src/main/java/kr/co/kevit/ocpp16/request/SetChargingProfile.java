/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import kr.co.kevit.ocpp16.domain.ChargingProfile;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class SetChargingProfile {
    
    /**
     * required
     */
    private Integer connectorId;
    
    /**
     * required
     */
    private ChargingProfile csChargingProfiles;

    public Integer getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Integer connectorId) {
        this.connectorId = connectorId;
    }

    public ChargingProfile getCsChargingProfiles() {
        return csChargingProfiles;
    }

    public void setCsChargingProfiles(ChargingProfile csChargingProfiles) {
        this.csChargingProfiles = csChargingProfiles;
    }

}

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
public class RemoteStartTransaction {

    /*
     * required
     * maxLength": 20
     * */
    private String idTag;
    
    private Integer connectorId;
    
    private ChargingProfile chargingProfile;

    private SetChargeLimit chargeLimit;

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }

    public Integer getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Integer connectorId) {
        this.connectorId = connectorId;
    }

    public ChargingProfile getChargingProfile() {
        return chargingProfile;
    }

    public void setChargingProfile(ChargingProfile chargingProfile) {
        this.chargingProfile = chargingProfile;
    }

    public SetChargeLimit getChargeLimit() {
        return chargeLimit;
    }

    public void setChargeLimit(SetChargeLimit chargeLimit) {
        this.chargeLimit = chargeLimit;
    }
}

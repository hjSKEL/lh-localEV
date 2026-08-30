/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import kr.co.kevit.ocpp16.enumtype.ChargingRateUnitEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class GetCompositeSchedule {
    
    /**
     * required
     */
    private Integer connectorId;

    /**
     * required
     */
    private Integer duration;
    
    private ChargingRateUnitEnum chargingRateUnit;

    public Integer getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Integer connectorId) {
        this.connectorId = connectorId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ChargingRateUnitEnum getChargingRateUnit() {
        return chargingRateUnit;
    }

    public void setChargingRateUnit(ChargingRateUnitEnum chargingRateUnit) {
        this.chargingRateUnit = chargingRateUnit;
    }
}
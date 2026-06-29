/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.response;

import kr.co.kevit.ocpp16.domain.ChargingSchedule;
import kr.co.kevit.ocpp16.enumtype.ARStatusEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class GetCompositeSchedule {

    /**
     * required
     */
    private ARStatusEnum status;
    
    private Integer connectorId;
    
    /**
     * "format": "date-time"
     */
    private String scheduleStart;
    
    private ChargingSchedule chargingSchedule;

    public ARStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ARStatusEnum status) {
        this.status = status;
    }

    public Integer getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Integer connectorId) {
        this.connectorId = connectorId;
    }

    public String getScheduleStart() {
        return scheduleStart;
    }

    public void setScheduleStart(String scheduleStart) {
        this.scheduleStart = scheduleStart;
    }

    public ChargingSchedule getChargingSchedule() {
        return chargingSchedule;
    }

    public void setChargingSchedule(ChargingSchedule chargingSchedule) {
        this.chargingSchedule = chargingSchedule;
    }
    
}

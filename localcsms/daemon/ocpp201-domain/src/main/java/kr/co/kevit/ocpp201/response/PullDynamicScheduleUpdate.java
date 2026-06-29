/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import kr.co.kevit.ocpp201.domain.*;
import kr.co.kevit.ocpp201.enumtype.ChargingProfileStatusEnumType;

import java.util.Map;

/**
 * (2.1)
 */
public class PullDynamicScheduleUpdate {

    /**
     * required
     */
    private ChargingProfileStatusEnumType status;

    private ChargingScheduleUpdateType scheduleUpdate;

    private StatusInfoType statusInfo;

    private Map<String, Object> customData;

    public ChargingProfileStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(ChargingProfileStatusEnumType status) {
        this.status = status;
    }

    public ChargingScheduleUpdateType getScheduleUpdate() {
        return scheduleUpdate;
    }

    public void setScheduleUpdate(ChargingScheduleUpdateType scheduleUpdate) {
        this.scheduleUpdate = scheduleUpdate;
    }

    public StatusInfoType getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(StatusInfoType statusInfo) {
        this.statusInfo = statusInfo;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}

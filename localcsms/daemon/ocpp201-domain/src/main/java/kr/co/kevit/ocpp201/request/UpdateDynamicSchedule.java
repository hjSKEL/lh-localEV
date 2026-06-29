/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import kr.co.kevit.ocpp201.domain.ChargingScheduleUpdateType;

import java.util.Map;

/**
 * Id of dynamic charging profile to update.
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 12. 20.
 */
public class UpdateDynamicSchedule {
    
    /**
     * required
     * Id of charging profile to update
     */
    private int chargingProfileId;
    
    /**
     * required
     */
    private ChargingScheduleUpdateType scheduleUpdate;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public int getChargingProfileId() {
        return chargingProfileId;
    }

    public void setChargingProfileId(int chargingProfileId) {
        this.chargingProfileId = chargingProfileId;
    }

    public ChargingScheduleUpdateType getScheduleUpdate() {
        return scheduleUpdate;
    }

    public void setScheduleUpdate(ChargingScheduleUpdateType scheduleUpdate) {
        this.scheduleUpdate = scheduleUpdate;
    }
}

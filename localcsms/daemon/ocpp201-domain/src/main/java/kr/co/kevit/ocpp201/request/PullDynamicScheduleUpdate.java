/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

/**
 * (2.1)
 */
public class PullDynamicScheduleUpdate {

    /**
     * required
     */
    private int chargingProfileId;

    private Map<String, Object> customData;

    public int getChargingProfileId() {
        return chargingProfileId;
    }

    public void setChargingProfileId(int chargingProfileId) {
        this.chargingProfileId = chargingProfileId;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}

/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import kr.co.kevit.ocpp201.domain.ChargingNeedsType;
import kr.co.kevit.ocpp201.domain.CustomDataType;

import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class NotifyEVChargingNeeds {


    /**
     *
     */
    private Map<String, Object> customData;

    /**
     *  Contains the maximum schedule tuples the car supports per SASchedule (both Pmax and Tariff).
     */
    private Integer maxScheduleTuples;

    /**
     * required
     */
    private ChargingNeedsType chargingNeeds;

    /**
     * Defines the EVSE and connector to which the EV is connected. EvseId may not be 0.
     * required
     */
    private Integer evseId;

    /**
     * *(2.1)* Time when EV charging needs were received. +\r\nField can be added when charging station was offline when charging needs were received.
     */
    private String timestamp;



    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Integer getMaxScheduleTuples() {
        return maxScheduleTuples;
    }

    public void setMaxScheduleTuples(Integer maxScheduleTuples) {
        this.maxScheduleTuples = maxScheduleTuples;
    }

    public ChargingNeedsType getChargingNeeds() {
        return chargingNeeds;
    }

    public void setChargingNeeds(ChargingNeedsType chargingNeeds) {
        this.chargingNeeds = chargingNeeds;
    }

    public Integer getEvseId() {
        return evseId;
    }

    public void setEvseId(Integer evseId) {
        this.evseId = evseId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

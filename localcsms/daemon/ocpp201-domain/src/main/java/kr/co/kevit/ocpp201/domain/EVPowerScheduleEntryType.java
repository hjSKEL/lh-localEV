/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * *(2.1)* An entry in schedule of the energy amount over time that EV is willing to discharge.
 * A negative value indicates the willingness to discharge under specific conditions,
 * a positive value indicates that the EV currently is not able to offer energy to discharge.
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public class EVPowerScheduleEntryType {
    //
    private Map<String, Object> customData;

    /**
     * required
     *   The duration of this entry.
     */
    private Integer duration;

    /**
     * required
     * Defines maximum amount of power for the duration of this EVPowerScheduleEntry to be discharged from the EV battery through EVSE power outlet.
     * Negative values are used for discharging.
     */
    private int power;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}

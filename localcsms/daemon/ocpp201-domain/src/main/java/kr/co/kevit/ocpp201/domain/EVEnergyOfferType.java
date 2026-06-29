/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * *(2.1)* A schedule of the energy amount over time that EV is willing to discharge.
 * A negative value indicates the willingness to discharge under specific conditions,
 * a positive value indicates that the EV currently is not able to offer energy to discharge.
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public class EVEnergyOfferType {
    //
    private Map<String, Object> customData;

    private EVAbsolutePriceScheduleType evAbsolutePriceSchedule;

    /**
     * required
     */
    private EVPowerScheduleType evPowerSchedule;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public EVAbsolutePriceScheduleType getEvAbsolutePriceSchedule() {
        return evAbsolutePriceSchedule;
    }

    public void setEvAbsolutePriceSchedule(EVAbsolutePriceScheduleType evAbsolutePriceSchedule) {
        this.evAbsolutePriceSchedule = evAbsolutePriceSchedule;
    }

    public EVPowerScheduleType getEvPowerSchedule() {
        return evPowerSchedule;
    }

    public void setEvPowerSchedule(EVPowerScheduleType evPowerSchedule) {
        this.evPowerSchedule = evPowerSchedule;
    }
}

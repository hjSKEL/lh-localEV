/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

/**
 * *(2.1)* Schedule of EV energy offer.
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public class EVPowerScheduleType {
    //
    private Map<String, Object> customData;

    /**
     * required
     *   "minItems": 1,
     *   "maxItems": 1024
     */
    private List<EVPowerScheduleEntryType> evPowerScheduleEntries;

    /**
     * required
     */
    private String timeAnchor;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<EVPowerScheduleEntryType> getEvPowerScheduleEntries() {
        return evPowerScheduleEntries;
    }

    public void setEvPowerScheduleEntries(List<EVPowerScheduleEntryType> evPowerScheduleEntries) {
        this.evPowerScheduleEntries = evPowerScheduleEntries;
    }

    public String getTimeAnchor() {
        return timeAnchor;
    }

    public void setTimeAnchor(String timeAnchor) {
        this.timeAnchor = timeAnchor;
    }
}

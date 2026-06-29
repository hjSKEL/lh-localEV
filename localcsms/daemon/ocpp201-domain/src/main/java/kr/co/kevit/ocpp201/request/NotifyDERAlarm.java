/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import kr.co.kevit.ocpp201.enumtype.DERControlEnumType;
import kr.co.kevit.ocpp201.enumtype.GridEventFaultEnumType;

import java.util.Map;

/**
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public class NotifyDERAlarm {
    //
    private Map<String, Object> customData;

    /**
     * required
     */
    private DERControlEnumType controlType;

    private GridEventFaultEnumType gridEventFault;

    /**
     * True when error condition has ended.
     * Absent or false when alarm has started.
     */
    private boolean alarmEnded;

    /**
     * required
     */
    private String timestamp;

    /**
     * required
     */
    private String extraInfo;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public DERControlEnumType getControlType() {
        return controlType;
    }

    public void setControlType(DERControlEnumType controlType) {
        this.controlType = controlType;
    }

    public GridEventFaultEnumType getGridEventFault() {
        return gridEventFault;
    }

    public void setGridEventFault(GridEventFaultEnumType gridEventFault) {
        this.gridEventFault = gridEventFault;
    }

    public boolean isAlarmEnded() {
        return alarmEnded;
    }

    public void setAlarmEnded(boolean alarmEnded) {
        this.alarmEnded = alarmEnded;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}

/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 11. 18.
 */
public class NotifyDERStartStop {


    /**
     *
     */
    private Map<String, Object> customData;


    /**
     * required
     * Id of DER control, e.g. FreqDroop
     * "maxLength": 36
     */
    private String controlId;

    /**
     * required
     * True if DER control has started. False if it has ended.
     */
    private boolean started;

    /**
     * required
     * Time of start or end of event.
     */
    private String timestamp;

    /**
     * List of controlIds that are superseded as a result of this control starting.
     *  "minItems": 1,
     *  "maxItems": 24
     *
     *  items
     *  "maxLength": 36
     */
    private List<String> supersededIds;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getSupersededIds() {
        return supersededIds;
    }

    public void setSupersededIds(List<String> supersededIds) {
        this.supersededIds = supersededIds;
    }
}

/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.StreamDataElementType;

/**
 * (2.1)
 */
public class NotifyPeriodicEventStream {

    /**
     * required
     */
    private int id;

    /**
     * required
     */
    private int pending;

    /**
     * required
     */
    private String basetime;

    /**
     * required
     */
    private List<StreamDataElementType> data;

    private Map<String, Object> customData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public String getBasetime() {
        return basetime;
    }

    public void setBasetime(String basetime) {
        this.basetime = basetime;
    }

    public List<StreamDataElementType> getData() {
        return data;
    }

    public void setData(List<StreamDataElementType> data) {
        this.data = data;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}

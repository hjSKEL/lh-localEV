/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.PeriodicEventStreamParamsType;

/**
 * (2.1)
 */
public class AdjustPeriodicEventStream {

    /**
     * required
     */
    private int id;

    /**
     * required
     */
    private PeriodicEventStreamParamsType params;

    private Map<String, Object> customData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PeriodicEventStreamParamsType getParams() {
        return params;
    }

    public void setParams(PeriodicEventStreamParamsType params) {
        this.params = params;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}

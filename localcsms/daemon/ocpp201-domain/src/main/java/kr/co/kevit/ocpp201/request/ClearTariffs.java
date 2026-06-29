/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

/**
 * (2.1)
 */
public class ClearTariffs {

    private List<String> tariffIds;

    private Integer evseId;

    private Map<String, Object> customData;

    public List<String> getTariffIds() {
        return tariffIds;
    }

    public void setTariffIds(List<String> tariffIds) {
        this.tariffIds = tariffIds;
    }

    public Integer getEvseId() {
        return evseId;
    }

    public void setEvseId(Integer evseId) {
        this.evseId = evseId;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}

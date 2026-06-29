/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.TariffClearStatusEnumType;

/**
 * (2.1)
 */
public class ClearTariffsResultType {

    /**
     * required
     */
    private TariffClearStatusEnumType status;

    private String tariffId;

    private StatusInfoType statusInfo;

    private Map<String, Object> customData;

    public TariffClearStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(TariffClearStatusEnumType status) {
        this.status = status;
    }

    public String getTariffId() {
        return tariffId;
    }

    public void setTariffId(String tariffId) {
        this.tariffId = tariffId;
    }

    public StatusInfoType getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(StatusInfoType statusInfo) {
        this.statusInfo = statusInfo;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}

/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import kr.co.kevit.ocpp201.domain.*;
import kr.co.kevit.ocpp201.enumtype.NotifyAllowedEnergyTransferStatusEnumType;

import java.util.Map;

/**
 * (2.1)
 */
public class NotifyAllowedEnergyTransfer {

    /**
     * required
     */
    private NotifyAllowedEnergyTransferStatusEnumType status;

    private StatusInfoType statusInfo;

    private Map<String, Object> customData;

    public NotifyAllowedEnergyTransferStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(NotifyAllowedEnergyTransferStatusEnumType status) {
        this.status = status;
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

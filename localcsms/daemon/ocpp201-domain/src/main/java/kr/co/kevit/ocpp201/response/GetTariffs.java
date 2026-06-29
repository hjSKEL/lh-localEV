/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import kr.co.kevit.ocpp201.domain.*;
import kr.co.kevit.ocpp201.enumtype.TariffGetStatusEnumType;

import java.util.List;
import java.util.Map;

/**
 * (2.1)
 */
public class GetTariffs {

    /**
     * required
     */
    private TariffGetStatusEnumType status;

    private StatusInfoType statusInfo;

    private List<TariffAssignmentType> tariffAssignments;

    private Map<String, Object> customData;

    public TariffGetStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(TariffGetStatusEnumType status) {
        this.status = status;
    }

    public StatusInfoType getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(StatusInfoType statusInfo) {
        this.statusInfo = statusInfo;
    }

    public List<TariffAssignmentType> getTariffAssignments() {
        return tariffAssignments;
    }

    public void setTariffAssignments(List<TariffAssignmentType> tariffAssignments) {
        this.tariffAssignments = tariffAssignments;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}

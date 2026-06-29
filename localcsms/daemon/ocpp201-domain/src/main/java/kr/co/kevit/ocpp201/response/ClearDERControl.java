/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import kr.co.kevit.ocpp201.domain.*;
import kr.co.kevit.ocpp201.enumtype.DERControlStatusEnumType;

import java.util.List;
import java.util.Map;

/**
 *
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 11. 18.
 */
public class ClearDERControl {
    //
    private Map<String, Object> customData;

    /**
     * required
     */
    private DERControlStatusEnumType status;

    /**
     *
     */
    private StatusInfoType statusInfo;


    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public DERControlStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(DERControlStatusEnumType status) {
        this.status = status;
    }

    public StatusInfoType getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(StatusInfoType statusInfo) {
        this.statusInfo = statusInfo;
    }
}

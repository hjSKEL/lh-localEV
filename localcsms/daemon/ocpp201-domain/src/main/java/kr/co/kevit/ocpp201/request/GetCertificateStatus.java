/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.OCSPRequestDataType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class GetCertificateStatus {
    
    /**
     * required
     */
    private OCSPRequestDataType ocspRequestData;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public OCSPRequestDataType getOcspRequestData() {
        return ocspRequestData;
    }

    public void setOcspRequestData(OCSPRequestDataType ocspRequestData) {
        this.ocspRequestData = ocspRequestData;
    }

}

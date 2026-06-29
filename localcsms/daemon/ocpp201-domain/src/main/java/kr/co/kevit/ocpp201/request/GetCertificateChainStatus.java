/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.CertificateStatusRequestInfoType;

/**
 * (2.1)
 */
public class GetCertificateChainStatus {

    /**
     * required
     */
    private List<CertificateStatusRequestInfoType> certificateStatusRequests;

    private Map<String, Object> customData;

    public List<CertificateStatusRequestInfoType> getCertificateStatusRequests() {
        return certificateStatusRequests;
    }

    public void setCertificateStatusRequests(List<CertificateStatusRequestInfoType> certificateStatusRequests) {
        this.certificateStatusRequests = certificateStatusRequests;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}

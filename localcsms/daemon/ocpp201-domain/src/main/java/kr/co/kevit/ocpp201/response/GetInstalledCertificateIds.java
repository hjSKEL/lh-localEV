/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.CertificateHashDataChainType;
import kr.co.kevit.ocpp201.domain.StatusInfoType;
import kr.co.kevit.ocpp201.enumtype.GetInstalledCertificateStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class GetInstalledCertificateIds {
    
    /**
     * "minItems": 1
     */
    private List<CertificateHashDataChainType> certificateHashDataChain;
    
    /**
     * required
     */
    private GetInstalledCertificateStatusEnumType status;
    
    private StatusInfoType statusInfo;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    /**
     * Get certificateHashDataChain
     * @return certificateHashDataChain
     */
    public List<CertificateHashDataChainType> getCertificateHashDataChain() {
        return certificateHashDataChain;
    }

    /**
     * Set certificateHashDataChain
     * @param certificateHashDataChain
     */
    public void setCertificateHashDataChain(List<CertificateHashDataChainType> certificateHashDataChain) {
        this.certificateHashDataChain = certificateHashDataChain;
    }

    /**
     * Get status
     * @return status
     */
    public GetInstalledCertificateStatusEnumType getStatus() {
        return status;
    }

    /**
     * Set status
     * @param status
     */
    public void setStatus(GetInstalledCertificateStatusEnumType status) {
        this.status = status;
    }

    /**
     * Get statusInfo
     * @return statusInfo
     */
    public StatusInfoType getStatusInfo() {
        return statusInfo;
    }

    /**
     * Set statusInfo
     * @param statusInfo
     */
    public void setStatusInfo(StatusInfoType statusInfo) {
        this.statusInfo = statusInfo;
    }

}

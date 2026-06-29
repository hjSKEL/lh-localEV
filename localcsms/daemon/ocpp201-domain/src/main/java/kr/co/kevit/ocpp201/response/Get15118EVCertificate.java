/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.StatusInfoType;
import kr.co.kevit.ocpp201.enumtype.Iso15118EVCertificateStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class Get15118EVCertificate {
    
    /**
     * required
     */
    private Iso15118EVCertificateStatusEnumType status;
    
    /**
     * 
     */
    private StatusInfoType statusInfo;
    
    /**
     * required
     * "type": "string","maxLength": 5600
     */
    private String exiResponse;

    /**
     * (2.1)
     */
    private Integer remainingContracts;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    /**
     * Get status
     * @return status
     */
    public Iso15118EVCertificateStatusEnumType getStatus() {
        return status;
    }

    /**
     * Set status
     * @param status
     */
    public void setStatus(Iso15118EVCertificateStatusEnumType status) {
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

    /**
     * Get exiResponse
     * @return exiResponse
     */
    public String getExiResponse() {
        return exiResponse;
    }

    /**
     * Set exiResponse
     * @param exiResponse
     */
    public void setExiResponse(String exiResponse) {
        this.exiResponse = exiResponse;
    }

    public Integer getRemainingContracts() {
        return remainingContracts;
    }

    public void setRemainingContracts(Integer remainingContracts) {
        this.remainingContracts = remainingContracts;
    }
}

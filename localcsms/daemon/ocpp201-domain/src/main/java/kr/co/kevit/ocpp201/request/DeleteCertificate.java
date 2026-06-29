/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.CertificateHashDataType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class DeleteCertificate{
    
    /**
     * required
     */
    private CertificateHashDataType certificateHashData;

    public CertificateHashDataType getCertificateHashData() {
        return certificateHashData;
    }

    public void setCertificateHashData(CertificateHashDataType certificateHashData) {
        this.certificateHashData = certificateHashData;
    }

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}

/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.InstallCertificateUseEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class InstallCertificate {
    
    /**
     * required
     */
    private InstallCertificateUseEnumType certificateType;
    
    /**
     * required
     * A PEM encoded X.509 certificate.
     * "type": "string","maxLength": 5500
     */
    private String certificate;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public InstallCertificateUseEnumType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(InstallCertificateUseEnumType certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

}
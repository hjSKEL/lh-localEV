/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.CertificateStatusSourceEnumType;

/**
 * (2.1)
 */
public class CertificateStatusRequestInfoType {

    /**
     * required
     */
    private CertificateStatusSourceEnumType source;

    /**
     * required
     */
    private List<String> urls;

    /**
     * required
     */
    private CertificateHashDataType certificateHashData;

    private Map<String, Object> customData;

    public CertificateStatusSourceEnumType getSource() {
        return source;
    }

    public void setSource(CertificateStatusSourceEnumType source) {
        this.source = source;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public CertificateHashDataType getCertificateHashData() {
        return certificateHashData;
    }

    public void setCertificateHashData(CertificateHashDataType certificateHashData) {
        this.certificateHashData = certificateHashData;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}

/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.CertificateChainStatusEnumType;
import kr.co.kevit.ocpp201.enumtype.CertificateStatusSourceEnumType;

/**
 * (2.1)
 */
public class CertificateStatusResultType {

    /**
     * required
     */
    private CertificateStatusSourceEnumType source;

    /**
     * required
     */
    private CertificateChainStatusEnumType status;

    /**
     * required
     */
    private String nextUpdate;

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

    public CertificateChainStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(CertificateChainStatusEnumType status) {
        this.status = status;
    }

    public String getNextUpdate() {
        return nextUpdate;
    }

    public void setNextUpdate(String nextUpdate) {
        this.nextUpdate = nextUpdate;
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

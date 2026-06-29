/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.CertificateSigningUseEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class CertificateSigned {
    
    /**
     * The signed PEM encoded X.509 certificate. This can also contain the necessary sub CA certificates. 
     * In that case, the order of the bundle should follow the certificate chain, starting from the leaf certificate.
     * The Configuration Variable <<configkey-max-certificate-chain-size,MaxCertificateChainSize>> can be used to limit the maximum size of this field. 
     * required
     * "maxLength": 10000
     */
    private String certificateChain;
    
    /**
     * Option
     */
    private CertificateSigningUseEnumType certificateType;

    /**
     * (2.1)
     */
    private Integer requestId;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getCertificateChain() {
        return certificateChain;
    }

    public void setCertificateChain(String certificateChain) {
        this.certificateChain = certificateChain;
    }

    public CertificateSigningUseEnumType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateSigningUseEnumType certificateType) {
        this.certificateType = certificateType;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

}
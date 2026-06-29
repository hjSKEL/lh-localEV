/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.CertificateHashDataType;
import kr.co.kevit.ocpp201.enumtype.CertificateSigningUseEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class SignCertificate {
    
    /**
     * required
     * "description": "The Charging Station SHALL send the public key in form of a Certificate Signing Request (CSR) as described in RFC 2986 [22] using the &lt;&lt;signcertificaterequeset,SignCertificateRequest&gt;&gt; message.",
     * "maxLength": 5500
     */
    private String csr;
    
    /**
     * 
     */
    private CertificateSigningUseEnumType certificateType;

    /**
     * (2.1)
     */
    private CertificateHashDataType hashRootCertificate;

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

    public String getCsr() {
        return csr;
    }

    public void setCsr(String csr) {
        this.csr = csr;
    }

    public CertificateSigningUseEnumType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateSigningUseEnumType certificateType) {
        this.certificateType = certificateType;
    }

    public CertificateHashDataType getHashRootCertificate() {
        return hashRootCertificate;
    }

    public void setHashRootCertificate(CertificateHashDataType hashRootCertificate) {
        this.hashRootCertificate = hashRootCertificate;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

}

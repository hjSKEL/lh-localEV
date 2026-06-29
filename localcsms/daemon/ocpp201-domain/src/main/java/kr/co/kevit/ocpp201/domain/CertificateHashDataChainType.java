/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.GetCertificateIdUseEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class CertificateHashDataChainType {
    
    /**
     * required
     */
    private CertificateHashDataType certificateHashData;
    
    /**
     * required
     */
    private GetCertificateIdUseEnumType certificateType;
    
    /**
     * "minItems": 1,"maxItems": 4
     */
    private List<CertificateHashDataType> childCertificateHashData;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public CertificateHashDataType getCertificateHashData() {
        return certificateHashData;
    }

    public void setCertificateHashData(CertificateHashDataType certificateHashData) {
        this.certificateHashData = certificateHashData;
    }

    public GetCertificateIdUseEnumType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(GetCertificateIdUseEnumType certificateType) {
        this.certificateType = certificateType;
    }

    public List<CertificateHashDataType> getChildCertificateHashData() {
        return childCertificateHashData;
    }

    public void setChildCertificateHashData(List<CertificateHashDataType> childCertificateHashData) {
        this.childCertificateHashData = childCertificateHashData;
    }
    
}

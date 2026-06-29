/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.IdTokenType;
import kr.co.kevit.ocpp201.domain.OCSPRequestDataType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class Authorize {
    
    /**
     * required
     */
    private IdTokenType idToken;
    
    /**
     * The X.509 certificated presented by EV and encoded in PEM format
     * maxLength": 5500
     */
    private String certificate;
    
    /**
     *  "minItems": 1,"maxItems": 4 
     */
    private List<OCSPRequestDataType> iso15118CertificateHashData;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public IdTokenType getIdToken() {
        return idToken;
    }

    public void setIdToken(IdTokenType idToken) {
        this.idToken = idToken;
    }

    public List<OCSPRequestDataType> getIso15118CertificateHashData() {
        return iso15118CertificateHashData;
    }

    public void setIso15118CertificateHashData(List<OCSPRequestDataType> iso15118CertificateHashData) {
        this.iso15118CertificateHashData = iso15118CertificateHashData;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }
}

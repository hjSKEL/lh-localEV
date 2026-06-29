/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.request;

import java.util.List;

import kr.co.kevit.ocpp16.pnc.domain.IdTokenType;
import kr.co.kevit.ocpp16.pnc.domain.OCSPRequestDataType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class Authorize {
    
    /**
     * The X.509 certificated presented by EV and encoded in PEM format
     * maxLength": 5500
     */
    private String certificate;
    
    /**
     * required
     */
    private IdTokenType idToken;
    
    /**
     *  "minItems": 1,"maxItems": 4 
     */
    private List<OCSPRequestDataType> iso15118CertificateHashData;

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

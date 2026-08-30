/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.request;

import kr.co.kevit.ocpp16.enumtype.CertificateActionEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class Get15118EVCertificate {
    
    /**
     * required
     * "type": "string","maxLength": 50
     */
    private String iso15118SchemaVersion;
    
    /**
     * required
     */
    private CertificateActionEnumType action;
    
    /**
     * required
     * "type": "string","maxLength": 5600
     */
    private String exiRequest;

    public String getIso15118SchemaVersion() {
        return iso15118SchemaVersion;
    }

    public void setIso15118SchemaVersion(String iso15118SchemaVersion) {
        this.iso15118SchemaVersion = iso15118SchemaVersion;
    }

    public CertificateActionEnumType getAction() {
        return action;
    }

    public void setAction(CertificateActionEnumType action) {
        this.action = action;
    }

    public String getExiRequest() {
        return exiRequest;
    }

    public void setExiRequest(String exiRequest) {
        this.exiRequest = exiRequest;
    }

}
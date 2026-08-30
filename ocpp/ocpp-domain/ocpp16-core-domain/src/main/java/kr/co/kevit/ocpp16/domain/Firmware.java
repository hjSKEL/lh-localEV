/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.domain;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 8. 17.
 */
public class Firmware {
    
    /**
     * required
     * "maxLength": 512
     */
    private String location;
    
    /**
     * required
     * "format": "date-time"
     */
    private String retrieveDateTime;
    
    /**
     * "format": "date-time"
     */
    private String installDateTime;
    
    /**
     * required
     * "maxLength": 5500
     */
    private String signingCertificate;
    
    /**
     * required
     * "maxLength": 800
     */
    private String signature;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRetrieveDateTime() {
        return retrieveDateTime;
    }

    public void setRetrieveDateTime(String retrieveDateTime) {
        this.retrieveDateTime = retrieveDateTime;
    }

    public String getInstallDateTime() {
        return installDateTime;
    }

    public void setInstallDateTime(String installDateTime) {
        this.installDateTime = installDateTime;
    }

    public String getSigningCertificate() {
        return signingCertificate;
    }

    public void setSigningCertificate(String signingCertificate) {
        this.signingCertificate = signingCertificate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}

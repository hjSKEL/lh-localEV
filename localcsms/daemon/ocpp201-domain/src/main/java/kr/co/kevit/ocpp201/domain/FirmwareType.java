/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * Firmware\r\nurn:x-enexis:ecdm:uid:2:233291\r\nRepresents a copy of the firmware that can be loaded/updated on the Charging Station.\r\n
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 16.
 */
public class FirmwareType {
    
    /**
     * required
     * "maxLength": 512
     * "description": "Firmware. Location. URI\r\nurn:x-enexis:ecdm:uid:1:569460\r\nURI defining the origin of the firmware.\r\n",
     */
    private String location;
    
    /**
     * required
     * "description": "Firmware. Retrieve. Date_ Time\r\nurn:x-enexis:ecdm:uid:1:569461\r\nDate and time at which the firmware shall be retrieved.\r\n",
     * "type": "string","format": "date-time"
     */
    private String retrieveDateTime;
    
    /**
     * "description": "Firmware. Install. Date_ Time\r\nurn:x-enexis:ecdm:uid:1:569462\r\nDate and time at which the firmware shall be installed.\r\n",
     * "type": "string","format": "date-time"
     */
    private String installDateTime;
    
    /**
     * "description": "Certificate with which the firmware was signed.\r\nX.509 certificate, first DER encoded into binary, and then Base64 encoded.\r\n\r\n",
     * "type": "string","maxLength": 5500
     */
    private String signingCertificate;
    
    /**
     * "description": "Firmware. Signature. Signature\r\nurn:x-enexis:ecdm:uid:1:569464\r\nBase64 encoded firmware signature.\r\n",
     * "type": "string","maxLength": 800
     */
    private String signature;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

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

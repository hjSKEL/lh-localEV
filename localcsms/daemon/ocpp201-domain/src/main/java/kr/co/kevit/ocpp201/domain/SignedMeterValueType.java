/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class SignedMeterValueType {
    
    /**
     * required
     * "type": "string","maxLength": 2500
     */
    private String signedMeterData;
    
    /**
     * required
     * "type": "string","maxLength": 50
     */
    private String signingMethod;
    
    /**
     * required
     * "type": "string","maxLength": 50
     */
    private String encodingMethod;
    
    /**
     * required
     * "type": "string","maxLength": 2500
     */
    private String publicKey;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getSignedMeterData() {
        return signedMeterData;
    }

    public void setSignedMeterData(String signedMeterData) {
        this.signedMeterData = signedMeterData;
    }

    public String getSigningMethod() {
        return signingMethod;
    }

    public void setSigningMethod(String signingMethod) {
        this.signingMethod = signingMethod;
    }

    public String getEncodingMethod() {
        return encodingMethod;
    }

    public void setEncodingMethod(String encodingMethod) {
        this.encodingMethod = encodingMethod;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

}
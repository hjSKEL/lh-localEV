/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.domain;

import kr.co.kevit.ocpp16.enumtype.HashAlgorithmEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class OCSPRequestDataType {
    
    /**
     * required
     */
    private HashAlgorithmEnumType hashAlgorithm;
    
    /**
     * required
     * Hashed value of the Issuer DN (Distinguished Name).
     * maxLength": 128
     */
    private String issuerNameHash;
    
    /**
     * required
     * Hashed value of the issuers public key
     * maxLength": 128
     */
    private String issuerKeyHash;
    
    /**
     * required
     * The serial number of the certificate.
     * maxLength": 40
     */
    private String serialNumber;
    
    /**
     * required
     * This contains the responder URL (Case insensitive).
     * maxLength: 512
     */
    private String responderURL;

    public HashAlgorithmEnumType getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(HashAlgorithmEnumType hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public String getIssuerNameHash() {
        return issuerNameHash;
    }

    public void setIssuerNameHash(String issuerNameHash) {
        this.issuerNameHash = issuerNameHash;
    }

    public String getIssuerKeyHash() {
        return issuerKeyHash;
    }

    public void setIssuerKeyHash(String issuerKeyHash) {
        this.issuerKeyHash = issuerKeyHash;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getResponderURL() {
        return responderURL;
    }

    public void setResponderURL(String responderURL) {
        this.responderURL = responderURL;
    }
}
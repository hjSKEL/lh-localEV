/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.domain;

import kr.co.kevit.ocpp16.enumtype.HashAlgorithmEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 8. 17.
 */
public class CertificateHashData {
    
    /**
     * required
     */
    private HashAlgorithmEnum hashAlgorithm;
    
    /**
     * required
     * "maxLength": 128
     */
    private String issuerNameHash;
    
    /**
     * required
     * "maxLength": 128
     */
    private String issuerKeyHash;
    
    /**
     * required
     * "maxLength": 40
     */
    private String serialNumber;

    public HashAlgorithmEnum getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(HashAlgorithmEnum hashAlgorithm) {
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

}
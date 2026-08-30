/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.request;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class CertificateSigned {
    
    /**
     * The signed PEM encoded X.509 certificate. This can also contain the necessary sub CA certificates. 
     * In that case, the order of the bundle should follow the certificate chain, starting from the leaf certificate.
     * The Configuration Variable <<configkey-max-certificate-chain-size,MaxCertificateChainSize>> can be used to limit the maximum size of this field. 
     * required
     * "maxLength": 10000
     */
    private String certificateChain;
    
    public String getCertificateChain() {
        return certificateChain;
    }

    public void setCertificateChain(String certificateChain) {
        this.certificateChain = certificateChain;
    }
}
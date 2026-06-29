/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import kr.co.kevit.ocpp16.enumtype.CertificateUseEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 8. 17.
 */
public class InstallCertificate {
    
    /**
     * required
     */
    private CertificateUseEnum certificateType;
    
    /**
     * required
     * "maxLength": 5500
     */
    private String certificate;

    public CertificateUseEnum getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateUseEnum certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

}

/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.response;

import java.util.List;

import kr.co.kevit.ocpp16.domain.CertificateHashData;
import kr.co.kevit.ocpp16.enumtype.GetInstalledCertificateStatusEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 8. 17.
 */
public class GetInstalledCertificateIds {
    
    /**
     * "minItems": 1
     */
    private List<CertificateHashData> certificateHashData;
    
    /**
     * required
     */
    private GetInstalledCertificateStatusEnum status;

    public List<CertificateHashData> getCertificateHashData() {
        return certificateHashData;
    }

    public void setCertificateHashData(List<CertificateHashData> certificateHashData) {
        this.certificateHashData = certificateHashData;
    }

    public GetInstalledCertificateStatusEnum getStatus() {
        return status;
    }

    public void setStatus(GetInstalledCertificateStatusEnum status) {
        this.status = status;
    }

}

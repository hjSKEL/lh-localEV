/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.response;

import java.util.List;

import kr.co.kevit.ocpp16.enumtype.GetInstalledCertificateStatusEnumType;
import kr.co.kevit.ocpp16.pnc.domain.CertificateHashDataChainType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class GetInstalledCertificateIds {
    
    /**
     * "minItems": 1
     */
    private List<CertificateHashDataChainType> certificateHashDataChain;
    
    /**
     * required
     */
    private GetInstalledCertificateStatusEnumType status;
    
    /**
     * Get certificateHashDataChain
     * @return certificateHashDataChain
     */
    public List<CertificateHashDataChainType> getCertificateHashDataChain() {
        return certificateHashDataChain;
    }

    /**
     * Set certificateHashDataChain
     * @param certificateHashDataChain
     */
    public void setCertificateHashDataChain(List<CertificateHashDataChainType> certificateHashDataChain) {
        this.certificateHashDataChain = certificateHashDataChain;
    }

    /**
     * Get status
     * @return status
     */
    public GetInstalledCertificateStatusEnumType getStatus() {
        return status;
    }

    /**
     * Set status
     * @param status
     */
    public void setStatus(GetInstalledCertificateStatusEnumType status) {
        this.status = status;
    }

}

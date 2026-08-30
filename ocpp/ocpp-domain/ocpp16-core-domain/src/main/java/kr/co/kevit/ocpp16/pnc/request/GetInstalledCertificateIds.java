/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.request;

import java.util.List;

import kr.co.kevit.ocpp16.enumtype.GetCertificateIdUseEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class GetInstalledCertificateIds {
    
    /**
     * "minItems": 1
     */
    private List<GetCertificateIdUseEnumType> certificateType;

    public List<GetCertificateIdUseEnumType> getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(List<GetCertificateIdUseEnumType> certificateType) {
        this.certificateType = certificateType;
    }

}

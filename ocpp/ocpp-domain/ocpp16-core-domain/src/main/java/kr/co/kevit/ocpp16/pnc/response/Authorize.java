/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.response;

import kr.co.kevit.ocpp16.enumtype.AuthorizeCertificateStatusEnumType;
import kr.co.kevit.ocpp16.pnc.domain.IdTokenInfoType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class Authorize {
    
    /**
     * required
     */
    private IdTokenInfoType idTokenInfo;
    
    /**
     * 
     */
    private AuthorizeCertificateStatusEnumType certificateStatus;

    public IdTokenInfoType getIdTokenInfo() {
        return idTokenInfo;
    }

    public void setIdTokenInfo(IdTokenInfoType idTokenInfo) {
        this.idTokenInfo = idTokenInfo;
    }

    public AuthorizeCertificateStatusEnumType getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(AuthorizeCertificateStatusEnumType certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

}
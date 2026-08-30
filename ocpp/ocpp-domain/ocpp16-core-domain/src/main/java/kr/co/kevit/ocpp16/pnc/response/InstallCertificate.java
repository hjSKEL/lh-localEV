/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.response;

import kr.co.kevit.ocpp16.enumtype.InstallCertificateStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class InstallCertificate {
    
    /**
     * required
     */
    private InstallCertificateStatusEnumType status;

    /**
     * Get status
     * @return status
     */
    public InstallCertificateStatusEnumType getStatus() {
        return status;
    }

    /**
     * Set status
     * @param status
     */
    public void setStatus(InstallCertificateStatusEnumType status) {
        this.status = status;
    }
}

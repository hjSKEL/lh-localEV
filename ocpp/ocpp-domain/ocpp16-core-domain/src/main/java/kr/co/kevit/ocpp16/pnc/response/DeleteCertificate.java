/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.response;

import kr.co.kevit.ocpp16.enumtype.DeleteCertificateStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class DeleteCertificate {
    
    /**
     * required
     */
    private DeleteCertificateStatusEnumType status;
    
    /**
     * Get status
     * @return status
     */
    public DeleteCertificateStatusEnumType getStatus() {
        return status;
    }

    /**
     * Set status
     * @param status
     */
    public void setStatus(DeleteCertificateStatusEnumType status) {
        this.status = status;
    }

}
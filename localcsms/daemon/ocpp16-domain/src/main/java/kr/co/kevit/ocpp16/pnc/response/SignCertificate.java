/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.response;

import kr.co.kevit.ocpp16.enumtype.GenericStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class SignCertificate {
    
    /**
     * required
     */
    private GenericStatusEnumType status;
    
    public GenericStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(GenericStatusEnumType status) {
        this.status = status;
    }

}

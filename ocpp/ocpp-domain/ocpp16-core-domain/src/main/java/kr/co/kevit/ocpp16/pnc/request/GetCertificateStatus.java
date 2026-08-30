/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.request;

import kr.co.kevit.ocpp16.pnc.domain.OCSPRequestDataType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class GetCertificateStatus {
    
    /**
     * required
     */
    private OCSPRequestDataType ocspRequestData;

    public OCSPRequestDataType getOcspRequestData() {
        return ocspRequestData;
    }

    public void setOcspRequestData(OCSPRequestDataType ocspRequestData) {
        this.ocspRequestData = ocspRequestData;
    }

}

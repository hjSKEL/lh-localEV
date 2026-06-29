/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.response;

import kr.co.kevit.ocpp16.enumtype.Iso15118EVCertificateStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class Get15118EVCertificate {
    
    /**
     * required
     */
    private Iso15118EVCertificateStatusEnumType status;
    
    /**
     * required
     * "type": "string","maxLength": 5600
     */
    private String exiResponse;

    /**
     * Get status
     * @return status
     */
    public Iso15118EVCertificateStatusEnumType getStatus() {
        return status;
    }

    /**
     * Set status
     * @param status
     */
    public void setStatus(Iso15118EVCertificateStatusEnumType status) {
        this.status = status;
    }

    /**
     * Get exiResponse
     * @return exiResponse
     */
    public String getExiResponse() {
        return exiResponse;
    }

    /**
     * Set exiResponse
     * @param exiResponse
     */
    public void setExiResponse(String exiResponse) {
        this.exiResponse = exiResponse;
    }
}

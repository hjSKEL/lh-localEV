/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.pnc.response;

import kr.co.kevit.ocpp16.enumtype.GetCertificateStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class GetCertificateStatus {
    
    /**
     * required
     */
    private GetCertificateStatusEnumType status;
    
    /**
     * "type": "string","maxLength": 5500
     * OCSPResponse class as defined in <<ref-ocpp_security_24, IETF RFC 6960>>. 
     * DER encoded (as defined in <<ref-ocpp_security_24, IETF RFC 6960>>), and then base64 encoded. 
     * MAY only be omitted when status is not Accepted.
     */
    private String ocspResult;

    /**
     * Get status
     * @return status
     */
    public GetCertificateStatusEnumType getStatus() {
        return status;
    }

    /**
     * Set status
     * @param status
     */
    public void setStatus(GetCertificateStatusEnumType status) {
        this.status = status;
    }

    /**
     * Get ocspResult
     * @return ocspResult
     */
    public String getOcspResult() {
        return ocspResult;
    }

    /**
     * Set ocspResult
     * @param ocspResult
     */
    public void setOcspResult(String ocspResult) {
        this.ocspResult = ocspResult;
    }

}

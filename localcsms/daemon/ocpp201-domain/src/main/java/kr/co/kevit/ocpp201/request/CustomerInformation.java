/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.CertificateHashDataType;
import kr.co.kevit.ocpp201.domain.IdTokenType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class CustomerInformation {
    
    /**
     *
     */
    private CertificateHashDataType customerCertificate;

    /**
     *
     */
    private IdTokenType idToken;

    /**
     * required
     */
    private Integer requestId;

    /**
     * required
     */
    private boolean report;

    /**
     * required
     */
    private boolean clear;

    /**
     * "type": "string","maxLength": 64
     */
    private String customerIdentifier;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    /**
     * Get customerCertificate
     * @return customerCertificate
     */
    public CertificateHashDataType getCustomerCertificate() {
        return customerCertificate;
    }

    /**
     * Set customerCertificate
     * @param customerCertificate
     */
    public void setCustomerCertificate(CertificateHashDataType customerCertificate) {
        this.customerCertificate = customerCertificate;
    }

    /**
     * Get idToken
     * @return idToken
     */
    public IdTokenType getIdToken() {
        return idToken;
    }

    /**
     * Set idToken
     * @param idToken
     */
    public void setIdToken(IdTokenType idToken) {
        this.idToken = idToken;
    }

    /**
     * Get requestId
     * @return requestId
     */
    public Integer getRequestId() {
        return requestId;
    }

    /**
     * Set requestId
     * @param requestId
     */
    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    /**
     * Get report
     * @return report
     */
    public boolean isReport() {
        return report;
    }

    /**
     * Set report
     * @param report
     */
    public void setReport(boolean report) {
        this.report = report;
    }

    /**
     * Get clear
     * @return clear
     */
    public boolean isClear() {
        return clear;
    }

    /**
     * Set clear
     * @param clear
     */
    public void setClear(boolean clear) {
        this.clear = clear;
    }

    /**
     * Get customerIdentifier
     * @return customerIdentifier
     */
    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    /**
     * Set customerIdentifier
     * @param customerIdentifier
     */
    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }
}

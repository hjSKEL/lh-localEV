/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.CertificateActionEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class Get15118EVCertificate {
    
    /**
     * required
     * "type": "string","maxLength": 50
     */
    private String iso15118SchemaVersion;
    
    /**
     * required
     */
    private CertificateActionEnumType action;
    
    /**
     * required
     * "type": "string","maxLength": 5600
     */
    private String exiRequest;

    /**
     * (2.1)
     */
    private Integer maximumContractCertificateChains;

    /**
     * (2.1)
     */
    private List<String> prioritizedEMAIDs;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getIso15118SchemaVersion() {
        return iso15118SchemaVersion;
    }

    public void setIso15118SchemaVersion(String iso15118SchemaVersion) {
        this.iso15118SchemaVersion = iso15118SchemaVersion;
    }

    public CertificateActionEnumType getAction() {
        return action;
    }

    public void setAction(CertificateActionEnumType action) {
        this.action = action;
    }

    public String getExiRequest() {
        return exiRequest;
    }

    public void setExiRequest(String exiRequest) {
        this.exiRequest = exiRequest;
    }

    public Integer getMaximumContractCertificateChains() {
        return maximumContractCertificateChains;
    }

    public void setMaximumContractCertificateChains(Integer maximumContractCertificateChains) {
        this.maximumContractCertificateChains = maximumContractCertificateChains;
    }

    public List<String> getPrioritizedEMAIDs() {
        return prioritizedEMAIDs;
    }

    public void setPrioritizedEMAIDs(List<String> prioritizedEMAIDs) {
        this.prioritizedEMAIDs = prioritizedEMAIDs;
    }

}
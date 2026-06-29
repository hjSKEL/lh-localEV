/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.IdTokenEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class IdTokenType {
    
    /**
     * "minItems": 1
     */
    private List<AdditionalInfoType> additionalInfo;
    
    /**
     * required
     * "type": "string",
     * "maxLength": 36
     */
    private String idToken;
    
    /**
     * required
     */
    private IdTokenEnumType type;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<AdditionalInfoType> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(List<AdditionalInfoType> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public IdTokenEnumType getType() {
        return type;
    }

    public void setType(IdTokenEnumType type) {
        this.type = type;
    }
    
}
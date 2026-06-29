/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.AuthorizationStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class IdTokenInfoType {
    
    /**
     * required
     */
    private AuthorizationStatusEnumType status;
    
    /**
     * "type": "string","format": "date-time"
     */
    private String cacheExpiryDateTime;
    
    /**
     * "type": "integer"
     * Default priority is 0, The range is from -9 to 9. 
     * Higher values indicate a higher priority. 
     * The chargingPriority in <<transactioneventresponse,TransactionEventResponse>> overrules this one.
     */
    private Integer chargingPriority;
    
    /**
     * "type": "string","maxLength": 8
     *  Contains a language code as defined in ref-RFC5646,[RFC5646]
     */
    private String language1;
    
    /**
     * "minItems": 1
     */
    private List<Integer> evseId;
    
    /**
     * 
     */
    private IdTokenType groupIdToken;
    
    /**
     * "type": "string","maxLength": 8
     */
    private String language2;
    
    private MessageContentType personalMessage;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public AuthorizationStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(AuthorizationStatusEnumType status) {
        this.status = status;
    }

    public String getCacheExpiryDateTime() {
        return cacheExpiryDateTime;
    }

    public void setCacheExpiryDateTime(String cacheExpiryDateTime) {
        this.cacheExpiryDateTime = cacheExpiryDateTime;
    }

    public Integer getChargingPriority() {
        return chargingPriority;
    }

    public void setChargingPriority(Integer chargingPriority) {
        this.chargingPriority = chargingPriority;
    }

    public String getLanguage1() {
        return language1;
    }

    public void setLanguage1(String language1) {
        this.language1 = language1;
    }

    public List<Integer> getEvseId() {
        return evseId;
    }

    public void setEvseId(List<Integer> evseId) {
        this.evseId = evseId;
    }

    public IdTokenType getGroupIdToken() {
        return groupIdToken;
    }

    public void setGroupIdToken(IdTokenType groupIdToken) {
        this.groupIdToken = groupIdToken;
    }

    public String getLanguage2() {
        return language2;
    }

    public void setLanguage2(String language2) {
        this.language2 = language2;
    }

    public MessageContentType getPersonalMessage() {
        return personalMessage;
    }

    public void setPersonalMessage(MessageContentType personalMessage) {
        this.personalMessage = personalMessage;
    }
}
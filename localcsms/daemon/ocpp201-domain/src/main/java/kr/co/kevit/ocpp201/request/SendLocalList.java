/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.AuthorizationData;
import kr.co.kevit.ocpp201.enumtype.UpdateEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class SendLocalList {
    
    /**
     * "minItems": 1
     */
    private List<AuthorizationData> localAuthorizationList;
    
    /**
     * required
     */
    private int versionNumber;
    
    /**
     * required
     */
    private UpdateEnumType updateType;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<AuthorizationData> getLocalAuthorizationList() {
        return localAuthorizationList;
    }

    public void setLocalAuthorizationList(List<AuthorizationData> localAuthorizationList) {
        this.localAuthorizationList = localAuthorizationList;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public UpdateEnumType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateEnumType updateType) {
        this.updateType = updateType;
    }

}

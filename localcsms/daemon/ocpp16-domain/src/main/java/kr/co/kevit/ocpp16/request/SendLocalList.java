/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import java.util.List;

import kr.co.kevit.ocpp16.domain.LocalAuthorizationList;
import kr.co.kevit.ocpp16.enumtype.UpdateTypeEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class SendLocalList {
    
    /**
     * required
     */
    private Integer listVersion;
    
    /**
     * required
     */
    private UpdateTypeEnum updateType;
    
    private List<LocalAuthorizationList> localAuthorizationList;

    public Integer getListVersion() {
        return listVersion;
    }

    public void setListVersion(Integer listVersion) {
        this.listVersion = listVersion;
    }

    public UpdateTypeEnum getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateTypeEnum updateType) {
        this.updateType = updateType;
    }

    public List<LocalAuthorizationList> getLocalAuthorizationList() {
        return localAuthorizationList;
    }

    public void setLocalAuthorizationList(List<LocalAuthorizationList> localAuthorizationList) {
        this.localAuthorizationList = localAuthorizationList;
    }

}

/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 ******************************************************************************/
package kr.co.kevit.localcsms.system.entity.shared;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * RemoteLog 검색 조건
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 17.
 */
public class RemoteLogSearchCond extends PageCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private String actionName;
    private String status;
    private String ocppVersion;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOcppVersion() {
        return ocppVersion;
    }

    public void setOcppVersion(String ocppVersion) {
        this.ocppVersion = ocppVersion;
    }

}

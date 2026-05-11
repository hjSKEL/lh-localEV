/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.entity.shared;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * DaemonAccess 검색 조건
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 17.
 */
public class DaemonAccessSearchCond extends PageCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cpCsId;
    private String ip;

    public String getCpCsId() {
        return cpCsId;
    }

    public void setCpCsId(String cpCsId) {
        this.cpCsId = cpCsId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}

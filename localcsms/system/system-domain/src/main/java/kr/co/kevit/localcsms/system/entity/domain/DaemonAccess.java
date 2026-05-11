/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.entity.domain;

import java.io.Serializable;

/**
 * TB_SYDA001
 * 데몬 접속 정보
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 17.
 */
public class DaemonAccess implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 충전기 ID (PK)
     * CP_CS_ID     VARCHAR(9)    NOT NULL
     * 예시: 111111-01
     */
    private String cpCsId;

    /**
     * IP 주소
     * IP           VARCHAR(16)
     */
    private String ip;

    /**
     * 포트
     * PORT         VARCHAR(5)
     */
    private String port;

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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}

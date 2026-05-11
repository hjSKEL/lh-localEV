/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 ******************************************************************************/
package kr.co.kevit.localcsms.system.entity.domain;

import java.io.Serializable;

/**
 * TB_SYRM001
 * 원격 로그
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 17.
 */
public class RemoteLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * UUID (PK)
     * UUID          VARCHAR(36)   NOT NULL
     */
    private String uuid;

    /**
     * Action Name
     * ACT_NM        VARCHAR(30)   NOT NULL
     */
    private String actionName;

    /**
     * Request Payload
     * REQ_PLD       VARCHAR(255)  NOT NULL
     */
    private String reqPayload;

    /**
     * 상태
     * STATUS        CHAR(6)       NOT NULL
     * RMST01: 요청, RMST02: 응답, RMST03: 실패
     */
    private String status;

    /**
     * Response Payload
     * RES_PLD       VARCHAR(255)
     */
    private String resPayload;

    /**
     * OCPP 버전
     * OCPP_VER      VARCHAR(10)
     */
    private String ocppVersion;

    /**
     * 키 ID (충전기 식별자 등)
     * KEY_ID        VARCHAR(36) DEFAULT NULL
     */
    private String keyId;

    /**
     * 등록일시
     * REG_DT        DATETIME
     */
    private String regDt;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getReqPayload() {
        return reqPayload;
    }

    public void setReqPayload(String reqPayload) {
        this.reqPayload = reqPayload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResPayload() {
        return resPayload;
    }

    public void setResPayload(String resPayload) {
        this.resPayload = resPayload;
    }

    public String getOcppVersion() {
        return ocppVersion;
    }

    public void setOcppVersion(String ocppVersion) {
        this.ocppVersion = ocppVersion;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

}

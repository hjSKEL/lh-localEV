/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.domain;

import kr.co.kevit.localcsms.common.util.enumtype.ocpp.OCPPMsgDirectType;

import java.math.BigInteger;
import java.util.Date;

/**
 * TB_RCLG001
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 7. 2.
 */
public class OcppLog {
    
    /**
     * Sequence
     * SEQ AutoIncrement.
     */
    private BigInteger seq;
    
    /**
     * CP_ID CHAR(9) NOT NULL,
     * 충전소 ID
     */
    private String cpId;
    
    /**
     * CS_ID CHAR(2) NOT NULL,
     * 충전기 ID
     */
    private String csId;
    
    /**
     * 
     * MSG_DIR_TY CHAR(7) NOT NULL
     */
    private OCPPMsgDirectType directType;
    
    /**
     * ID VARCHAR(36) NOT NULL,
     * Maximum of 36 characters, to allow for GUIDs
     */
    private String id;
    
    /**
     * MSG_TY_ID CHAR(1) NOT NULL,
     * MessageTypeId
     * 2 : 요청
     * 3 : 응답
     * 4 : 응답 에러
     */
    private String messageTypeId;
    
    /**
     * OCPP_VER VARCHAR(10) NOT NULL,
     * 1.6, 2.0
     * OCPP Version.
     */
    private String ocppVer;
    
    /**
     * ACT_NM VARCHAR(40) NOT NULL,
     * Action name
     */
    private String action;
    
    /**
     * PAYLOAD VARCHAR(1000),
     * 내용
     */
    private String payloadJson;
    
    /**
     * REG_DT DATETIME
     * 서버 수집 시간
     */
    private Date regDate;

    /**
     * Get seq
     * @return seq
     */
    public BigInteger getSeq() {
        return seq;
    }

    /**
     * Set seq
     * @param seq
     */
    public void setSeq(BigInteger seq) {
        this.seq = seq;
    }

    /**
     * Get cpId
     * @return cpId
     */
    public String getCpId() {
        return cpId;
    }

    /**
     * Set cpId
     * @param cpId
     */
    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    /**
     * Get csId
     * @return csId
     */
    public String getCsId() {
        return csId;
    }

    /**
     * Set csId
     * @param csId
     */
    public void setCsId(String csId) {
        this.csId = csId;
    }

    /**
     * Get directType
     * @return directType
     */
    public OCPPMsgDirectType getDirectType() {
        return directType;
    }

    /**
     * Set directType
     * @param directType
     */
    public void setDirectType(OCPPMsgDirectType directType) {
        this.directType = directType;
    }

    /**
     * Get id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Set id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get messageTypeId
     * @return messageTypeId
     */
    public String getMessageTypeId() {
        return messageTypeId;
    }

    /**
     * Set messageTypeId
     * @param messageTypeId
     */
    public void setMessageTypeId(String messageTypeId) {
        this.messageTypeId = messageTypeId;
    }

    /**
     * Get ocppVer
     * @return ocppVer
     */
    public String getOcppVer() {
        return ocppVer;
    }

    /**
     * Set ocppVer
     * @param ocppVer
     */
    public void setOcppVer(String ocppVer) {
        this.ocppVer = ocppVer;
    }

    /**
     * Get action
     * @return action
     */
    public String getAction() {
        return action;
    }

    /**
     * Set action
     * @param action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Get payloadJson
     * @return payloadJson
     */
    public String getPayloadJson() {
        return payloadJson;
    }

    /**
     * Set payloadJson
     * @param payloadJson
     */
    public void setPayloadJson(String payloadJson) {
        this.payloadJson = payloadJson;
    }

    /**
     * Get regDate
     * @return regDate
     */
    public Date getRegDate() {
        return regDate;
    }

    /**
     * Set regDate
     * @param regDate
     */
    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
}

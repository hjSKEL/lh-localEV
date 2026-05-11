/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;
import java.io.Serializable;
import java.util.Date;

/**
 * TB_RCRS001
 * 
 * @since 2026. 03. 09.
 */
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 예약 ID (RSV_ID)
     */
    private Long rsvId;

    /**
     * 충전소 ID (CP_ID)
     */
    private String cpId;

    /**
     * 충전기 ID (CS_ID)
     */
    private String csId;

    /**
     * 만료 일시 (EXP_DT)
     */
    private Date expiredDate;

    /**
     * 인증 카드 번호 (CUT_CRD_NO)
     */
    private String cutCardNo;

    /**
     * 부모 카드 번호 (PRNT_CRD_NO)
     */
    private String parentCardNo;

    /**
     * 상태 (STATUS)
     * RSVT01: 예약, RSVT02: 예약취소
     */
    private String status;

    /**
     * 등록/수정 정보
     */
    private Writer writer;

    public Long getRsvId() {
        return rsvId;
    }

    public void setRsvId(Long rsvId) {
        this.rsvId = rsvId;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getCsId() {
        return csId;
    }

    public void setCsId(String csId) {
        this.csId = csId;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getCutCardNo() {
        return cutCardNo;
    }

    public void setCutCardNo(String cutCardNo) {
        this.cutCardNo = cutCardNo;
    }

    public String getParentCardNo() {
        return parentCardNo;
    }

    public void setParentCardNo(String parentCardNo) {
        this.parentCardNo = parentCardNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }
}

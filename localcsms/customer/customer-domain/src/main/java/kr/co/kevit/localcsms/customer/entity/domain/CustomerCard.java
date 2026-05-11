/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.domain;

import java.io.Serializable;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * 회원 카드
 * 
 * TB : TB_CUCA001
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 8. 31.
 */
public class CustomerCard implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -231138431921364081L;
    
    /**
     * PK
     * 고객카드번호(티머니)
     * CUT_CRD_NO   VARCHAR2(16 BYTE)                    NOT NULL,
     * 
     */
    private String cutCardNo;
    
    /**
     * 고객아이디
     * CUT_ID   CHAR(9 BYTE),
     */
    private String customerId;
    
    /**
     * 고객카드상태코드
     * 공통코드 : MEML00
     * 미사용/발송/수령/분실/삭제_불량/재발급요청
     * CUT_STAT_CD   VARCHAR2(6 BYTE)                     DEFAULT 'MEML01'              NOT NULL,
     */
    private String custStatCode;
    
    /**
     * 분실자
     * LOS_ID   CHAR(9 BYTE),
     */
    private String lossId;
    
    /**
     * 분실일
     * LOS_DT  DATE,
     */
    private Date lossDate;
    
    /**
     * 삭제자
     * DEL_ID    CHAR(9 BYTE),
     */
    private String deleteId;
    
    /**
     * 삭제일
     * DEL_DT   DATE,
     */
    private Date delDate;
    
    /**
     * 등록정보
     */
    private Writer writer;

    /**
     * Get cutCardNo
     * @return cutCardNo
     */
    public String getCutCardNo() {
        return cutCardNo;
    }

    /**
     * Set cutCardNo
     * @param cutCardNo
     */
    public void setCutCardNo(String cutCardNo) {
        this.cutCardNo = cutCardNo;
    }

    /**
     * Get customerId
     * @return customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Set customerId
     * @param customerId
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Get custStatCode
     * @return custStatCode
     */
    public String getCustStatCode() {
        return custStatCode;
    }

    /**
     * Set custStatCode
     * @param custStatCode
     */
    public void setCustStatCode(String custStatCode) {
        this.custStatCode = custStatCode;
    }

    /**
     * Get lossId
     * @return lossId
     */
    public String getLossId() {
        return lossId;
    }

    /**
     * Set lossId
     * @param lossId
     */
    public void setLossId(String lossId) {
        this.lossId = lossId;
    }

    /**
     * Get lossDate
     * @return lossDate
     */
    public Date getLossDate() {
        return lossDate;
    }

    /**
     * Set lossDate
     * @param lossDate
     */
    public void setLossDate(Date lossDate) {
        this.lossDate = lossDate;
    }

    /**
     * Get deleteId
     * @return deleteId
     */
    public String getDeleteId() {
        return deleteId;
    }

    /**
     * Set deleteId
     * @param deleteId
     */
    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    /**
     * Get delDate
     * @return delDate
     */
    public Date getDelDate() {
        return delDate;
    }

    /**
     * Set delDate
     * @param delDate
     */
    public void setDelDate(Date delDate) {
        this.delDate = delDate;
    }

    /**
     * Get writer
     * @return writer
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * Set writer
     * @param writer
     */
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

}

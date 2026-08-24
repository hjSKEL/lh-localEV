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
     * 분실여부
     * LOS_YN   CHAR(1 BYTE)                    DEFAULT 'N'              NOT NULL,
     */
    private String lossYn;

    /**
     * 분실일
     * LOS_DT  DATE,
     */
    private Date lossDate;

    /**
     * 정지여부
     * STOP_YN   CHAR(1 BYTE)                    DEFAULT 'N'              NOT NULL,
     */
    private String stopYn;

    /**
     * 정지일
     * STOP_DT   DATE,
     */
    private Date stopDate;
    
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
     * Get lossYn
     * @return lossYn
     */
    public String getLossYn() {
        return lossYn;
    }

    /**
     * Set lossYn
     * @param lossYn
     */
    public void setLossYn(String lossYn) {
        this.lossYn = lossYn;
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
     * Get stopYn
     * @return stopYn
     */
    public String getStopYn() {
        return stopYn;
    }

    /**
     * Set stopYn
     * @param stopYn
     */
    public void setStopYn(String stopYn) {
        this.stopYn = stopYn;
    }

    /**
     * Get stopDate
     * @return stopDate
     */
    public Date getStopDate() {
        return stopDate;
    }

    /**
     * Set stopDate
     * @param stopDate
     */
    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
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

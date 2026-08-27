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
     * 정지사유코드 (LOSS/USER_REQ/UNPAID/ETC)
     * STOP_RSN_CD   VARCHAR(20),
     */
    private String stopRsnCd;

    /**
     * 정지사유 기타 직접입력 (stopRsnCd=ETC일 때만)
     * STOP_RSN_TXT   VARCHAR(200),
     */
    private String stopRsnTxt;
    
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
     * Get stopRsnCd
     * @return stopRsnCd
     */
    public String getStopRsnCd() {
        return stopRsnCd;
    }

    /**
     * Set stopRsnCd
     * @param stopRsnCd
     */
    public void setStopRsnCd(String stopRsnCd) {
        this.stopRsnCd = stopRsnCd;
    }

    /**
     * Get stopRsnTxt
     * @return stopRsnTxt
     */
    public String getStopRsnTxt() {
        return stopRsnTxt;
    }

    /**
     * Set stopRsnTxt
     * @param stopRsnTxt
     */
    public void setStopRsnTxt(String stopRsnTxt) {
        this.stopRsnTxt = stopRsnTxt;
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

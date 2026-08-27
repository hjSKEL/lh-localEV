/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.domain;

import java.io.Serializable;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.string.StringUtils;

/**
 * 회원 TB_CUCU001
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */
public class Customer implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 5308323459182070925L;

    /**
     * PK
     * 사용자아이디 CUT_ID CHAR(9 BYTE) NOT NULL,
     */
    private String customerId;

    /**
     * 고객명 CUT_NM VARCHAR2(20 BYTE) NOT NULL,
     */
    private String custName;

    /**
     * 휴대폰 번호 MBL_PHN_NO VARCHAR2(60 BYTE) NOT NULL,
     */
    private String mblPhoneNo;

    /**
     * E-Mail EMAIL VARCHAR2(200 BYTE),
     */
    private String email;

    /**
     * 거주지 동 DONG VARCHAR(20)
     */
    private String dong;

    /**
     * 거주지 호 HO VARCHAR(20)
     */
    private String ho;

    /**
     * 단지아이디 `CX_ID` CHAR(9)
     */
    private String complexId;

    /**
     * 단지명 (TB_ORCX001 조인 조회 전용, 저장 안 함)
     */
    private String complexName;

    /**
     * 고객 삭제여부 DEL_YN CHAR(1) DEFAULT 'N' NOT NULL
     */
    private String delYn;

    /**
     * 고객 삭제일 DEL_DT DATETIME
     */
    private Date delDate;

    /**
     * 고객 정지여부 STOP_YN CHAR(1) DEFAULT 'N' NOT NULL
     */
    private String stopYn;

    /**
     * 고객 정지일 STOP_DT DATETIME
     */
    private Date stopDate;

    private CustomerMgt customerMgt;

    private Writer writer;

    public Customer() {
    }

    public Customer(String custName, String mblPhoneNo) {
        this.custName = custName;
        this.mblPhoneNo = mblPhoneNo;
    }

    /**
     * Get customerId
     * 
     * @return customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Set customerId
     * 
     * @param customerId
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Get custName
     * 
     * @return custName
     */
    public String getCustName() {
        return custName;
    }

    /**
     * Set custName
     * 
     * @param custName
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     * Get mblPhoneNo
     * 
     * @return mblPhoneNo
     */
    public String getMblPhoneNo() {
        return mblPhoneNo;
    }

    /**
     * Set mblPhoneNo
     * 
     * @param mblPhoneNo
     */
    public void setMblPhoneNo(String mblPhoneNo) {
        this.mblPhoneNo = mblPhoneNo;
    }

    /**
     * Get email
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set email
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get dong
     *
     * @return dong
     */
    public String getDong() {
        return dong;
    }

    /**
     * Set dong
     *
     * @param dong
     */
    public void setDong(String dong) {
        this.dong = dong;
    }

    /**
     * Get ho
     *
     * @return ho
     */
    public String getHo() {
        return ho;
    }

    /**
     * Set ho
     *
     * @param ho
     */
    public void setHo(String ho) {
        this.ho = ho;
    }

    /**
     * Get complexId
     *
     * @return complexId
     */
    public String getComplexId() {
        return complexId;
    }

    /**
     * Set complexId
     *
     * @param complexId
     */
    public void setComplexId(String complexId) {
        this.complexId = complexId;
    }

    /**
     * Get complexName
     *
     * @return complexName
     */
    public String getComplexName() {
        return complexName;
    }

    /**
     * Set complexName
     *
     * @param complexName
     */
    public void setComplexName(String complexName) {
        this.complexName = complexName;
    }

    /**
     * Get delYn
     *
     * @return delYn
     */
    public String getDelYn() {
        return delYn;
    }

    /**
     * Set delYn
     *
     * @param delYn
     */
    public void setDelYn(String delYn) {
        this.delYn = delYn;
    }

    /**
     * Get delDate
     *
     * @return delDate
     */
    public Date getDelDate() {
        return delDate;
    }

    /**
     * Set delDate
     *
     * @param delDate
     */
    public void setDelDate(Date delDate) {
        this.delDate = delDate;
    }

    /**
     * Get stopYn
     *
     * @return stopYn
     */
    public String getStopYn() {
        return stopYn;
    }

    /**
     * Set stopYn
     *
     * @param stopYn
     */
    public void setStopYn(String stopYn) {
        this.stopYn = stopYn;
    }

    /**
     * Get stopDate
     *
     * @return stopDate
     */
    public Date getStopDate() {
        return stopDate;
    }

    /**
     * Set stopDate
     *
     * @param stopDate
     */
    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    /**
     * Get customerMgt
     *
     * @return customerMgt
     */
    public CustomerMgt getCustomerMgt() {
        return customerMgt;
    }

    /**
     * Set customerMgt
     * 
     * @param customerMgt
     */
    public void setCustomerMgt(CustomerMgt customerMgt) {
        this.customerMgt = customerMgt;
    }

    /**
     * Get writer
     * 
     * @return writer
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * Set writer
     * 
     * @param writer
     */
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public void makeCustomerId(String maxCustomerId) {
        String seqStr = StringUtils.leftPadding(String.valueOf(StringUtils.isEmpty(maxCustomerId) ? 1 : maxCustomerId),
                '0', 8);
        this.setCustomerId("C" + seqStr);
    }

}

/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * TB_CUCU002
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */
public class CustomerMgt implements Serializable {

    /**  */
    private static final long serialVersionUID = -171607476222929156L;

    /**
     * PK
     * 고객카드번호
     * CUT_CRD_NO VARCHAR2(16 BYTE),
     */
    private String cutCardNo;

    /**
     * 고객ID
     * CUT_ID CHAR(9 BYTE) NOT NULL,
     */
    private String customerId;

    /**
     * 고객관리코드
     * 공통코드 : MEMK00
     * 정상/관심/불량/우수
     * CUT_MNG_CD VARCHAR2(6 BYTE),
     */
    private String cutManageCode = "MEMK01";

    /**
     * 고객등급코드
     * 공통코드 : MEMB00
     * 정회원/준회원
     * CUT_GRD_CD VARCHAR2(6 BYTE) DEFAULT 'MEMB02' NOT NULL,
     */
    private String cutGrdCode = "MEMB02";

    /**
     * 정지여부
     * STOP_YN CHAR(1 BYTE) DEFAULT 'N',
     */
    private String stopYn = "N";

    /**
     * 정지일
     * STOP_DT DATE,
     */
    private Date stopDate;

    /**
     * 정회원인증일
     * REG_CERT_DT DATE,
     */
    private Date regCertDate;

    /**
     * 태그유형
     * 공통코드 : TGTP01 ISO15693 / TGTP02 ISO14443 / TGTP03 KeyCode
     * TAG_TP VARCHAR(6),
     */
    private String tagType;

    /**
     * 부모 카드번호
     * PRNT_CRD_NO VARCHAR2(16 BYTE),
     */
    private String parentCardNo;

    /**
     * 등록일
     * REG_DT
     *
     */
    private Date registrationDate;

    /**
     * 수정일
     * UPD_DT
     *
     */
    private Date updateDate;

    /**
     * Get cutCardNo
     * 
     * @return cutCardNo
     */
    public String getCutCardNo() {
        return cutCardNo;
    }

    /**
     * Set cutCardNo
     * 
     * @param cutCardNo
     */
    public void setCutCardNo(String cutCardNo) {
        this.cutCardNo = cutCardNo;
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

    public String getCutManageCode() {
        return cutManageCode;
    }

    public void setCutManageCode(String cutManageCode) {
        this.cutManageCode = cutManageCode;
    }

    public String getCutGrdCode() {
        return cutGrdCode;
    }

    public void setCutGrdCode(String cutGrdCode) {
        this.cutGrdCode = cutGrdCode;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getParentCardNo() {
        return parentCardNo;
    }

    public void setParentCardNo(String parentCardNo) {
        this.parentCardNo = parentCardNo;
    }

    /**
     * 삭제여부
     * DEL_YN CHAR(1 BYTE) DEFAULT 'N',
     */
    private String deleteYn = "N";

    /**
     * 삭제일
     * DEL_DT DATE,
     */
    private Date deleteDate;

    public String getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
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
     * Get regCertDate
     * 
     * @return regCertDate
     */
    public Date getRegCertDate() {
        return regCertDate;
    }

    /**
     * Set regCertDate
     * 
     * @param regCertDate
     */
    public void setRegCertDate(Date regCertDate) {
        this.regCertDate = regCertDate;
    }

    /**
     * Get registrationDate
     * 
     * @return registrationDate
     */
    public Date getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Set registrationDate
     * 
     * @param registrationDate
     */
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Get updateDate
     * 
     * @return updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * Set updateDate
     * 
     * @param updateDate
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}

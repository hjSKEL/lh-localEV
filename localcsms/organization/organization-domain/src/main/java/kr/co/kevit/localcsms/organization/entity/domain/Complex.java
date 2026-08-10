/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.domain;

import java.io.Serializable;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;

/**
 * TB_ORCX001
 * 단지 (아파트 단지 등 LocalCSMS 설치 단위)
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 8. 10.
 */
public class Complex implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 단지아이디
     * CX_ID       CHAR(9 BYTE)                     NOT NULL,
     */
    private String complexId;

    /**
     * 단지명
     * CX_NM       VARCHAR2(60 BYTE)                NOT NULL,
     */
    private String complexName;

    /**
     * 단지주소
     * CX_ADDR     VARCHAR2(200 BYTE),
     */
    private String complexAddr;

    /**
     * 관리법인아이디
     * CO_ID       CHAR(9 BYTE)                     NOT NULL,
     */
    private String companyId;

    /**
     * 단지사용가능여부
     * CX_US_YN    CHAR(1 BYTE)                     DEFAULT 'Y'    NOT NULL,
     */
    private String complexUseYn = StringConstants.Y;

    /**
     * 삭제여부
     * DEL_YN      CHAR(1 BYTE)                     DEFAULT 'N'    NOT NULL,
     */
    private String deleteYn = StringConstants.N;

    /**
     * 삭제일
     * DEL_DT      DATE,
     */
    private Date deleteDate;

    /**
     * 등록정보
     */
    private Writer writer;

    private String memo;

    /**
     * Get complexId
     * @return complexId
     */
    public String getComplexId() {
        return complexId;
    }

    /**
     * Set complexId
     * @param complexId
     */
    public void setComplexId(String complexId) {
        this.complexId = complexId;
    }

    /**
     * Get complexName
     * @return complexName
     */
    public String getComplexName() {
        return complexName;
    }

    /**
     * Set complexName
     * @param complexName
     */
    public void setComplexName(String complexName) {
        this.complexName = complexName;
    }

    /**
     * Get complexAddr
     * @return complexAddr
     */
    public String getComplexAddr() {
        return complexAddr;
    }

    /**
     * Set complexAddr
     * @param complexAddr
     */
    public void setComplexAddr(String complexAddr) {
        this.complexAddr = complexAddr;
    }

    /**
     * Get companyId
     * @return companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * Set companyId
     * @param companyId
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * Get complexUseYn
     * @return complexUseYn
     */
    public String getComplexUseYn() {
        return complexUseYn;
    }

    /**
     * Set complexUseYn
     * @param complexUseYn
     */
    public void setComplexUseYn(String complexUseYn) {
        this.complexUseYn = complexUseYn;
    }

    /**
     * Get deleteYn
     * @return deleteYn
     */
    public String getDeleteYn() {
        return deleteYn;
    }

    /**
     * Set deleteYn
     * @param deleteYn
     */
    public void setDeleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
    }

    /**
     * Get deleteDate
     * @return deleteDate
     */
    public Date getDeleteDate() {
        return deleteDate;
    }

    /**
     * Set deleteDate
     * @param deleteDate
     */
    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
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

    /**
     * Get memo
     * @return memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * Set memo
     * @param memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 단지아이디 생성 ("CX" + 7자리 seq)
     * @param maxComplexId selectMaxComplexId() 결과
     */
    public void makeComplexId(String maxComplexId) {
        String seqStr = StringUtils.leftPadding(String.valueOf(StringUtils.isEmpty(maxComplexId) ? 1 : maxComplexId), '0', 7);
        this.setComplexId("CX" + seqStr);
    }

}

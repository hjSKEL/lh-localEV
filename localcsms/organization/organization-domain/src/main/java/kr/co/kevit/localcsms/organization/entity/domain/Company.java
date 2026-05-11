/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeDto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 4. 11.
 */
public class Company implements Serializable{

    /**  */
    private static final long serialVersionUID = 4990332987712514782L;

    /**
     * 회사아이디
     * CO_ID       CHAR(9)                 NOT NULL,
     */
    private String companyId;

    /**
     * 회사명
     * CO_NM       VARCHAR(60)                NOT NULL,
     */
    private String companyName;

    /**
     * 사업자번호
     * BIZ_REG_NO       CHAR(10)              NOT NULL,
     */
    private String bizRegNo;

    /**
     * 대표자명
     * CEO_NM       VARCHAR(20)           NOT NULL,
     */
    private String ceoName;

    /**
     * 우편번호
     * ZIP_CD       VARCHAR(6)
     */
    private String zipCode;

    /**
     * 도로명
     * RD_NM       VARCHAR(100)          NOT NULL,
     */
    private String roadName;

    /**
     * 도로명상세주소
     * RD_DET_NM       VARCHAR(50)
     */
    private String roadDetName;

    /**
     * 회사전화번호
     * RD_DET_NM       VARCHAR(11)
     */
    private String coPhoneNo;

    /**
     * 팩스번호
     * FAX_NO       VARCHAR(11)
     */
    private String faxNo;

    /**
     * 제조사 여부
     * MAKER_YN       CHAR(1)
     */
    private String makerYn;

    /**
     * 소유사 여부
     * POSS_YN       CHAR(1)
     */
    private String possessionYn;

    /**
     * 고객수
     * CUT_CNT       INT(5)
     */
    private int custCount;

    /**
     * 등록정보
     */
    private Writer writer;

    private List<CompanyInfoItem> infoItems;
    
    private List<EmployeeDto> employees;

    public List<CompanyInfoItem> getInfoItems() {
        return infoItems;
    }

    public void setInfoItems(List<CompanyInfoItem> infoItems) {
        this.infoItems = infoItems;
    }

    public List<EmployeeDto> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDto> employees) {
        this.employees = employees;
    }
    
    public String getInfoItemValue(String infoItemId) {
        if(this.infoItems != null && !this.infoItems.isEmpty()) {
            for(CompanyInfoItem item : this.infoItems) {
                if(item.getInfoItemId().equals(infoItemId)) {
                    return item.getInfoItemValue();
                }
            }
        }
        return StringConstants.BLANK;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBizRegNo() {
        return bizRegNo;
    }

    public void setBizRegNo(String bizRegNo) {
        this.bizRegNo = bizRegNo;
    }

    public String getCeoName() {
        return ceoName;
    }

    public void setCeoName(String ceoName) {
        this.ceoName = ceoName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getRoadDetName() {
        return roadDetName;
    }

    public void setRoadDetName(String roadDetName) {
        this.roadDetName = roadDetName;
    }

    public String getCoPhoneNo() {
        return coPhoneNo;
    }

    public void setCoPhoneNo(String coPhoneNo) {
        this.coPhoneNo = coPhoneNo;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    public String getMakerYn() {
        return makerYn;
    }

    public void setMakerYn(String makerYn) {
        this.makerYn = makerYn;
    }

    public String getPossessionYn() {
        return possessionYn;
    }

    public void setPossessionYn(String possessionYn) {
        this.possessionYn = possessionYn;
    }

    public int getCustCount() {
        return custCount;
    }

    public void setCustCount(int custCount) {
        this.custCount = custCount;
    }


    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public void makeCompanyId(String maxCompanyId) {
        String cpSeqStr = StringUtils.leftPadding(String.valueOf(StringUtils.isEmpty(maxCompanyId) ? 1 : maxCompanyId), '0', 7);
        this.setCompanyId("CO" + cpSeqStr);
    }
}

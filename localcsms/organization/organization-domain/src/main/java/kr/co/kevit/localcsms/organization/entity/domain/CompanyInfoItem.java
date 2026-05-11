/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.domain;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 4. 11.
 */
public class CompanyInfoItem {

    /**
     * 회사아이디
     * CO_ID       CHAR(9)                 NOT NULL,
     */
    private String companyId;

    /**
     * 정보항목아이디
     * ITEM_ID       CHAR(6)                 NOT NULL,
     */
    private String infoItemId;

    /**
     * 정보항목값
     * VALUE       VARCHAR(20)           NOT NULL,
     */
    private String infoItemValue;

    public CompanyInfoItem() {
    }

    public CompanyInfoItem(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getInfoItemId() {
        return infoItemId;
    }

    public void setInfoItemId(String infoItemId) {
        this.infoItemId = infoItemId;
    }

    public String getInfoItemValue() {
        return infoItemValue;
    }

    public void setInfoItemValue(String infoItemValue) {
        this.infoItemValue = infoItemValue;
    }
}

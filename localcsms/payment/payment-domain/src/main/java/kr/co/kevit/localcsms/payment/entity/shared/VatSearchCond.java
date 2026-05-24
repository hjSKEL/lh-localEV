/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.shared;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * VAT 검색 조건
 *
 * @author bckim
 * @since 2026. 5. 24.
 */
public class VatSearchCond extends PageCriteria implements Serializable {

    private static final long serialVersionUID = 4810314410723102292L;

    /** 사업자번호 */
    private String vatNo;
    /** 회사명 (LIKE) */
    private String companyNm;
    /** 사용여부 Y/N */
    private String useYn;

    public String getVatNo() { return vatNo; }
    public void setVatNo(String vatNo) { this.vatNo = vatNo; }

    public String getCompanyNm() { return companyNm; }
    public void setCompanyNm(String companyNm) { this.companyNm = companyNm; }

    public String getUseYn() { return useYn; }
    public void setUseYn(String useYn) { this.useYn = useYn; }
}

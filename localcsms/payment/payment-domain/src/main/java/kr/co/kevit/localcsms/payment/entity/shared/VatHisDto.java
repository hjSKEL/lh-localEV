/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.shared;

import kr.co.kevit.localcsms.payment.entity.domain.VatHis;

/**
 * VAT 검증 이력 DTO — 회사명 조인
 *
 * @author bckim
 * @since 2026. 5. 24.
 */
public class VatHisDto extends VatHis {
    private static final long serialVersionUID = -7488510031120442209L;

    /** 회사명 (TB_PAVAT01 조인) */
    private String companyNm;

    public String getCompanyNm() { return companyNm; }
    public void setCompanyNm(String companyNm) { this.companyNm = companyNm; }
}

/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.domain;

import java.io.Serializable;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * 사업자(VAT) 마스터.
 *
 * <p>OCPP 2.1 VatNumberValidation 메시지의 company(AddressType) 응답 원천 데이터.</p>
 *
 * TB : TB_PAVAT01
 *
 * @author bckim
 * @since 2026. 5. 24.
 */
public class Vat implements Serializable {

    private static final long serialVersionUID = 4187522001841052201L;

    /** 사용여부 'Y' */
    public static final String USE_Y = "Y";
    /** 사용여부 'N' */
    public static final String USE_N = "N";

    /** PK — 사업자등록번호 (한국 10자리, OCPP 20자 한도) */
    private String vatNo;
    /** 상호/회사명 (응답.company.name) */
    private String companyNm;
    /** 대표자명 */
    private String repNm;
    /** 주소1 (응답.company.address1) */
    private String addr1;
    /** 주소2 */
    private String addr2;
    /** 시 (응답.company.city) */
    private String city;
    /** 우편번호 (응답.company.postalCode) */
    private String postalCd;
    /** 국가 (응답.company.country) */
    private String country;
    /** 업태 */
    private String bizType;
    /** 종목 */
    private String bizItem;
    /** 사용여부 Y/N */
    private String useYn = USE_Y;
    /** 마지막 외부 검증 시각 */
    private Date lastVerifiedDt;
    /** 검증 출처: MANUAL / NTS_API / VIES */
    private String verifySource;
    /** 등록정보 */
    private Writer writer;

    public String getVatNo() { return vatNo; }
    public void setVatNo(String vatNo) { this.vatNo = vatNo; }

    public String getCompanyNm() { return companyNm; }
    public void setCompanyNm(String companyNm) { this.companyNm = companyNm; }

    public String getRepNm() { return repNm; }
    public void setRepNm(String repNm) { this.repNm = repNm; }

    public String getAddr1() { return addr1; }
    public void setAddr1(String addr1) { this.addr1 = addr1; }

    public String getAddr2() { return addr2; }
    public void setAddr2(String addr2) { this.addr2 = addr2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostalCd() { return postalCd; }
    public void setPostalCd(String postalCd) { this.postalCd = postalCd; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getBizType() { return bizType; }
    public void setBizType(String bizType) { this.bizType = bizType; }

    public String getBizItem() { return bizItem; }
    public void setBizItem(String bizItem) { this.bizItem = bizItem; }

    public String getUseYn() { return useYn; }
    public void setUseYn(String useYn) { this.useYn = useYn; }

    public Date getLastVerifiedDt() { return lastVerifiedDt; }
    public void setLastVerifiedDt(Date lastVerifiedDt) { this.lastVerifiedDt = lastVerifiedDt; }

    public String getVerifySource() { return verifySource; }
    public void setVerifySource(String verifySource) { this.verifySource = verifySource; }

    public Writer getWriter() { return writer; }
    public void setWriter(Writer writer) { this.writer = writer; }
}

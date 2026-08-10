/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.domain;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.domain.CarModel;
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
     * 거주지 동/호 DONG_HO VARCHAR(20)
     */
    private String dongHo;

    /**
     * 법인아이디 `CO_ID` CHAR(9)
     */
    private String companyId;

    /**
     * 단지아이디 `CX_ID` CHAR(9)
     */
    private String complexId;

    /**
     * 단지명 (TB_ORCX001 조인 조회 전용, 저장 안 함)
     */
    private String complexName;

    /**
     * 차량번호 CAR_NO VARCHAR(50) ,
     */
    private String carNumber;

    private CarModel carModel;

    /**
     * 차량 명 CAR_NM
     */
    private String carName;

    /**
     * V2X 서비스 가입 여부 (Y/N). OCPP 2.1 AuthorizeResponse.allowedEnergyTransfer 정책 적용.
     */
    private String v2xContractYn;

    /**
     * 계약상 허용 energy transfer modes (CSV).
     * 예: "AC_three_phase,AC_BPT,DC_BPT".
     * NULL/빈 값 → AuthorizeResponse 에서 omit (= 단방향 default).
     */
    private String allowedEnergyTransfer;

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
     * Get dongHo
     *
     * @return dongHo
     */
    public String getDongHo() {
        return dongHo;
    }

    /**
     * Set dongHo
     *
     * @param dongHo
     */
    public void setDongHo(String dongHo) {
        this.dongHo = dongHo;
    }

    /**
     * Get companyId
     *
     * @return companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * Set companyId
     * 
     * @param companyId
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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
     * Get carNumber
     *
     * @return carNumber
     */
    public String getCarNumber() {
        return carNumber;
    }

    /**
     * Set carNumber
     * 
     * @param carNumber
     */
    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    /**
     * Get carModel
     * 
     * @return carModel
     */
    public CarModel getCarModel() {
        return carModel;
    }

    /**
     * Set carModel
     * 
     * @param carModel
     */
    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    /**
     * Get carName
     * 
     * @return carName
     */
    public String getCarName() {
        return carName;
    }

    /**
     * Set carName
     * 
     * @param carName
     */
    public void setCarName(String carName) {
        this.carName = carName;
    }

    /**
     * Get customerMgt
     * 
     * @return customerMgt
     */
    public String getV2xContractYn() { return v2xContractYn; }
    public void setV2xContractYn(String v2xContractYn) { this.v2xContractYn = v2xContractYn; }

    public String getAllowedEnergyTransfer() { return allowedEnergyTransfer; }
    public void setAllowedEnergyTransfer(String allowedEnergyTransfer) { this.allowedEnergyTransfer = allowedEnergyTransfer; }

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

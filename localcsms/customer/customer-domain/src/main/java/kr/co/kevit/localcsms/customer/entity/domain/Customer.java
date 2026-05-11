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
     * 법인아이디 `CO_ID` CHAR(9)
     */
    private String companyId;

    /**
     * 차량번호 CAR_NO VARCHAR(50) ,
     */
    private String carNumber;

    private CarModel carModel;

    /**
     * 차량 명 CAR_NM
     */
    private String carName;

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

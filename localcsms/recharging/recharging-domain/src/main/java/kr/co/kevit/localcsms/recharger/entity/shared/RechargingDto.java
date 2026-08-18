/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.shared;

import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 17.
 */
public class RechargingDto extends Recharging {

    /**  */
    private static final long serialVersionUID = 2959923845149330355L;

    private String cpName;
    private String carMoelName;

    /**
     * 단지명 (TB_ORCX001 조인 조회 전용, 저장 안 함)
     */
    private String complexName;

    private String chStartDateStr;
    private String chEndDateStr;

    private String cardNo;
    
    /* 고객 정보 */
    private String companyName;
    private String custName;
    private String mblPhoneNo;

    /**
     * 동 (TB_CUCU001 조인 조회 전용, 저장 안 함)
     */
    private String dong;

    /**
     * 호 (TB_CUCU001 조인 조회 전용, 저장 안 함)
     */
    private String ho;


    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getCarMoelName() {
        return carMoelName;
    }

    public void setCarMoelName(String carMoelName) {
        this.carMoelName = carMoelName;
    }

    public String getComplexName() {
        return complexName;
    }

    public void setComplexName(String complexName) {
        this.complexName = complexName;
    }

    public String getChStartDateStr() {
        return chStartDateStr;
    }

    public void setChStartDateStr(String chStartDateStr) {
        this.chStartDateStr = chStartDateStr;
    }

    public String getChEndDateStr() {
        return chEndDateStr;
    }

    public void setChEndDateStr(String chEndDateStr) {
        this.chEndDateStr = chEndDateStr;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * Get companyName
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Set companyName
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Get custName
     * @return custName
     */
    public String getCustName() {
        return custName;
    }

    /**
     * Set custName
     * @param custName
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     * Get mblPhoneNo
     * @return mblPhoneNo
     */
    public String getMblPhoneNo() {
        return mblPhoneNo;
    }

    /**
     * Set mblPhoneNo
     * @param mblPhoneNo
     */
    public void setMblPhoneNo(String mblPhoneNo) {
        this.mblPhoneNo = mblPhoneNo;
    }

    public String getDong() {
        return dong;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

}

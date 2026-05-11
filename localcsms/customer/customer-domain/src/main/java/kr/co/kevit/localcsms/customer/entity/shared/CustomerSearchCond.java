/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.shared;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 8. 31.
 */
public class CustomerSearchCond extends PageCriteria implements Serializable{

    /**  */
    private static final long serialVersionUID = 7184214587268757972L;

    /**
     * 怨좉컼紐?
     */
    private String custName;

    /**
     * 怨좉컼?꾩씠??
     */
    private String customerId;

    /**
     * ?대??곕쾲??
     */
    private String mblPhoneNo;

    /**
     * ?뚯썝踰덊샇(?뚯썝移대뱶 踰덊샇)
     * MEMNUM        VARCHAR2(16 BYTE),
     */
    private String cutCardNo;
    
    /**
     * 硫ㅻ쾭?깃툒
     * 怨듯넻肄붾뱶 : MEMB00
     * MEMGRD        VARCHAR2(6 BYTE)     DEFAULT 'MEMB02'              NOT NULL,
     */
    private String cutGrdCode;
    
    /**
     * ?뺣젹
     * 00 : 媛?낆씪 ASC
     * 01 : 媛?낆씪 DESC
     * 10 : 怨좉컼紐?ASC
     * 11 : 怨좉컼紐?DESC
     */
    private String order = StringConstants.ZERO2;

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
     * Get customerId
     * @return customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Set customerId
     * @param customerId
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    /**
     * Get cutCardNo
     * @return cutCardNo
     */
    public String getCutCardNo() {
        return cutCardNo;
    }

    /**
     * Set cutCardNo
     * @param cutCardNo
     */
    public void setCutCardNo(String cutCardNo) {
        this.cutCardNo = cutCardNo;
    }

    /**
     * Get cutGrdCode
     * @return cutGrdCode
     */
    public String getCutGrdCode() {
        return cutGrdCode;
    }

    /**
     * Set cutGrdCode
     * @param cutGrdCode
     */
    public void setCutGrdCode(String cutGrdCode) {
        this.cutGrdCode = cutGrdCode;
    }

    /**
     * Get order
     * @return order
     */
    public String getOrder() {
        return order;
    }

    /**
     * Set order
     * @param order
     */
    public void setOrder(String order) {
        this.order = order;
    }
}

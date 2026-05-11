/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

import java.io.Serializable;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 10.
 */
public class CustomerCardSearchCond extends PageCriteria implements Serializable{
    
    /**  */
    private static final long serialVersionUID = 1818979204702523182L;

    /**
     * 移대뱶?곹깭
     * 怨듯넻肄붾뱶 : MEML00
     */
    private String custStatCode;
    
    /**
     * 移대뱶踰덊샇
     */
    private String cutCardNo;

    /**
     * 怨좉컼?꾩씠??
     */
    private String customerId;
    
    /**
     * 怨좉컼紐?
     */
    private String customerName;

    /**
     * Get custStatCode
     * @return custStatCode
     */
    public String getCustStatCode() {
        return custStatCode;
    }

    /**
     * Set custStatCode
     * @param custStatCode
     */
    public void setCustStatCode(String custStatCode) {
        this.custStatCode = custStatCode;
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
     * Get customerName
     * @return customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Set customerName
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
}

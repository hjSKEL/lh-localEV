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
     * 정지여부 (TB_CUCA001.STOP_YN)
     */
    private String stopYn;
    
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
     * 정렬 - A:카드번호ASC, B:카드번호DESC, C:세대주명ASC, D:세대주명DESC
     */
    private String sortOrder;

    /**
     * Get sortOrder
     * @return sortOrder
     */
    public String getSortOrder() {
        return sortOrder;
    }

    /**
     * Set sortOrder
     * @param sortOrder
     */
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Get stopYn
     * @return stopYn
     */
    public String getStopYn() {
        return stopYn;
    }

    /**
     * Set stopYn
     * @param stopYn
     */
    public void setStopYn(String stopYn) {
        this.stopYn = stopYn;
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

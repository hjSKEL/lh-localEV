/*******************************************************************************
 * Copyright(c) 2019 Charge All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.shared;

import kr.co.kevit.localcsms.customer.entity.domain.CustomerCard;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr</a> 
 * @since 2019. 5. 18.
 */
public class CustomerCardDto extends CustomerCard{

    /**  */
    private static final long serialVersionUID = -4102821828598084209L;
    
    private String customerName;
    
    private String senderName;
    
    /**
     * ?섏떊??
     */
    private String rctName;

    private String dong;

    private String ho;

    private String complexName;

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

    /**
     * Get senderName
     * @return senderName
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * Set senderName
     * @param senderName
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * Get rctName
     * @return rctName
     */
    public String getRctName() {
        return rctName;
    }

    /**
     * Set rctName
     * @param rctName
     */
    public void setRctName(String rctName) {
        this.rctName = rctName;
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

    public String getComplexName() {
        return complexName;
    }

    public void setComplexName(String complexName) {
        this.complexName = complexName;
    }

}

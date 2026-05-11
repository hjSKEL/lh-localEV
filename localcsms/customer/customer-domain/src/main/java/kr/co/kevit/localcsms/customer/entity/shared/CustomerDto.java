/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.shared;

import kr.co.kevit.localcsms.customer.entity.domain.Customer;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 5. 9.
 */
public class CustomerDto extends Customer{
    
    /**  */
    private static final long serialVersionUID = -5826108546755947959L;

    /**
     * 濡쒓렇?몄븘?대뵒
     * LOGIN_ID       VARCHAR(20)
     */
    private String loginId;

    private String userPwd;

    private String userType;

    private CustomerCardDto customerCard;
    
    private String userStatus;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public CustomerCardDto getCustomerCard() {
        return customerCard;
    }

    public void setCustomerCard(CustomerCardDto customerCard) {
        this.customerCard = customerCard;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}

/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.shared;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 10. 10.
 */
public class UserInfoDto {
    private String loginId;

    private String userId;
    
    private String userName;
    
    private String userRole;
    
    private String companyName;

    /**
     * Get loginId
     * @return loginId
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * Set loginId
     * @param loginId
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * Get userId
     * @return userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set userId
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Get userName
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set userName
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get userRole
     * @return userRole
     */
    public String getUserRole() {
        return userRole;
    }

    /**
     * Set userRole
     * @param userRole
     */
    public void setUserRole(String userRole) {
        this.userRole = userRole;
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
    
}

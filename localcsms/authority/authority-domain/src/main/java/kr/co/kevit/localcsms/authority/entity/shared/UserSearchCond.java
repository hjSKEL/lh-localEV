/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 10. 10.
 */
public class UserSearchCond extends PageCriteria{
    
    /**
     * 사용자유형 USER_TP VARCHAR(20) NOT NULL,
     */
    private String userType;

    /**
     * Get userType
     * @return userType
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Set userType
     * @param userType
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
}

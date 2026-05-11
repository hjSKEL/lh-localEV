/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.domain;

import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;

import java.util.List;

/**
 * 
 * USER_ROLE
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 13.
 */
public class UserRole {

    /**
     * 로그인아이디
     * LOGIN_ID        VARCHAR(20)            NOT NULL,
     */
    private String loginId;

    /**
     * 역할구분
     * ORD_PRI        VARCHAR(20)            NOT NULL,
     */
    private UserRoleType roleType;

    /**
     * 우선순위
     * ORD_PRI        INT(11)            NOT NULL,
     */
    private int ordPriority;

    private List<RoleAuthority> authority;

    public UserRole() {
    }

    public UserRole(String loginId, UserRoleType roleType) {
        this.loginId = loginId;
        this.roleType = roleType;
        this.ordPriority = 1;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public UserRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(UserRoleType roleType) {
        this.roleType = roleType;
    }

    public List<RoleAuthority> getAuthority() {
        return authority;
    }

    public void setAuthority(List<RoleAuthority> authority) {
        this.authority = authority;
    }

    public int getOrdPriority() {
        return ordPriority;
    }

    public void setOrdPriority(int ordPriority) {
        this.ordPriority = ordPriority;
    }
}

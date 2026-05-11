/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.shared;

import kr.co.kevit.localcsms.organization.entity.domain.Employee;

import java.util.Date;
import java.util.List;

/**
 * 議곗쭅 - 吏곸썝 DTO
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 5. 7.
 */
public class EmployeeDto extends Employee {

    /**  */
    private static final long serialVersionUID = -6432736185931279998L;

    /**
     * 濡쒓렇?몄븘?대뵒 LOGIN_ID VARCHAR(20) NOT NULL,
     */
    private String loginId;

    /**
     * ?ъ슜?먯긽??USER_STAT VARCHAR(20) NOT NULL,
     */
    private String userStatus;

    /**
     * 鍮꾨?踰덊샇?ㅽ뙣?뚯닔 PW_FAIL_CNT INT NOT NULL,
     */
    private int pwFailCount = 0;

    /**
     * 鍮꾨?踰덊샇留뚮즺??PW_EXP_DAT VARCHAR(20) NOT NULL,
     */
    private Date pwExpireDate;

    /**
     * 留덉?留?濡쒓렇????LST_LOGIN_DT NULL,
     */
    private Date lastLoginDate;

    /**
     * ?뚯궗紐?CO_ID VARCHAR(60) NOT NULL,
     */
    private String companyName;

    private String bid;

    private List<EmployeeChargePointDto> employeeChargePoints;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public int getPwFailCount() {
        return pwFailCount;
    }

    public void setPwFailCount(int pwFailCount) {
        this.pwFailCount = pwFailCount;
    }

    public Date getPwExpireDate() {
        return pwExpireDate;
    }

    public void setPwExpireDate(Date pwExpireDate) {
        this.pwExpireDate = pwExpireDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public List<EmployeeChargePointDto> getEmployeeChargePoints() {
        return employeeChargePoints;
    }

    public void setEmployeeChargePoints(List<EmployeeChargePointDto> employeeChargePoints) {
        this.employeeChargePoints = employeeChargePoints;
    }
}

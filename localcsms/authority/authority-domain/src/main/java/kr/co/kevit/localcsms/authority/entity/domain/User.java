/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * TB_USAU001
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 10. 15.
 */
public class User implements Serializable {

    /**  */
    private static final long serialVersionUID = -1133767604428524853L;

    /**
     * 로그인아이디 LOGIN_ID VARCHAR(20) NOT NULL,
     */
    private String loginId;

    /**
     * 사용자아이디 USER_ID CHAR(9) NOT NULL,
     */
    private String userId;

    /**
     * 사용자유형 USER_TP VARCHAR(20) NOT NULL,
     */
    private String userType;

    /**
     * 사용자비밀번호 USER_PWD VARCHAR(50) NOT NULL,
     */
    private String userPwd;

    /**
     * USER_SALT VARCHAR(30) NOT NULL
     */
    private String salt;

    /**
     * 비밀번호실패회수 PW_FAIL_CNT INT NOT NULL,
     */
    private int pwFailCount = 0;

    /**
     * 사용자상태 USER_STAT VARCHAR(20) NOT NULL,
     */
    private String userStatus = "1";

    /**
     * 비밀번호변경일 PW_UPD_DT CHAR(8) NOT NULL,
     */
    private Date pwUpdateDate;

    /**
     * 비밀번호만료일 PW_EXP_DT CHAR(8) NOT NULL
     */
    private Date pwExpireDate;

    private Writer writer;

    private Date lastLoginDate;

    private List<UserRole> roles;

    // 초기 비밀번호 판단 Y/N
    private String pwInitYn;

    public User() {
        this.pwExpireDate = setDefaultExpireDateMonth(12);
    }

    private Date setDefaultExpireDateMonth(int month) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    /**
     * Get loginId
     * 
     * @return loginId
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * Set loginId
     * 
     * @param loginId
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * Get userId
     * 
     * @return userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set userId
     * 
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Get userType
     * 
     * @return userType
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Set userType
     * 
     * @param userType
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * Get userPwd
     * 
     * @return userPwd
     */
    public String getUserPwd() {
        return userPwd;
    }

    /**
     * Set userPwd
     * 
     * @param userPwd
     */
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    /**
     * Get salt
     * 
     * @return salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Set salt
     * 
     * @param salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * Get pwFailCount
     * 
     * @return pwFailCount
     */
    public int getPwFailCount() {
        return pwFailCount;
    }

    /**
     * Set pwFailCount
     * 
     * @param pwFailCount
     */
    public void setPwFailCount(int pwFailCount) {
        this.pwFailCount = pwFailCount;
    }

    /**
     * Get userStatus
     * 
     * @return userStatus
     */
    public String getUserStatus() {
        return userStatus;
    }

    /**
     * Set userStatus
     * 
     * @param userStatus
     */
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * Get pwUpdateDate
     * 
     * @return pwUpdateDate
     */
    public Date getPwUpdateDate() {
        return pwUpdateDate;
    }

    /**
     * Set pwUpdateDate
     * 
     * @param pwUpdateDate
     */
    public void setPwUpdateDate(Date pwUpdateDate) {
        this.pwUpdateDate = pwUpdateDate;
    }

    /**
     * Get pwExpireDate
     * 
     * @return pwExpireDate
     */
    public Date getPwExpireDate() {
        return pwExpireDate;
    }

    /**
     * Set pwExpireDate
     * 
     * @param pwExpireDate
     */
    public void setPwExpireDate(Date pwExpireDate) {
        this.pwExpireDate = pwExpireDate;
    }

    /**
     * Get writer
     * 
     * @return writer
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * Set writer
     * 
     * @param writer
     */
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Get lastLoginDate
     * 
     * @return lastLoginDate
     */
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    /**
     * Set lastLoginDate
     * 
     * @param lastLoginDate
     */
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    /**
     * Get roles
     * 
     * @return roles
     */
    public List<UserRole> getRoles() {
        return roles;
    }

    /**
     * Set roles
     * 
     * @param roles
     */
    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public String getPwInitYn() {
        return pwInitYn;
    }

    public void setPwInitYn(String pwInitYn) {
        this.pwInitYn = pwInitYn;
    }
}

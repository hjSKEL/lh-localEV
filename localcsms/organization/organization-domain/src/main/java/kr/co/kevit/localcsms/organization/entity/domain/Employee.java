/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.common.util.string.StringUtils;

import java.io.Serializable;

/**
 * 직원
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 19.
 */
public class Employee implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 136875480538095177L;

    /**
     * 직원아이디
     * EMP_ID       CHAR(9)                 NOT NULL,
     */
    private String employeeId;

    /**
     * 법인아이디
     * CO_ID       VARCHAR(20)
     */
    private String companyId;

    /**
     * 역할구분
     * ORD_PRI        VARCHAR(20)            NOT NULL,
     */
    private UserRoleType roleType;

    /**
     * 직원명
     * EMP_NM       VARCHAR(20)
     */
    private String emplName;
    
    /**
     * 직원 상태
     * EMP_STATUS       VARCHAR(20)
     */
    private String emplStatus;

    /**
     * 사무실전화번호
     * OFC_PHN_NO       VARCHAR(60)
     */
    private String ofcPhoneNo;

    /**
     * 부서명
     * DEPT_NM       VARCHAR(100)
     */
    private String deptName;

    /**
     * 휴대폰번호
     * DEPT_NM       VARCHAR(100)
     */
    private String mblPhoneNo;

    /**
     * 이메일
     * EMAIL       VARCHAR(200)
     */
    private String email;

    /**
     * SMS수신여부
     * SMS_RCT_YN       CHAR(1)
     */
    private String smsRctYn;

    /**
     * email수신여부
     * EMAIL_RCT_YN       CHAR(1)
     */
    private String emailRctYn;

    private Writer writer;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmplName() {
        return emplName;
    }

    public void setEmplName(String emplName) {
        this.emplName = emplName;
    }

    public String getEmplStatus() {
		return emplStatus;
	}

	public void setEmplStatus(String emplStatus) {
		this.emplStatus = emplStatus;
	}

	public String getOfcPhoneNo() {
        return ofcPhoneNo;
    }

    public void setOfcPhoneNo(String ofcPhoneNo) {
        this.ofcPhoneNo = ofcPhoneNo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getMblPhoneNo() {
        return mblPhoneNo;
    }

    public void setMblPhoneNo(String mblPhoneNo) {
        this.mblPhoneNo = mblPhoneNo;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public UserRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(UserRoleType roleType) {
        this.roleType = roleType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSmsRctYn() {
        return smsRctYn;
    }

    public void setSmsRctYn(String smsRctYn) {
        this.smsRctYn = smsRctYn;
    }

    public String getEmailRctYn() {
        return emailRctYn;
    }

    public void setEmailRctYn(String emailRctYn) {
        this.emailRctYn = emailRctYn;
    }

    public void makeEmployeeId(String maxEmployeeId) {
        String cpSeqStr = StringUtils.leftPadding(String.valueOf(StringUtils.isEmpty(maxEmployeeId) ? 1 : maxEmployeeId), '0', 8);
        this.setEmployeeId("E" + cpSeqStr);
    }
}

/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.domain;

import java.io.Serializable;

/**
 * 직원 관리 충전소
 * @author wj.lee <a href="mailto:wj.lee@kevit.co.kr">wj.lee@kevit.co.kr</a>
 * @since 2021. 12. 22.
 */
public class EmployeeChargePoint implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 186875480138006177L;

    /**
     * 직원아이디
     * EMP_ID       CHAR(9)                 NOT NULL,
     */
    private String employeeId;

    /**
     * 충전소아이디
     * CP_ID       CHAR(9)                 NOT NULL,
     */
    private String cpId;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }
}

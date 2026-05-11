/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.shared;


import kr.co.kevit.localcsms.organization.entity.domain.EmployeeChargePoint;

/**
 * @author wj.lee <a href="mailto:wj.lee@kevit.co.kr">wj.lee@kevit.co.kr</a>
 * @since 2019. 5. 3.
 */
public class EmployeeChargePointDto extends EmployeeChargePoint {

    private String cpName;

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }
}

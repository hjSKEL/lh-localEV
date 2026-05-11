/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity;

import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.domain.Employee;
import kr.co.kevit.localcsms.organization.entity.domain.EmployeeChargePoint;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeChargePointDto;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeDto;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeSearchCond;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 19.
 */
@Component
public interface EmployeeProvider {
    
    public List<EmployeeDto> retrieveEmployeeByCompanyId(String companyId);

    public List<EmployeeDto> retrieveEmployeeByIds(List<String> ids);

    public EmployeeDto retrieveEmployeeById(String id);

    public Page<EmployeeDto> retrieveEmployeeBySearchCond(EmployeeSearchCond searchCond);

    public void registerEmployee(Employee employee);

    public void modifyEmployee(Employee employee);

    public List<EmployeeDto> retrieveCoUserEmployeeByCompanyId(String companyId, UserRoleType userRoleType);

    public List<Employee> retrieveAdminEmployee(String name);

    void registerEmployeeChargePoint(EmployeeChargePoint employeeChargePoint);

    void removeEmployeeChargePointById(String employeeId);

    List<EmployeeChargePointDto> retrieveEmployeeChargePointById(String employeeId);
}

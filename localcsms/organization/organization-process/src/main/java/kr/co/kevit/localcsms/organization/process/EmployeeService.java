/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.process;

import java.util.List;
import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.domain.Employee;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeDto;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 19.
 */
public interface EmployeeService {

    EmployeeDto retrieveEmployeeById(String id);

    Page<EmployeeDto> retrieveEmployeeBySearchCond(EmployeeSearchCond searchCond, UserRoleType role);

    /**
     * 직원목록 조회. ROOT_ADMIN=전체, ADMIN=본인 소속법인만, 그 외(OPERATION)=본인 계정만 보이도록
     * loginUserId 기준으로 조회범위를 강제 제한한다.
     */
    Page<EmployeeDto> retrieveEmployeeWithUserBySearchCond(EmployeeSearchCond searchCond, UserRoleType role, String loginUserId);

    void registerEmployee(Employee employee);

    void modifyEmployee(Employee employee);

    List<Employee> retrieveAdminEmployee(String name);

    void removeEmployee(Employee employee);
}

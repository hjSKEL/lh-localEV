/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.dao;

import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.organization.entity.domain.Employee;
import kr.co.kevit.localcsms.organization.entity.domain.EmployeeChargePoint;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeChargePointDto;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeDto;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeSearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 19.
 */
@Repository
public interface EmployeeMapper {

    public List<EmployeeDto> selectEmployeeByIds(@Param("employeeIds") List<String> ids);
    
    public List<EmployeeDto> selectEmployeeByCompanyId(@Param("companyId") String companyId);
    
    public EmployeeDto selectEmployeeById(@Param("employeeId") String employeeId);

    public int countEmployeeBySearchCond(@Param("searchCond") EmployeeSearchCond searchCond);

    public List<EmployeeDto> selectEmployeeBySearchCond(@Param("searchCond") EmployeeSearchCond searchCond);

    public String selectMaxEmployeeId();

    public int insertEmployee(@Param("employee") Employee employee);

    public int updateEmployee(@Param("employee") Employee employee);

    public List<EmployeeDto> selectCoUserEmployeeByCompanyId(@Param("companyId") String companyId, @Param("roleType") UserRoleType roleType);

    public List<Employee> selectAdminEmployee(@Param("companyName") String companyName);

    int insertEmployeeChargePoint(@Param("employeeChargePoint") EmployeeChargePoint employeeChargePoint);

    int deleteEmployeeChargePointById(@Param("employeeId") String employeeId);

    List<EmployeeChargePointDto> selectEmployeeChargePointDtoById(@Param("employeeId") String employeeId);
}

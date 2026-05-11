/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.EmployeeProvider;
import kr.co.kevit.localcsms.organization.entity.dao.EmployeeMapper;
import kr.co.kevit.localcsms.organization.entity.domain.Employee;
import kr.co.kevit.localcsms.organization.entity.domain.EmployeeChargePoint;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeChargePointDto;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeDto;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 19.
 */
@Component
public class EmployeeProviderImpl implements EmployeeProvider {

    @Autowired
    private EmployeeMapper mapper;

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public EmployeeDto retrieveEmployeeById(String id) {
        //
        return mapper.selectEmployeeById(id);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Page<EmployeeDto> retrieveEmployeeBySearchCond(EmployeeSearchCond searchCond) {
        //
        int totalItemCount = mapper.countEmployeeBySearchCond(searchCond);
        searchCond.setTotalItemCount(totalItemCount);
        Page<EmployeeDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0) {
            resultSet.setResult(new ArrayList<>(0));
            return resultSet;
        }
        List<EmployeeDto> result = mapper.selectEmployeeBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void registerEmployee(Employee employee) {
        //
        String maxEmployeeId = mapper.selectMaxEmployeeId();
        employee.makeEmployeeId(maxEmployeeId);
        mapper.insertEmployee(employee);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void modifyEmployee(Employee employee) {
        //
        mapper.updateEmployee(employee);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<EmployeeDto> retrieveCoUserEmployeeByCompanyId(String companyId, UserRoleType userRoleType) {
        //
        return mapper.selectCoUserEmployeeByCompanyId(companyId, userRoleType);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<Employee> retrieveAdminEmployee(String name) {
        //
        return mapper.selectAdminEmployee(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmployeeDto> retrieveEmployeeByCompanyId(String companyId) {
        //
        return mapper.selectEmployeeByCompanyId(companyId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmployeeDto> retrieveEmployeeByIds(List<String> ids) {
        // 
        return mapper.selectEmployeeByIds(ids);
    }

    @Override
    public void registerEmployeeChargePoint(EmployeeChargePoint employeeChargePoint) {
        //
        mapper.insertEmployeeChargePoint(employeeChargePoint);
    }

    @Override
    public void removeEmployeeChargePointById(String employeeId) {
        //
        mapper.deleteEmployeeChargePointById(employeeId);
    }

    @Override
    public List<EmployeeChargePointDto> retrieveEmployeeChargePointById(String employeeId) {
        //
        return mapper.selectEmployeeChargePointDtoById(employeeId);
    }
}

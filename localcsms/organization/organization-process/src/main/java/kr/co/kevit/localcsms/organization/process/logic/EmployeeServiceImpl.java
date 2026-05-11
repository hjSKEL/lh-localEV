/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.process.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.authority.external.UserExtProcess;
import kr.co.kevit.localcsms.common.entity.CryptoKeyProvider;
import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.security.AES256Util;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.localcsms.organization.entity.EmployeeProvider;
import kr.co.kevit.localcsms.organization.entity.domain.Company;
import kr.co.kevit.localcsms.organization.entity.domain.Employee;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeDto;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeSearchCond;
import kr.co.kevit.localcsms.organization.external.CompanyExtProcess;
import kr.co.kevit.localcsms.organization.process.EmployeeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 19.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeProvider provider;

    @Autowired
    private UserExtProcess userExtProcess;
    
    @Autowired
    private CompanyExtProcess companyExtProcess;

    @Autowired
    private CryptoKeyProvider cryptoKeyProvider;

    @Transactional(readOnly = true)

    @Override
    public EmployeeDto retrieveEmployeeById(String id) {
        //
        EmployeeDto employee = provider.retrieveEmployeeById(id);
        if (employee == null) {
            return null;
        }
        User user = userExtProcess.retrieveUserByUserId(employee.getEmployeeId());
        if (user != null) {
        	employee.setPwFailCount(user.getPwFailCount());
            employee.setLoginId(user.getLoginId());
            employee.setLastLoginDate(user.getLastLoginDate());
        }

        Company company = companyExtProcess.retrieveCompanyById(employee.getCompanyId());
        String makerCode = company.getInfoItemValue("CPIT01");
            String chBid = company.getInfoItemValue("CPIT02");
        if (company != null ) {
            if (StringUtils.isNotEmpty(chBid)) {
                employee.setBid(chBid);
            } else if (StringUtils.isNotEmpty(makerCode)) {
                employee.setBid(makerCode);
            }
        }
        if(employee.getMblPhoneNo() != null) {
            byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Employee.class);
            employee.setMblPhoneNo(AES256Util.decryption(keyData, employee.getMblPhoneNo()));
        }
        return employee;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<EmployeeDto> retrieveEmployeeBySearchCond(EmployeeSearchCond searchCond, UserRoleType role) {
        //
        List<String> roleTypes = new ArrayList<>();
        if(role == UserRoleType.ADMIN) {
            roleTypes.add(UserRoleType.ADMIN.toString());
            roleTypes.add(UserRoleType.OPERATION.toString());
        }else {
            roleTypes.add(UserRoleType.OPERATION.toString());
        }
        searchCond.setRoleTypes(roleTypes);
        Page<EmployeeDto> resultSet = provider.retrieveEmployeeBySearchCond(searchCond);

        if (resultSet.getCriteria().getTotalItemCount() == 0) {
            return resultSet;
        }
        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Employee.class);
        for (EmployeeDto employeeDto : resultSet.getResult()) {
            employeeDto.setMblPhoneNo(AES256Util.decryption(keyData, employeeDto.getMblPhoneNo()));
        }

        return resultSet;
    }

    @Transactional(readOnly = true)

    @Override
    public Page<EmployeeDto> retrieveEmployeeWithUserBySearchCond(EmployeeSearchCond searchCond, UserRoleType role) {
        //
        List<String> roleTypes = new ArrayList<>();
        if(role == UserRoleType.ADMIN) {
            roleTypes.add("ROLE_ADMIN");
            roleTypes.add("ROLE_OPER");
        }else {
            roleTypes.add("ROLE_OPER");
        }
        searchCond.setRoleTypes(roleTypes);
        Page<EmployeeDto> employees = provider.retrieveEmployeeBySearchCond(searchCond);
        
        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Employee.class);
        for (EmployeeDto employee : employees.getResult()) {
            employee.setMblPhoneNo(AES256Util.decryption(keyData, employee.getMblPhoneNo()));
            User user = userExtProcess.retrieveUserByUserId(employee.getEmployeeId());
            if (user == null) {
                continue;
            }
            employee.setLoginId(user.getLoginId());
            employee.setPwExpireDate(user.getPwExpireDate());
            employee.setPwFailCount(user.getPwFailCount());
            employee.setUserStatus(user.getUserStatus());
            employee.setLastLoginDate(user.getLastLoginDate());
        }
        return employees;
    }

    @Override
    public void registerEmployee(Employee employee) {
        //
        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Employee.class);
        employee.setMblPhoneNo(AES256Util.encryption(keyData, employee.getMblPhoneNo()));
        provider.registerEmployee(employee);
    }

    @Override
    public void modifyEmployee(Employee employee) {
        //
        if(employee.getMblPhoneNo() != null) {
            byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Employee.class);
            employee.setMblPhoneNo(AES256Util.encryption(keyData, employee.getMblPhoneNo()));
        }
        provider.modifyEmployee(employee);
    }

    @Transactional(readOnly = true)

    @Override
    public List<Employee> retrieveAdminEmployee(String name) {
        //
        return provider.retrieveAdminEmployee(name);
    }

	@Override
	public void removeEmployee(Employee employee) {
		// 
		employee.setEmplStatus("2");
		employee.setEmplName(employee.getEmplName() + "(?댁궗)");
	    if (employee.getMblPhoneNo() != null) {
	      byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Employee.class);
	      employee.setMblPhoneNo(AES256Util.encryption(keyData, employee.getMblPhoneNo()));
	    } 
	    provider.modifyEmployee(employee);
	    User user = this.userExtProcess.retrieveUserByUserId(employee.getEmployeeId());
	    if (user != null) {
	      userExtProcess.removeUser(user.getLoginId());
	    } 
	  }
}

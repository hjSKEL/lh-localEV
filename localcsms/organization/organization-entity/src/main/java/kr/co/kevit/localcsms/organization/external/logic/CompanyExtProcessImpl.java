/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.external.logic;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.organization.entity.dao.CompanyMapper;
import kr.co.kevit.localcsms.organization.entity.dao.EmployeeMapper;
import kr.co.kevit.localcsms.organization.entity.domain.Company;
import kr.co.kevit.localcsms.organization.entity.shared.CoAdminEmployeeDto;
import kr.co.kevit.localcsms.organization.entity.shared.CompanyDto;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeDto;
import kr.co.kevit.localcsms.organization.external.CompanyExtProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 4. 11.
 */
@Component
public class CompanyExtProcessImpl implements CompanyExtProcess {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Company retrieveCompanyById(String id) {
        //
        Company company = companyMapper.selectCompanyById(id);
        if (company != null) {
            company.setInfoItems(companyMapper.selectCompanyInfoItemByCompanyId(id));
        }
        return company;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Company> retrieveCompanyByIds(List<String> ids) {
        //
        return companyMapper.selectCompanyByIds(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CoAdminEmployeeDto retrieveCoAdminEmployeeDto(String companyId) {
        //
        return companyMapper.selectCoAdminEmployeeDto(companyId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CoAdminEmployeeDto> retrieveCoAdminEmployeeDtos(List<String> companyIds) {
        //
        return companyMapper.selectCoAdminEmployeeDtos(companyIds);
    }

    @Override
    public boolean synchronizeCustomerCount(String companyId, int customerCount, Writer writer) {
        //
        CompanyDto companyDto = companyMapper.selectCompanyByCompanyId(companyId);
        companyDto.setCustCount(customerCount);
        companyDto.setWriter(writer);
        int result = companyMapper.updateCompany(companyDto);
        return result == 1;
    }

    @Override
    public Company retrieveCompanyByEmployeeId(String employeeId) {
        //
        EmployeeDto employee = employeeMapper.selectEmployeeById(employeeId);
        if (employee != null) {
            Company company = companyMapper.selectCompanyById(employee.getCompanyId());
            if (company != null) {
                company.setInfoItems(companyMapper.selectCompanyInfoItemByCompanyId(company.getCompanyId()));
            }
            return company;
        }
        return null;
    }
}

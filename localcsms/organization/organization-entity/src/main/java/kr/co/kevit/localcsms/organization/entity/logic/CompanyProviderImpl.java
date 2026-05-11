/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.logic;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.CompanyProvider;
import kr.co.kevit.localcsms.organization.entity.dao.CompanyMapper;
import kr.co.kevit.localcsms.organization.entity.dao.EmployeeMapper;
import kr.co.kevit.localcsms.organization.entity.domain.Company;
import kr.co.kevit.localcsms.organization.entity.domain.CompanyInfoItem;
import kr.co.kevit.localcsms.organization.entity.domain.Employee;
import kr.co.kevit.localcsms.organization.entity.shared.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 11.
 */
@Component
public class CompanyProviderImpl implements CompanyProvider {

    @Autowired
    private CompanyMapper mapper;
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Company retrieveCompanyById(String id) {
        // 
        Company company = mapper.selectCompanyById(id);
        if(company != null) {
            company.setInfoItems(mapper.selectCompanyInfoItemByCompanyId(id));
        }
        return company;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Company retrieveCompanyWithEmployeeById(String id) {
        // 
        Company company = mapper.selectCompanyById(id);
        if(company == null)
            return company;
        EmployeeSearchCond searchCond = new EmployeeSearchCond();
        searchCond.setCompanyId(id);
        searchCond.setPageNumber(0);
        searchCond.setPageItemSize(Integer.MAX_VALUE);
        company.setEmployees(employeeMapper.selectEmployeeBySearchCond(searchCond));
        return company;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Company> retrieveCompanyByCompanySearchCond(CompanySearchCond searchCond) {
        // 
        Page<Company> resultSet = new Page<Company>();
        int totalItemCount = mapper.countCompanyByCompanySearchCond(searchCond);
        searchCond.setTotalItemCount(totalItemCount);
        resultSet.setCriteria(searchCond);
        if(totalItemCount == 0)
            return resultSet;
        
        resultSet.setResult(mapper.selectCompanyByCompanySearchCond(searchCond));
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int registerCompany(Company company) {
        //
        String maxCompanyId = mapper.selectMaxCompanyId();
        company.makeCompanyId(maxCompanyId);
        int result = mapper.insertCompany(company);
        //踰뺤씤 ?뺣낫??ぉ ??젣 ???깅줉
        mapper.deleteCompanyInfoItem(company.getCompanyId());
        if (company.getInfoItems() != null) {
            for (CompanyInfoItem infoItem : company.getInfoItems()) {
                mapper.insertCompanyInfoItem(infoItem);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean modifyCompany(CompanyDto company) {
        // 
        int result = mapper.updateCompany(company);
        //踰뺤씤 ?뺣낫??ぉ ??젣 ???깅줉
        mapper.deleteCompanyInfoItem(company.getCompanyId());
        if (company.getInfoItems() != null) {
            for (CompanyInfoItem infoItem : company.getInfoItems()) {
                 mapper.insertCompanyInfoItem(infoItem);
            }
        }
        return result == 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Company retrieveCompanyByEmployeeId(String employeeId) {
        // 
        Employee employee = employeeMapper.selectEmployeeById(employeeId);
        if (employee != null) {
            Company company = mapper.selectCompanyById(employee.getCompanyId());
            company.setInfoItems(mapper.selectCompanyInfoItemByCompanyId(company.getCompanyId()));
            return company;
        }
        return null;
    }

    @Override
    public Page<CompanyDto> retrieveCompanyDetailByCompanySearchCond(CompanySearchCond searchCond) {
        Page<CompanyDto> resultSet = new Page<CompanyDto>();
        int totalItemCount = mapper.countCompanyDetailByCompanySearchCond(searchCond);
        searchCond.setTotalItemCount(totalItemCount);
        resultSet.setCriteria(searchCond);
        if(totalItemCount == 0)
            return resultSet;

        resultSet.setResult(mapper.selectCompanyDetailByCompanySearchCond(searchCond));
        return resultSet;
    }

    @Override
    public CompanyDto retrieveCompanyByCompanyId(String companyId) {
        //
        CompanyDto companyDto = mapper.selectCompanyByCompanyId(companyId);
        if(companyDto != null) {
            companyDto.setInfoItems(mapper.selectCompanyInfoItemByCompanyId(companyId));
        }
        return companyDto;
    }

    @Override
    public boolean removeCompany(String companyId) {
        //
        return mapper.deleteCompany(companyId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Company> retrieveCompanyByIds(List<String> ids) {
        //
        return mapper.selectCompanyByIds(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CoAdminEmployeeDto retrieveCoAdminEmployeeDto(String companyId) {
        //
        return mapper.selectCoAdminEmployeeDto(companyId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CoAdminEmployeeDto> retrieveCoAdminEmployeeDtos(List<String> companyIds) {
        //
        return mapper.selectCoAdminEmployeeDtos(companyIds);
    }

    /**
     * ?대떦 踰뺤씤??怨좉컼?섎? ?꾩옱 吏곸썝?뚯씠釉?湲곗??쇰줈 ?ш퀎??
     * @param companyId
     * @return
     */
    @Override
    public boolean synchronizeCustomerCount(String companyId, int customerCount, Writer writer) {
        //
        CompanyDto companyDto = mapper.selectCompanyByCompanyId(companyId);
        companyDto.setCustCount(customerCount);
        companyDto.setWriter(writer);
        int result = mapper.updateCompany(companyDto);
        return result == 1;
    }

    @Override
    public CompanyInfoItem retrieveCompanyInfoItemByValue(String value) {
        return mapper.selectCompanyInfoItemByValue(value);
    }
}

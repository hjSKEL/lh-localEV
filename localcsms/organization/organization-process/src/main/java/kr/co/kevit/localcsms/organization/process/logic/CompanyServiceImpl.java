/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.process.logic;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.CompanyProvider;
import kr.co.kevit.localcsms.organization.entity.domain.Company;
import kr.co.kevit.localcsms.organization.entity.domain.CompanyInfoItem;
import kr.co.kevit.localcsms.organization.entity.shared.CompanyDto;
import kr.co.kevit.localcsms.organization.entity.shared.CompanySearchCond;
import kr.co.kevit.localcsms.organization.process.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 4. 11.
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyProvider provider;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Company retrieveCompanyById(String id) {
        //
        return provider.retrieveCompanyById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Company retrieveCompanyWithEmployeeById(String id) {
        //
        return provider.retrieveCompanyWithEmployeeById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<Company> retrieveCompanyByCompanySearchCond(CompanySearchCond searchCond) {
        //
        return provider.retrieveCompanyByCompanySearchCond(searchCond);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int registerCompany(Company company) {
        //
        return provider.registerCompany(company);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean modifyCompany(CompanyDto company) {
        //
        // ?뚯궗?뺣낫 蹂寃?
        return provider.modifyCompany(company);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Company retrieveCompanyByEmployeeId(String employeeId) {
        //
        return provider.retrieveCompanyByEmployeeId(employeeId);
    }

    @Transactional(readOnly = true)

    @Override
    public Page<CompanyDto> retrieveCompanyDetailByCompanySearchCond(CompanySearchCond searchCond) {
        //
        return provider.retrieveCompanyDetailByCompanySearchCond(searchCond);
    }

    @Transactional(readOnly = true)

    @Override
    public CompanyDto retrieveCompanyByCompanyId(String companyId) {
        //
        return provider.retrieveCompanyByCompanyId(companyId);
    }

    @Override
    public boolean removeCompany(String companyId) {
        //
        return provider.removeCompany(companyId);
    }

    @Transactional(readOnly = true)

    @Override
    public CompanyInfoItem retrieveCompanyInfoItemByValue(String value) {
        //
        return provider.retrieveCompanyInfoItemByValue(value);
    }
}

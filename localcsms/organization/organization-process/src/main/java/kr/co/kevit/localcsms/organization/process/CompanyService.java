/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.process;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.domain.Company;
import kr.co.kevit.localcsms.organization.entity.domain.CompanyInfoItem;
import kr.co.kevit.localcsms.organization.entity.shared.CompanyDto;
import kr.co.kevit.localcsms.organization.entity.shared.CompanySearchCond;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 4. 11.
 */
public interface CompanyService {

    Company retrieveCompanyById(String id);

    Company retrieveCompanyByEmployeeId(String employeeId);

    Company retrieveCompanyWithEmployeeById(String id);

    Page<Company> retrieveCompanyByCompanySearchCond(CompanySearchCond searchCond);

    Page<CompanyDto> retrieveCompanyDetailByCompanySearchCond(CompanySearchCond searchCond);

    int registerCompany(Company company);

    boolean modifyCompany(CompanyDto company);

    CompanyDto retrieveCompanyByCompanyId(String companyId);

    boolean removeCompany(String companyId);

    CompanyInfoItem retrieveCompanyInfoItemByValue(String value);
}

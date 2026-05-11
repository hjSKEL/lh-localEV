/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.domain.Company;
import kr.co.kevit.localcsms.organization.entity.domain.CompanyInfoItem;
import kr.co.kevit.localcsms.organization.entity.shared.CoAdminEmployeeDto;
import kr.co.kevit.localcsms.organization.entity.shared.CompanyDto;
import kr.co.kevit.localcsms.organization.entity.shared.CompanySearchCond;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 19.
 */
@Component
public interface CompanyProvider {

    public Company retrieveCompanyById(String id);
    
    public List<Company> retrieveCompanyByIds(List<String> ids);
    
    public CoAdminEmployeeDto retrieveCoAdminEmployeeDto(String companyId);
    
    public List<CoAdminEmployeeDto> retrieveCoAdminEmployeeDtos(List<String> companyIds);
    
    public Company retrieveCompanyWithEmployeeById(String id);
    
    public Page<Company> retrieveCompanyByCompanySearchCond(CompanySearchCond searchCond);
    
    public int registerCompany(Company company);
    
    public boolean modifyCompany(CompanyDto company);
    
    public Company retrieveCompanyByEmployeeId(String employeeId);

    public Page<CompanyDto> retrieveCompanyDetailByCompanySearchCond(CompanySearchCond searchCond);

    public CompanyDto retrieveCompanyByCompanyId(String companyId);

    public boolean removeCompany(String companyId);

    public boolean synchronizeCustomerCount(String companyId, int customerCount, Writer writer);

    CompanyInfoItem retrieveCompanyInfoItemByValue(String value);
}

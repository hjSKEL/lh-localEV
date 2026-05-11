/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.dao;

import kr.co.kevit.localcsms.organization.entity.domain.Company;
import kr.co.kevit.localcsms.organization.entity.domain.CompanyInfoItem;
import kr.co.kevit.localcsms.organization.entity.shared.CoAdminEmployeeDto;
import kr.co.kevit.localcsms.organization.entity.shared.CompanyDto;
import kr.co.kevit.localcsms.organization.entity.shared.CompanySearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 11.
 */
@Repository
public interface CompanyMapper {
    
    public Company selectCompanyById(@Param("companyId") String companyId);
    
    public List<Company> selectCompanyByIds(@Param("ids") List<String> ids);
    
    public CoAdminEmployeeDto selectCoAdminEmployeeDto(@Param("companyId") String companyId);
    
    public List<CoAdminEmployeeDto> selectCoAdminEmployeeDtos(@Param("companyIds") List<String> companyIds);
    
    public int countCompanyByCompanySearchCond(@Param("searchCond") CompanySearchCond searchCond);
    
    public List<Company> selectCompanyByCompanySearchCond(@Param("searchCond") CompanySearchCond searchCond);
    
    public int insertCompany(@Param("company") Company company);
    
    public int updateCompany(@Param("company") CompanyDto company);
    
    public List<CompanyInfoItem> selectCompanyInfoItemByCompanyId(@Param("companyId") String companyId);
    
    public int insertCompanyInfoItem(@Param("infoItem") CompanyInfoItem infoItem);
    
    public int deleteCompanyInfoItem(@Param("companyId") String companyId);

    public List<CompanyDto> selectCompanyDetailByCompanySearchCond(@Param("searchCond") CompanySearchCond searchCond);

    public int countCompanyDetailByCompanySearchCond(@Param("searchCond") CompanySearchCond searchCond);

    public CompanyDto selectCompanyByCompanyId(@Param("companyId") String companyId);

    public boolean deleteCompany(@Param("companyId") String companyId);

    public String selectMaxCompanyId();

    CompanyInfoItem selectCompanyInfoItemByValue(@Param("value") String value);
}

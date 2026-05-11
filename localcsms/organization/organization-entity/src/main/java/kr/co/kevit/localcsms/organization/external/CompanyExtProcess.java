/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.external;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.organization.entity.domain.Company;
import kr.co.kevit.localcsms.organization.entity.shared.CoAdminEmployeeDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 4. 11.
 */
@Component
public interface CompanyExtProcess {

    Company retrieveCompanyById(String id);

    Company retrieveCompanyByEmployeeId(String employeeId);

    List<Company> retrieveCompanyByIds(List<String> ids);

    CoAdminEmployeeDto retrieveCoAdminEmployeeDto(String companyId);

    List<CoAdminEmployeeDto> retrieveCoAdminEmployeeDtos(List<String> companyIds);

    boolean synchronizeCustomerCount(String companyId, int customerCount, Writer writer);
}

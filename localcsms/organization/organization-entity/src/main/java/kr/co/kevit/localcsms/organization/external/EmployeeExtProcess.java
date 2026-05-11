/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation.
 * All rights reserved. This software is the proprietary information of
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.external;

import kr.co.kevit.localcsms.organization.entity.domain.Employee;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 19.
 */
@Component
public interface EmployeeExtProcess {

    public Employee retrieveEmployeeById(String id);

    public List<EmployeeDto> retrieveEmployeeByIds(List<String> ids);
}

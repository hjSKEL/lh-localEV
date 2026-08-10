/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.domain.Complex;
import kr.co.kevit.localcsms.organization.entity.shared.ComplexSearchCond;

/**
 * 단지 Provider
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 8. 10.
 */
@Component
public interface ComplexProvider {

    public Complex retrieveComplexById(String complexId);

    public List<Complex> retrieveComplexList();

    public List<Complex> retrieveComplexByCompanyId(String companyId);

    public Page<Complex> retrieveComplexBySearchCond(ComplexSearchCond searchCond);

    public int registerComplex(Complex complex);

    public boolean modifyComplex(Complex complex);

    public boolean removeComplex(String complexId);

}

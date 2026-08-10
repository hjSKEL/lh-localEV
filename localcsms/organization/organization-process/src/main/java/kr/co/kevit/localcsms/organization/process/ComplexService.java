/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.process;

import java.util.List;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.domain.Complex;
import kr.co.kevit.localcsms.organization.entity.shared.ComplexSearchCond;

/**
 * 단지 Service
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 8. 10.
 */
public interface ComplexService {

    Complex retrieveComplexById(String complexId);

    List<Complex> retrieveComplexList();

    List<Complex> retrieveComplexByCompanyId(String companyId);

    Page<Complex> retrieveComplexBySearchCond(ComplexSearchCond searchCond);

    int registerComplex(Complex complex);

    boolean modifyComplex(Complex complex);

    boolean removeComplex(String complexId);

}

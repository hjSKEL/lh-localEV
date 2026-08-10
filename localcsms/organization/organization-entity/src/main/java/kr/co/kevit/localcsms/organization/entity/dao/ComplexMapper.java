/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.organization.entity.domain.Complex;
import kr.co.kevit.localcsms.organization.entity.shared.ComplexSearchCond;

/**
 * TB_ORCX001 (단지) 매퍼
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 8. 10.
 */
@Repository
public interface ComplexMapper {

    public Complex selectComplexById(@Param("complexId") String complexId);

    public List<Complex> selectComplexList();

    public List<Complex> selectComplexByCompanyId(@Param("companyId") String companyId);

    public int countComplexBySearchCond(@Param("searchCond") ComplexSearchCond searchCond);

    public List<Complex> selectComplexBySearchCond(@Param("searchCond") ComplexSearchCond searchCond);

    public int insertComplex(@Param("complex") Complex complex);

    public int updateComplex(@Param("complex") Complex complex);

    public int deleteComplex(@Param("complexId") String complexId);

    public String selectMaxComplexId();

}

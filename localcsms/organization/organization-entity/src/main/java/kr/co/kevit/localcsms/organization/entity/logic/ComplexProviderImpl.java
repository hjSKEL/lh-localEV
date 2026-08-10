/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.ComplexProvider;
import kr.co.kevit.localcsms.organization.entity.dao.ComplexMapper;
import kr.co.kevit.localcsms.organization.entity.domain.Complex;
import kr.co.kevit.localcsms.organization.entity.shared.ComplexSearchCond;

/**
 * 단지 Provider 구현체
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 8. 10.
 */
@Component
public class ComplexProviderImpl implements ComplexProvider {

    @Autowired
    private ComplexMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Complex retrieveComplexById(String complexId) {
        //
        return mapper.selectComplexById(complexId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Complex> retrieveComplexList() {
        //
        return mapper.selectComplexList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Complex> retrieveComplexByCompanyId(String companyId) {
        //
        return mapper.selectComplexByCompanyId(companyId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Complex> retrieveComplexBySearchCond(ComplexSearchCond searchCond) {
        //
        Page<Complex> resultSet = new Page<Complex>();
        int totalItemCount = mapper.countComplexBySearchCond(searchCond);
        searchCond.setTotalItemCount(totalItemCount);
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0)
            return resultSet;

        resultSet.setResult(mapper.selectComplexBySearchCond(searchCond));
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int registerComplex(Complex complex) {
        //
        String maxComplexId = mapper.selectMaxComplexId();
        complex.makeComplexId(maxComplexId);
        return mapper.insertComplex(complex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean modifyComplex(Complex complex) {
        //
        return mapper.updateComplex(complex) == 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeComplex(String complexId) {
        //
        return mapper.deleteComplex(complexId) == 1;
    }

}

/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.process.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.ComplexProvider;
import kr.co.kevit.localcsms.organization.entity.domain.Complex;
import kr.co.kevit.localcsms.organization.entity.shared.ComplexSearchCond;
import kr.co.kevit.localcsms.organization.process.ComplexService;

/**
 * 단지 Service 구현체
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 8. 10.
 */
@Service
@Transactional
public class ComplexServiceImpl implements ComplexService {

    @Autowired
    private ComplexProvider provider;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Complex retrieveComplexById(String complexId) {
        //
        return provider.retrieveComplexById(complexId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Complex> retrieveComplexList() {
        //
        return provider.retrieveComplexList();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Complex> retrieveComplexByCompanyId(String companyId) {
        //
        return provider.retrieveComplexByCompanyId(companyId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<Complex> retrieveComplexBySearchCond(ComplexSearchCond searchCond) {
        //
        return provider.retrieveComplexBySearchCond(searchCond);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int registerComplex(Complex complex) {
        //
        return provider.registerComplex(complex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean modifyComplex(Complex complex) {
        //
        return provider.modifyComplex(complex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeComplex(String complexId) {
        //
        return provider.removeComplex(complexId);
    }

}

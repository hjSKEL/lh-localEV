/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.certificate.entity.logic;

import java.math.BigInteger;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.certificate.CsCertCrlProvider;
import kr.co.kevit.localcsms.certificate.entity.dao.CsCertCrlMapper;
import kr.co.kevit.localcsms.certificate.entity.domain.CsCert;
import kr.co.kevit.localcsms.certificate.entity.domain.CsCertCrl;
import kr.co.kevit.localcsms.certificate.entity.shared.CsCertCrlSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author KEVIT <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 11. 10.
 */
@Component
public class CsCertCrlProviderImpl implements CsCertCrlProvider{
    
    @Autowired
    private CsCertCrlMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCsCertCrl(CsCert cert) {
        // 
        mapper.insertCsCertCrl(cert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CsCertCrl retrieveCsCertCrl(BigInteger certId) {
        // 
        return mapper.selectCsCertCrl(certId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CsCert> retrieveCsCertCrlBySearchCond(CsCertCrlSearchCond searchCond) {
        // 
        Page<CsCert> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(mapper.countCsCertCrlBySearchCond(searchCond));
        
        if(searchCond.getTotalItemCount() > 0) {
            resultSet.setResult(mapper.selectCsCertCrlBySearchCond(searchCond));
        }else {
            resultSet.setResult(new ArrayList<>(0));
        }
        return resultSet;
    }
    
}

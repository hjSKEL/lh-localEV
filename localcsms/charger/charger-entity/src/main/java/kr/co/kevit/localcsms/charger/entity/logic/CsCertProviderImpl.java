/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.logic;

import java.math.BigInteger;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.charger.entity.CsCertProvider;
import kr.co.kevit.localcsms.charger.entity.dao.CsCertMapper;
import kr.co.kevit.localcsms.charger.entity.domain.CsCert;
import kr.co.kevit.localcsms.charger.entity.shared.CsCertSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author KEVIT <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 11. 10.
 */
@Component
public class CsCertProviderImpl implements CsCertProvider{
    
    @Autowired
    private CsCertMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCsCert(CsCert cert) {
        // 
        mapper.insertCsCert(cert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCsCert(BigInteger certId) {
        // 
        mapper.deleteCsCert(certId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CsCert retrieveCsCert(BigInteger certId) {
        // 
        return mapper.selectCsCert(certId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CsCert> retrieveCsCertBySearchCond(CsCertSearchCond searchCond) {
        // 
        Page<CsCert> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        int totalItemCount = mapper.countCsCertBySearchCond(searchCond);
        searchCond.setTotalItemCount(totalItemCount);
        if(totalItemCount  > 0) {            
            resultSet.setResult(mapper.selectCsCertBySearchCond(searchCond));
        }else {
            resultSet.setResult(new ArrayList<>(0));
        }
        return resultSet;
    }

}

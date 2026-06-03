/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process.logic;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.charger.entity.CsCertCrlProvider;
import kr.co.kevit.localcsms.charger.entity.CsCertProvider;
import kr.co.kevit.localcsms.charger.entity.domain.CsCert;
import kr.co.kevit.localcsms.charger.entity.shared.CsCertSearchCond;
import kr.co.kevit.localcsms.charger.process.CsCertService;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author KEVIT <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 11. 10.
 */
@Service
public class CsCertServiceImpl implements CsCertService{
    
    @Autowired
    private CsCertProvider provider;
    
    @Autowired
    private CsCertCrlProvider crlProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCsCert(CsCert cert) {
        // 
        provider.registerCsCert(cert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCsCert(BigInteger certId, String status) {
        // 
        CsCert csCert = provider.retrieveCsCert(certId);
        provider.removeCsCert(certId);
        csCert.setCertStatus(status);
        crlProvider.registerCsCertCrl(csCert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CsCert retrieveCsCert(BigInteger certId) {
        // 
        return provider.retrieveCsCert(certId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CsCert> retrieveCsCertBySearchCond(CsCertSearchCond searchCond) {
        // 
        return provider.retrieveCsCertBySearchCond(searchCond);
    }
    
}

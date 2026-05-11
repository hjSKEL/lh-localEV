/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.certificate.process.logic;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.certificate.CsCertCrlProvider;
import kr.co.kevit.localcsms.certificate.entity.domain.CsCert;
import kr.co.kevit.localcsms.certificate.entity.domain.CsCertCrl;
import kr.co.kevit.localcsms.certificate.entity.shared.CsCertCrlSearchCond;
import kr.co.kevit.localcsms.certificate.CsCertCrlService;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author KEVIT <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 11. 10.
 */
@Service
public class CsCertCrlServiceImpl implements CsCertCrlService{
    
    @Autowired
    private CsCertCrlProvider provider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCsCertCrl(CsCertCrl cert) {
        // 
        provider.registerCsCertCrl(cert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CsCertCrl retrieveCsCertCrl(BigInteger certId) {
        // 
        return provider.retrieveCsCertCrl(certId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CsCert> retrieveCsCertCrlBySearchCond(CsCertCrlSearchCond searchCond) {
        // 
        return provider.retrieveCsCertCrlBySearchCond(searchCond);
    }
    
}

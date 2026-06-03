/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity;

import java.math.BigInteger;

import kr.co.kevit.localcsms.charger.entity.domain.CsCert;
import kr.co.kevit.localcsms.charger.entity.domain.CsCertCrl;
import kr.co.kevit.localcsms.charger.entity.shared.CsCertCrlSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author KEVIT <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 11. 10.
 */
public interface CsCertCrlProvider {
    
    /**
     * 
     * @param cert
     * @return
     */
    void registerCsCertCrl(CsCert cert);
    
    /**
     * 
     * @param certId
     * @return
     */
    CsCertCrl retrieveCsCertCrl(BigInteger certId);
    
    /**
     * 
     * @param searchCond
     * @return
     */
    Page<CsCert> retrieveCsCertCrlBySearchCond(CsCertCrlSearchCond searchCond);
}

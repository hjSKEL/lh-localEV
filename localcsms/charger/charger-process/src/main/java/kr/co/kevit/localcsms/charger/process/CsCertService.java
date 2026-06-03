/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process;

import java.math.BigInteger;

import kr.co.kevit.localcsms.charger.entity.domain.CsCert;
import kr.co.kevit.localcsms.charger.entity.shared.CsCertSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author KEVIT <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 11. 10.
 */
public interface CsCertService {
    
    /**
     * 
     * @param cert
     * @return
     */
    void registerCsCert(CsCert cert);
    
    /**
     * 
     * @param certId
     * @param status
     */
    void removeCsCert(BigInteger certId, String status);
    
    /**
     * 
     * @param certId
     * @return
     */
    CsCert retrieveCsCert(BigInteger certId);
    
    /**
     * 
     * @param searchCond
     * @return
     */
    Page<CsCert> retrieveCsCertBySearchCond(CsCertSearchCond searchCond);
}

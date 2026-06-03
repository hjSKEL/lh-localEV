/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.charger.entity.domain.CsCert;
import kr.co.kevit.localcsms.charger.entity.domain.CsCertCrl;
import kr.co.kevit.localcsms.charger.entity.shared.CsCertCrlSearchCond;

/**
 * 
 * @author KEVIT <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 11. 10.
 */
@Repository
public interface CsCertCrlMapper {
    
    /**
     * 
     * @param cert
     * @return
     */
    int insertCsCertCrl(@Param("cert")CsCert cert);
    
    /**
     * 
     * @param certId
     * @return
     */
    CsCertCrl selectCsCertCrl(@Param("certId")BigInteger certId);
    
    /**
     * 
     * @param searchCond
     * @return
     */
    int countCsCertCrlBySearchCond(@Param("searchCond")CsCertCrlSearchCond searchCond);
    
    /**
     * 
     * @param searchCond
     * @return
     */
    List<CsCert> selectCsCertCrlBySearchCond(@Param("searchCond")CsCertCrlSearchCond searchCond);

}

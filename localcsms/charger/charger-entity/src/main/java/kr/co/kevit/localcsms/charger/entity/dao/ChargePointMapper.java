/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.charger.entity.domain.ChargePoint;
import kr.co.kevit.localcsms.charger.entity.shared.ChargePointSearchCond;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 14.
 */
@Repository
public interface ChargePointMapper {
    
    int insertChargePoint(@Param("chargePoint") ChargePoint chargePoint);
    
    int updateChargePoint(@Param("chargePoint") ChargePoint chargePoint);
    
    ChargePoint selectChargePointByCpId(@Param("cpId") String cpId);
    
    List<ChargePoint> selectChargePointByCpIds(@Param("cpIds") List<String> cpIds);
    
    List<ChargePoint> selectAllChargePoint();
    
    int countChargePointBySearchCond(@Param("searchCond") ChargePointSearchCond searchCond);
    
    List<ChargePoint> selectChargePointBySearchCond(@Param("searchCond") ChargePointSearchCond searchCond);

    String selectMaxCpId(@Param("siGunCode") String siGunCode);

}

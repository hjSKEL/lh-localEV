/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingSearchCond;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 17.
 */
@Repository
public interface RechargingMapper {
    
    int insertRecharging(@Param("recharging") Recharging recharging);
    
    int insertRechargingError(@Param("recharging") Recharging recharging);
    
    int updateRecharging(@Param("recharging") Recharging recharging);
    
    Recharging selectRechargingById(@Param("rechargingId") String rechargingId);
    
    RechargingDto selectRechargingDtoById(String id);
    
    List<RechargingDto> selectRechargingDtoByIds(@Param("rechargingIds") List<String> ids);
    
    int countRechargingByRechargingSearchCond(@Param("searchCond") RechargingSearchCond searchCond);
    
    List<RechargingDto> selectRechargingByRechargingSearchCond(@Param("searchCond") RechargingSearchCond searchCond);
    
}

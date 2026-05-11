/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusInfoDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusSearchCond;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 8. 30.
 */
@Repository
public interface ChargerStatusMapper {
    
    int insertChargerStatus(@Param("chargerStatus") ChargerStatusInfo chargerStatusInfo);
    
    int updateChargerStatus(@Param("chargerStatus") ChargerStatusInfo chargerStatusInfo);
        
    List<ChargerStatusInfo> selectChargerStatusByCpIdNCsId(@Param("cpId") String cpId, @Param("csId") String csId);

    int countChargerStatusBySearchCond(@Param("searchCond") ChargerStatusSearchCond searchCond);
    
    List<ChargerStatusInfoDto> selectChargerStatusBySearchCond(@Param("searchCond") ChargerStatusSearchCond searchCond);

    int deleteChargerStatusByCpIdNCsId(@Param("cpId") String cpId, @Param("csId") String csId);
}

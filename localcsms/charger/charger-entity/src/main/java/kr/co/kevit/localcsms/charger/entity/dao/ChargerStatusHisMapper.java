/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerAuthHis;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfoHis;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerAuthHisSearchCond;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusInfoHisSearchCond;

/**
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 30.
 */
@Repository
public interface ChargerStatusHisMapper {

    int insertChargerStatusHis(@Param("chargerStatusInfo") ChargerStatusInfo chargerStatusInfo);

    int countChargerStatusInfoHisBySearchCond(@Param("searchCond") ChargerStatusInfoHisSearchCond searchCond);

    List<ChargerStatusInfoHis> selectChargerStatusInfoHisBySearchCond(@Param("searchCond") ChargerStatusInfoHisSearchCond searchCond);

    int countChargerAuthHisBySearchCond(@Param("searchCond") ChargerAuthHisSearchCond searchCond);

    List<ChargerAuthHis> selectChargerAuthHisBySearchCond(@Param("searchCond") ChargerAuthHisSearchCond searchCond);
}

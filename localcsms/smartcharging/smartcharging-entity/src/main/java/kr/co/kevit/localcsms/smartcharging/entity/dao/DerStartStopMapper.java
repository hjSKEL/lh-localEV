/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.smartcharging.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.smartcharging.entity.domain.DerStartStop;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerStartStopDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerStartStopSearchCond;

@Repository
public interface DerStartStopMapper {

    int insertStartStop(@Param("event") DerStartStop event);

    int countStartStopBySearchCond(@Param("searchCond") DerStartStopSearchCond cond);

    List<DerStartStopDto> selectStartStopBySearchCond(@Param("searchCond") DerStartStopSearchCond cond);
}

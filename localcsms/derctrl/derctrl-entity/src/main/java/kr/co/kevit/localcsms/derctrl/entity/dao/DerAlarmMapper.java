/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.derctrl.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.derctrl.entity.domain.DerAlarm;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerAlarmDto;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerAlarmSearchCond;

@Repository
public interface DerAlarmMapper {

    int insertAlarm(@Param("alarm") DerAlarm alarm);

    int countAlarmBySearchCond(@Param("searchCond") DerAlarmSearchCond cond);

    List<DerAlarmDto> selectAlarmBySearchCond(@Param("searchCond") DerAlarmSearchCond cond);
}

/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.smartcharging.entity;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerAlarm;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerAlarmDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerAlarmSearchCond;

public interface DerAlarmProvider {

    void registerAlarm(DerAlarm alarm);

    Page<DerAlarmDto> retrieveAlarmBySearchCond(DerAlarmSearchCond cond);
}

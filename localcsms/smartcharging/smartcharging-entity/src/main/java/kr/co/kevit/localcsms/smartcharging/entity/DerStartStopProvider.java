/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.smartcharging.entity;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerStartStop;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerStartStopDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerStartStopSearchCond;

public interface DerStartStopProvider {

    void registerStartStop(DerStartStop event);

    Page<DerStartStopDto> retrieveStartStopBySearchCond(DerStartStopSearchCond cond);
}

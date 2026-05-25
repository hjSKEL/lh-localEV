/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.derctrl.entity;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.derctrl.entity.domain.DerStartStop;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerStartStopDto;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerStartStopSearchCond;

public interface DerStartStopProvider {

    void registerStartStop(DerStartStop event);

    Page<DerStartStopDto> retrieveStartStopBySearchCond(DerStartStopSearchCond cond);
}

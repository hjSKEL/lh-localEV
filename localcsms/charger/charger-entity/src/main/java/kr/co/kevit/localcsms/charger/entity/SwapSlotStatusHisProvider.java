/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity;

import kr.co.kevit.localcsms.charger.entity.domain.SwapSlotStatusHis;
import kr.co.kevit.localcsms.charger.entity.shared.SwapSlotStatusHisDto;
import kr.co.kevit.localcsms.charger.entity.shared.SwapSlotStatusHisSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 슬롯 상태이력 Provider (C + R-list)
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface SwapSlotStatusHisProvider {

    void registerSwapSlotStatusHis(SwapSlotStatusHis history);

    Page<SwapSlotStatusHisDto> retrieveSwapSlotStatusHisBySearchCond(SwapSlotStatusHisSearchCond searchCond);

}

/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.process;

import kr.co.kevit.localcsms.batteryswap.entity.domain.SwapSlotStatusHis;
import kr.co.kevit.localcsms.batteryswap.entity.shared.SwapSlotStatusHisDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.SwapSlotStatusHisSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 슬롯 상태이력 — C(등록) + R(목록조회) 만 제공.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface SwapSlotStatusHisService {

    /** C — 등록 (append-only) */
    void registerSwapSlotStatusHis(SwapSlotStatusHis history);

    /** R — 목록 조회 */
    Page<SwapSlotStatusHisDto> retrieveSwapSlotStatusHisBySearchCond(SwapSlotStatusHisSearchCond searchCond);

}

/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.process;

import java.util.Map;

import kr.co.kevit.localcsms.batteryswap.entity.domain.SwapSlotStatus;
import kr.co.kevit.localcsms.batteryswap.entity.shared.SwapSlotStatusDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.SwapSlotStatusSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 슬롯 상태 CRUD.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface SwapSlotStatusService {

    /** C — 등록 */
    void registerSwapSlotStatus(SwapSlotStatus slot);

    /** U — 수정 */
    void modifySwapSlotStatus(SwapSlotStatus slot);

    /** D — 삭제 */
    void removeSwapSlotStatus(String cpId, String csId, int evseId);

    /** R — 단건 조회 */
    SwapSlotStatus retrieveSwapSlotStatus(String cpId, String csId, int evseId);

    /** R — 목록 조회 */
    Page<SwapSlotStatusDto> retrieveSwapSlotStatusBySearchCond(SwapSlotStatusSearchCond searchCond);

    /** R — 슬롯상태별 카운트 (key=slotState, value=count) */
    Map<String, Integer> retrieveSlotStateCountBySearchCond(SwapSlotStatusSearchCond searchCond);

}

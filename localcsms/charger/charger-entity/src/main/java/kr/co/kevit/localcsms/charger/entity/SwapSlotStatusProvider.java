/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity;

import java.util.Map;

import kr.co.kevit.localcsms.charger.entity.domain.SwapSlotStatus;
import kr.co.kevit.localcsms.charger.entity.shared.SwapSlotStatusDto;
import kr.co.kevit.localcsms.charger.entity.shared.SwapSlotStatusSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface SwapSlotStatusProvider {

    void registerSwapSlotStatus(SwapSlotStatus slot);

    void modifySwapSlotStatus(SwapSlotStatus slot);

    int removeSwapSlotStatus(String cpId, String csId, int evseId);

    SwapSlotStatus retrieveSwapSlotStatus(String cpId, String csId, int evseId);

    Page<SwapSlotStatusDto> retrieveSwapSlotStatusBySearchCond(SwapSlotStatusSearchCond searchCond);

    /** 슬롯상태별 카운트 (key=slotState, value=count). 결과는 입력 순서 보존. */
    Map<String, Integer> countSlotStateGroupBy(SwapSlotStatusSearchCond searchCond);

}

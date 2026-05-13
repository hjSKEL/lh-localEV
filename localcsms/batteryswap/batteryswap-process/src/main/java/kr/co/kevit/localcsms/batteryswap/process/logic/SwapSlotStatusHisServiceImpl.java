/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.process.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.batteryswap.entity.SwapSlotStatusHisProvider;
import kr.co.kevit.localcsms.batteryswap.entity.domain.SwapSlotStatusHis;
import kr.co.kevit.localcsms.batteryswap.entity.shared.SwapSlotStatusHisDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.SwapSlotStatusHisSearchCond;
import kr.co.kevit.localcsms.batteryswap.process.SwapSlotStatusHisService;
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Service
@Transactional
public class SwapSlotStatusHisServiceImpl implements SwapSlotStatusHisService {

    @Autowired
    private SwapSlotStatusHisProvider provider;

    @Override
    public void registerSwapSlotStatusHis(SwapSlotStatusHis history) {
        if (history.getCpId() == null || history.getCpId().isEmpty()) {
            throw new KEVITException("충전소ID가 비어 있습니다.");
        }
        if (history.getCsId() == null || history.getCsId().isEmpty()) {
            throw new KEVITException("충전기ID가 비어 있습니다.");
        }
        if (history.getEvseId() <= 0) {
            throw new KEVITException("슬롯번호(evseId)는 1 이상이어야 합니다.");
        }
        if (history.getNewState() == null || history.getNewState().isEmpty()) {
            throw new KEVITException("변경 후 상태(newState)가 비어 있습니다.");
        }
        if (history.getEventType() == null || history.getEventType().isEmpty()) {
            throw new KEVITException("트리거 유형(eventType)이 비어 있습니다.");
        }
        provider.registerSwapSlotStatusHis(history);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SwapSlotStatusHisDto> retrieveSwapSlotStatusHisBySearchCond(SwapSlotStatusHisSearchCond searchCond) {
        return provider.retrieveSwapSlotStatusHisBySearchCond(searchCond);
    }

}

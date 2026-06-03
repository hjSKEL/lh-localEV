/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process.logic;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.charger.entity.SwapSlotStatusProvider;
import kr.co.kevit.localcsms.charger.entity.domain.SwapSlotStatus;
import kr.co.kevit.localcsms.charger.entity.shared.SwapSlotStatusDto;
import kr.co.kevit.localcsms.charger.entity.shared.SwapSlotStatusSearchCond;
import kr.co.kevit.localcsms.charger.process.SwapSlotStatusService;
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Service
@Transactional
public class SwapSlotStatusServiceImpl implements SwapSlotStatusService {

    @Autowired
    private SwapSlotStatusProvider provider;

    @Override
    public void registerSwapSlotStatus(SwapSlotStatus slot) {
        validateKey(slot.getCpId(), slot.getCsId(), slot.getEvseId());
        if (slot.getSlotState() == null || slot.getSlotState().isEmpty()) {
            throw new KEVITException("슬롯상태(slotState)가 비어 있습니다.");
        }
        if (provider.retrieveSwapSlotStatus(slot.getCpId(), slot.getCsId(), slot.getEvseId()) != null) {
            throw new KEVITException("이미 등록된 슬롯 입니다.");
        }
        provider.registerSwapSlotStatus(slot);
    }

    @Override
    public void modifySwapSlotStatus(SwapSlotStatus slot) {
        validateKey(slot.getCpId(), slot.getCsId(), slot.getEvseId());
        if (provider.retrieveSwapSlotStatus(slot.getCpId(), slot.getCsId(), slot.getEvseId()) == null) {
            throw new KEVITException("등록되지 않은 슬롯 입니다.");
        }
        provider.modifySwapSlotStatus(slot);
    }

    @Override
    public void removeSwapSlotStatus(String cpId, String csId, int evseId) {
        validateKey(cpId, csId, evseId);
        if (provider.retrieveSwapSlotStatus(cpId, csId, evseId) == null) {
            throw new KEVITException("등록되지 않은 슬롯 입니다.");
        }
        provider.removeSwapSlotStatus(cpId, csId, evseId);
    }

    @Transactional(readOnly = true)
    @Override
    public SwapSlotStatus retrieveSwapSlotStatus(String cpId, String csId, int evseId) {
        return provider.retrieveSwapSlotStatus(cpId, csId, evseId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SwapSlotStatusDto> retrieveSwapSlotStatusBySearchCond(SwapSlotStatusSearchCond searchCond) {
        return provider.retrieveSwapSlotStatusBySearchCond(searchCond);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<String, Integer> retrieveSlotStateCountBySearchCond(SwapSlotStatusSearchCond searchCond) {
        return provider.countSlotStateGroupBy(searchCond);
    }

    private void validateKey(String cpId, String csId, int evseId) {
        if (cpId == null || cpId.isEmpty()) {
            throw new KEVITException("충전소ID가 비어 있습니다.");
        }
        if (csId == null || csId.isEmpty()) {
            throw new KEVITException("충전기ID가 비어 있습니다.");
        }
        if (evseId <= 0) {
            throw new KEVITException("슬롯번호(evseId)는 1 이상이어야 합니다.");
        }
    }

}

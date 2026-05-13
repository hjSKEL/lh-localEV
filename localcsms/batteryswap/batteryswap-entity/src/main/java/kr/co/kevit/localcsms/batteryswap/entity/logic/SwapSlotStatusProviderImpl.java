/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity.logic;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.batteryswap.entity.SwapSlotStatusProvider;
import kr.co.kevit.localcsms.batteryswap.entity.dao.SwapSlotStatusMapper;
import kr.co.kevit.localcsms.batteryswap.entity.domain.SwapSlotStatus;
import kr.co.kevit.localcsms.batteryswap.entity.shared.SwapSlotStatusDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.SwapSlotStatusSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Component
public class SwapSlotStatusProviderImpl implements SwapSlotStatusProvider {

    @Autowired
    private SwapSlotStatusMapper mapper;

    @Override
    public void registerSwapSlotStatus(SwapSlotStatus slot) {
        mapper.insertSwapSlotStatus(slot);
    }

    @Override
    public void modifySwapSlotStatus(SwapSlotStatus slot) {
        mapper.updateSwapSlotStatus(slot);
    }

    @Override
    public int removeSwapSlotStatus(String cpId, String csId, int evseId) {
        return mapper.deleteSwapSlotStatus(cpId, csId, evseId);
    }

    @Override
    public SwapSlotStatus retrieveSwapSlotStatus(String cpId, String csId, int evseId) {
        return mapper.selectSwapSlotStatus(cpId, csId, evseId);
    }

    @Override
    public Page<SwapSlotStatusDto> retrieveSwapSlotStatusBySearchCond(SwapSlotStatusSearchCond searchCond) {
        int totCnt = mapper.countSwapSlotStatusBySearchCond(searchCond);
        Page<SwapSlotStatusDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<SwapSlotStatusDto> result = mapper.selectSwapSlotStatusBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

    @Override
    public Map<String, Integer> countSlotStateGroupBy(SwapSlotStatusSearchCond searchCond) {
        List<Map<String, Object>> rows = mapper.countGroupBySlotState(searchCond);
        Map<String, Integer> result = new LinkedHashMap<>();
        if (rows == null) {
            return result;
        }
        for (Map<String, Object> row : rows) {
            Object key = row.get("slotState");
            Object cnt = row.get("cnt");
            if (key == null) {
                continue;
            }
            int n = (cnt instanceof Number) ? ((Number) cnt).intValue() : 0;
            result.put(key.toString(), n);
        }
        return result;
    }

}

/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.recharger.entity.BatterySwapRecordDetailProvider;
import kr.co.kevit.localcsms.recharger.entity.dao.BatterySwapRecordDetailMapper;
import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecordDetail;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Component
public class BatterySwapRecordDetailProviderImpl implements BatterySwapRecordDetailProvider {

    @Autowired
    private BatterySwapRecordDetailMapper mapper;

    @Override
    public void registerBatterySwapRecordDetail(BatterySwapRecordDetail detail) {
        mapper.insertBatterySwapRecordDetail(detail);
    }

    @Override
    public void modifyBatterySwapRecordDetail(BatterySwapRecordDetail detail) {
        mapper.updateBatterySwapRecordDetail(detail);
    }

    @Override
    public BatterySwapRecordDetail retrieveBatterySwapRecordDetail(Long requestId, int evseId) {
        return mapper.selectBatterySwapRecordDetail(requestId, evseId);
    }

    @Override
    public List<BatterySwapRecordDetail> retrieveBatterySwapRecordDetailByRequestId(Long requestId) {
        return mapper.selectBatterySwapRecordDetailByRequestId(requestId);
    }

}

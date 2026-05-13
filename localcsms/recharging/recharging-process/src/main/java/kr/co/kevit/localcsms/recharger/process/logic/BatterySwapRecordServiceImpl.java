/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.BatterySwapRecordDetailProvider;
import kr.co.kevit.localcsms.recharger.entity.BatterySwapRecordProvider;
import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecord;
import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecordDetail;
import kr.co.kevit.localcsms.recharger.entity.shared.BatterySwapRecordDto;
import kr.co.kevit.localcsms.recharger.entity.shared.BatterySwapRecordSearchCond;
import kr.co.kevit.localcsms.recharger.process.BatterySwapRecordService;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Service
@Transactional
public class BatterySwapRecordServiceImpl implements BatterySwapRecordService {

    @Autowired
    private BatterySwapRecordProvider provider;

    @Autowired
    private BatterySwapRecordDetailProvider detailProvider;

    @Override
    public void registerBatterySwapRecord(BatterySwapRecord record) {
        if (record.getRequestId() == null) {
            throw new KEVITException("requestId가 비어 있습니다.");
        }
        if (record.getStatus() == null || record.getStatus().isEmpty()) {
            throw new KEVITException("상태(status)가 비어 있습니다.");
        }
        if (provider.retrieveBatterySwapRecord(record.getRequestId()) != null) {
            throw new KEVITException("이미 등록된 교체 기록 입니다. requestId=" + record.getRequestId());
        }
        provider.registerBatterySwapRecord(record);
    }

    @Override
    public void modifyBatterySwapRecord(BatterySwapRecord record) {
        if (record.getRequestId() == null) {
            throw new KEVITException("requestId가 비어 있습니다.");
        }
        if (provider.retrieveBatterySwapRecord(record.getRequestId()) == null) {
            throw new KEVITException("등록되지 않은 교체 기록 입니다. requestId=" + record.getRequestId());
        }
        provider.modifyBatterySwapRecord(record);
    }

    @Transactional(readOnly = true)
    @Override
    public BatterySwapRecord retrieveBatterySwapRecord(Long requestId) {
        BatterySwapRecord record = provider.retrieveBatterySwapRecord(requestId);
        if (record != null) {
            List<BatterySwapRecordDetail> details = detailProvider.retrieveBatterySwapRecordDetailByRequestId(requestId);
            record.setDetails(details);
        }
        return record;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BatterySwapRecordDto> retrieveBatterySwapRecordBySearchCond(BatterySwapRecordSearchCond searchCond) {
        return provider.retrieveBatterySwapRecordBySearchCond(searchCond);
    }

}

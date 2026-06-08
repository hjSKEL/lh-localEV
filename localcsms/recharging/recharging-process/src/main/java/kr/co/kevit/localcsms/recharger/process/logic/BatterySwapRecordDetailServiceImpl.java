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
import kr.co.kevit.localcsms.recharger.entity.BatterySwapRecordDetailProvider;
import kr.co.kevit.localcsms.recharger.entity.BatterySwapRecordProvider;
import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecordDetail;
import kr.co.kevit.localcsms.recharger.process.BatterySwapRecordDetailService;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Service
@Transactional
public class BatterySwapRecordDetailServiceImpl implements BatterySwapRecordDetailService {

    @Autowired
    private BatterySwapRecordDetailProvider detailProvider;

    @Autowired
    private BatterySwapRecordProvider recordProvider;

    @Override
    public void registerBatterySwapRecordDetail(BatterySwapRecordDetail detail) {
        validateKey(detail.getRequestId(), detail.getEvseId());
        if (detail.getType() == null || detail.getType().isEmpty()) {
            throw new KEVITException("유형(type)이 비어 있습니다.");
        }
        if (recordProvider.retrieveBatterySwapRecord(detail.getRequestId()) == null) {
            throw new KEVITException("등록되지 않은 교체 기록 입니다. requestId=" + detail.getRequestId());
        }
        if (detailProvider.retrieveBatterySwapRecordDetail(detail.getRequestId(), detail.getEvseId()) != null) {
            throw new KEVITException("이미 등록된 디테일 입니다. (requestId, evseId)=("
                    + detail.getRequestId() + ", " + detail.getEvseId() + ")");
        }
        detailProvider.registerBatterySwapRecordDetail(detail);
    }

    @Override
    public void modifyBatterySwapRecordDetail(BatterySwapRecordDetail detail) {
        validateKey(detail.getRequestId(), detail.getEvseId());
        if (detailProvider.retrieveBatterySwapRecordDetail(detail.getRequestId(), detail.getEvseId()) == null) {
            throw new KEVITException("등록되지 않은 디테일 입니다.");
        }
        detailProvider.modifyBatterySwapRecordDetail(detail);
    }

    @Transactional(readOnly = true)
    @Override
    public BatterySwapRecordDetail retrieveBatterySwapRecordDetail(Long requestId, int evseId) {
        return detailProvider.retrieveBatterySwapRecordDetail(requestId, evseId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BatterySwapRecordDetail> retrieveBatterySwapRecordDetailByRequestId(Long requestId) {
        return detailProvider.retrieveBatterySwapRecordDetailByRequestId(requestId);
    }

    private void validateKey(Long requestId, int evseId) {
        if (requestId == null) {
            throw new KEVITException("requestId가 비어 있습니다.");
        }
        if (evseId <= 0) {
            throw new KEVITException("슬롯번호(evseId)는 1 이상이어야 합니다.");
        }
    }

}

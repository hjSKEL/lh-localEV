/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity;

import java.util.List;

import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecordDetail;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface BatterySwapRecordDetailProvider {

    void registerBatterySwapRecordDetail(BatterySwapRecordDetail detail);

    void modifyBatterySwapRecordDetail(BatterySwapRecordDetail detail);

    BatterySwapRecordDetail retrieveBatterySwapRecordDetail(Long requestId, int evseId);

    /** requestId 단위 디테일 전체 (상세 화면용, 페이징 없음). */
    List<BatterySwapRecordDetail> retrieveBatterySwapRecordDetailByRequestId(Long requestId);

}

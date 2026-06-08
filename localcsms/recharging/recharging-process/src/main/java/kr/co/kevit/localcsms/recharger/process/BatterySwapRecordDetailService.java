/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process;

import java.util.List;

import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecordDetail;

/**
 * 배터리 교체 기록 디테일 — C/R/U.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface BatterySwapRecordDetailService {

    /** C — 등록 */
    void registerBatterySwapRecordDetail(BatterySwapRecordDetail detail);

    /** U — 수정 */
    void modifyBatterySwapRecordDetail(BatterySwapRecordDetail detail);

    /** R — 단건 조회 */
    BatterySwapRecordDetail retrieveBatterySwapRecordDetail(Long requestId, int evseId);

    /** R — requestId 단위 디테일 전체 (페이징 없음) */
    List<BatterySwapRecordDetail> retrieveBatterySwapRecordDetailByRequestId(Long requestId);

}

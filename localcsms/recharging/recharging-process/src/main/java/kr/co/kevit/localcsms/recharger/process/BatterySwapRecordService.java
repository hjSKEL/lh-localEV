/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecord;
import kr.co.kevit.localcsms.recharger.entity.shared.BatterySwapRecordDto;
import kr.co.kevit.localcsms.recharger.entity.shared.BatterySwapRecordSearchCond;

/**
 * 배터리 교체 기록 — C/R/U + 목록조회.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface BatterySwapRecordService {

    /** C — 등록 */
    void registerBatterySwapRecord(BatterySwapRecord record);

    /** U — 수정 */
    void modifyBatterySwapRecord(BatterySwapRecord record);

    /** R — 단건 조회 */
    BatterySwapRecord retrieveBatterySwapRecord(Long requestId);

    /** R — 목록 조회 */
    Page<BatterySwapRecordDto> retrieveBatterySwapRecordBySearchCond(BatterySwapRecordSearchCond searchCond);

}

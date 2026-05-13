/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.process;

import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapPoint;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapPointDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapPointSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 배터리 교체 충전소 CRUD.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface BatterySwapPointService {

    /** C — 등록 */
    void registerBatterySwapPoint(BatterySwapPoint point);

    /** U — 수정 */
    void modifyBatterySwapPoint(BatterySwapPoint point);

    /** D — 삭제 (소속 교환충전기까지 함께 제거) */
    void removeBatterySwapPoint(String cpId);

    /** R — 단건 조회 (소속 교환충전기 목록 포함) */
    BatterySwapPoint retrieveBatterySwapPoint(String cpId);

    /** R — 목록 조회 (페이지) */
    Page<BatterySwapPointDto> retrieveBatterySwapPointBySearchCond(BatterySwapPointSearchCond searchCond);

}

/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.process;

import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapStation;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapStationDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapStationSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 배터리 교환 충전기 CRUD.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface BatterySwapStationService {

    /** C — 등록 */
    void registerBatterySwapStation(BatterySwapStation station);

    /** U — 수정 */
    void modifyBatterySwapStation(BatterySwapStation station);

    /** D — 삭제 */
    void removeBatterySwapStation(String cpId, String csId);

    /** R — 단건 조회 */
    BatterySwapStation retrieveBatterySwapStation(String cpId, String csId);

    /** R — 목록 조회 (페이지) */
    Page<BatterySwapStationDto> retrieveBatterySwapStationBySearchCond(BatterySwapStationSearchCond searchCond);

}

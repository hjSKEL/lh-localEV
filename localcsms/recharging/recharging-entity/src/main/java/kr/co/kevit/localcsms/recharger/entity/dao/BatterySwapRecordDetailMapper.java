/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecordDetail;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Repository
public interface BatterySwapRecordDetailMapper {

    int insertBatterySwapRecordDetail(@Param("detail") BatterySwapRecordDetail detail);

    int updateBatterySwapRecordDetail(@Param("detail") BatterySwapRecordDetail detail);

    BatterySwapRecordDetail selectBatterySwapRecordDetail(@Param("requestId") Long requestId,
                                                          @Param("evseId") int evseId);

    /** requestId 단위 디테일 전체 — 상세 화면용 (페이징 없음, IN 먼저 → OUT, 슬롯번호 오름차순) */
    List<BatterySwapRecordDetail> selectBatterySwapRecordDetailByRequestId(@Param("requestId") Long requestId);

}

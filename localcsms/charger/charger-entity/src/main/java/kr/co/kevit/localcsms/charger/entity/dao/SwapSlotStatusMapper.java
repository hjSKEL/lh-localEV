/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.charger.entity.domain.SwapSlotStatus;
import kr.co.kevit.localcsms.charger.entity.shared.SwapSlotStatusDto;
import kr.co.kevit.localcsms.charger.entity.shared.SwapSlotStatusSearchCond;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Repository
public interface SwapSlotStatusMapper {

    int insertSwapSlotStatus(@Param("slot") SwapSlotStatus slot);

    int updateSwapSlotStatus(@Param("slot") SwapSlotStatus slot);

    int deleteSwapSlotStatus(@Param("cpId") String cpId,
                             @Param("csId") String csId,
                             @Param("evseId") int evseId);

    SwapSlotStatus selectSwapSlotStatus(@Param("cpId") String cpId,
                                        @Param("csId") String csId,
                                        @Param("evseId") int evseId);

    int countSwapSlotStatusBySearchCond(@Param("searchCond") SwapSlotStatusSearchCond searchCond);

    List<SwapSlotStatusDto> selectSwapSlotStatusBySearchCond(@Param("searchCond") SwapSlotStatusSearchCond searchCond);

    /** 슬롯상태(SLOT_ST_CD)별 카운트 GROUP BY */
    List<Map<String, Object>> countGroupBySlotState(@Param("searchCond") SwapSlotStatusSearchCond searchCond);

}

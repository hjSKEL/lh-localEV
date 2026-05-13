/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.batteryswap.entity.domain.SwapSlotStatusHis;
import kr.co.kevit.localcsms.batteryswap.entity.shared.SwapSlotStatusHisDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.SwapSlotStatusHisSearchCond;

/**
 * 슬롯 상태이력 Mapper (C + R-list)
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Repository
public interface SwapSlotStatusHisMapper {

    int insertSwapSlotStatusHis(@Param("history") SwapSlotStatusHis history);

    int countSwapSlotStatusHisBySearchCond(@Param("searchCond") SwapSlotStatusHisSearchCond searchCond);

    List<SwapSlotStatusHisDto> selectSwapSlotStatusHisBySearchCond(@Param("searchCond") SwapSlotStatusHisSearchCond searchCond);

}

/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.recharger.entity.domain.RechargingAdjustment;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingAdjustmentDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingAdjustmentSearchCond;

/**
 * @since 2026. 9. 1.
 */
@Repository
public interface RechargingAdjustmentMapper {

    int insertRechargingAdjustment(@Param("adjustment") RechargingAdjustment adjustment);

    int countRechargingAdjustmentBySearchCond(@Param("searchCond") RechargingAdjustmentSearchCond searchCond);

    List<RechargingAdjustmentDto> selectRechargingAdjustmentBySearchCond(@Param("searchCond") RechargingAdjustmentSearchCond searchCond);

}

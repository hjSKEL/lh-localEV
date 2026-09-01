/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity;

import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.RechargingAdjustment;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingAdjustmentDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingAdjustmentSearchCond;

/**
 * @since 2026. 9. 1.
 */
@Component
public interface RechargingAdjustmentProvider {

    void registerRechargingAdjustment(RechargingAdjustment adjustment);

    Page<RechargingAdjustmentDto> retrieveRechargingAdjustmentBySearchCond(RechargingAdjustmentSearchCond searchCond);

}

/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.RechargingAdjustmentProvider;
import kr.co.kevit.localcsms.recharger.entity.domain.RechargingAdjustment;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingAdjustmentDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingAdjustmentSearchCond;
import kr.co.kevit.localcsms.recharger.process.RechargingAdjustmentService;

/**
 * @since 2026. 9. 1.
 */
@Service
@Transactional
public class RechargingAdjustmentServiceImpl implements RechargingAdjustmentService {

    @Autowired
    private RechargingAdjustmentProvider provider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerRechargingAdjustment(RechargingAdjustment adjustment) {
        //
        provider.registerRechargingAdjustment(adjustment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RechargingAdjustmentDto> retrieveRechargingAdjustmentBySearchCond(RechargingAdjustmentSearchCond searchCond) {
        //
        return provider.retrieveRechargingAdjustmentBySearchCond(searchCond);
    }

}

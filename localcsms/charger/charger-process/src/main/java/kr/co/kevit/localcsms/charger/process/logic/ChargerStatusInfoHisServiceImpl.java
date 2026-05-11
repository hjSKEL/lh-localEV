/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.charger.entity.ChargerStatusInfoHisProvider;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfoHis;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusInfoHisSearchCond;
import kr.co.kevit.localcsms.charger.process.ChargerStatusInfoHisService;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 16.
 */
@Service
@Transactional
public class ChargerStatusInfoHisServiceImpl implements ChargerStatusInfoHisService {

    @Autowired
    private ChargerStatusInfoHisProvider provider;

    @Transactional(readOnly = true)

    @Override
    public Page<ChargerStatusInfoHis> retrieveChargerStatusInfoHisBySearchCond(ChargerStatusInfoHisSearchCond searchCond) {
        //
        return provider.retrieveChargerStatusInfoHisBySearchCond(searchCond);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerChargerStatusHis(ChargerStatusInfo chargerStatusInfo) {
        // 
        provider.registerChargerStatusHis(chargerStatusInfo);
    }
}

/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.DischargingProvider;
import kr.co.kevit.localcsms.recharger.entity.domain.Discharging;
import kr.co.kevit.localcsms.recharger.entity.shared.DischargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.DischargingSearchCond;
import kr.co.kevit.localcsms.recharger.process.DischargingService;

@Service
public class DischargingServiceImpl implements DischargingService {

    @Autowired
    private DischargingProvider provider;

    @Override
    public void registerDischarging(Discharging discharging) {
        provider.registerDischarging(discharging);
    }

    @Override
    public void modifyDischarging(Discharging discharging) {
        provider.modifyDischarging(discharging);
    }

    @Override
    public Discharging retrieveDischargingById(String dcId) {
        return provider.retrieveDischargingById(dcId);
    }

    @Override
    public DischargingDto retrieveDischargingDtoById(String dcId) {
        return provider.retrieveDischargingDtoById(dcId);
    }

    @Override
    public Page<DischargingDto> retrieveDischargingBySearchCond(DischargingSearchCond searchCond) {
        return provider.retrieveDischargingBySearchCond(searchCond);
    }

    @Override
    public List<DischargingDto> retrieveDischargingByEvccId(String evccId) {
        return provider.retrieveDischargingByEvccId(evccId);
    }
}

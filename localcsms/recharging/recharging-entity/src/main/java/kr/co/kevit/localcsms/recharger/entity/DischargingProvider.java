/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.Discharging;
import kr.co.kevit.localcsms.recharger.entity.shared.DischargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.DischargingSearchCond;

@Component
public interface DischargingProvider {

    void registerDischarging(Discharging discharging);

    void modifyDischarging(Discharging discharging);

    Discharging retrieveDischargingById(String dcId);

    DischargingDto retrieveDischargingDtoById(String dcId);

    Page<DischargingDto> retrieveDischargingBySearchCond(DischargingSearchCond searchCond);

    List<DischargingDto> retrieveDischargingByEvccId(String evccId);
}

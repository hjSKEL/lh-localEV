/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingSearchCond;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 17.
 */
@Component
public interface RechargingProvider {
    
    void registerRecharging(Recharging recharging);
    
    void registerRechargingError(Recharging recharging);
    
    void modifyRecharging(Recharging recharging);
    
    Recharging retrieveRechargingById(String id);
    
    List<RechargingDto> retrieveRechargingDtoByIds(List<String> ids);
    
    Page<RechargingDto> retrieveRechargingByRechargingSearchCond(RechargingSearchCond searchCond);
    
    List<RechargingDto> retrieveRecharging4DownloadByRechargingSearchCond(RechargingSearchCond searchCond);
    
    RechargingDto retrieveRechargingDtoById(String id);
}

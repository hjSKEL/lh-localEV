/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingSearchCond;

import java.util.List;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 17.
 */
public interface RechargingService {
    
    /**
     * for IF
     * @param id
     * @return
     */
    Recharging retrieveRecharging4IfById(String id);
    
    /**
     * for IF
     * @param recharging
     * @return
     */
    void registerRecharging(Recharging recharging);
    
    /**
     * for IF
     * @param recharging
     * @return
     */
    void modifyRecharging(Recharging recharging);

    /**
     * 진행 중(RECS02) 트랜잭션의 최대 에너지 한도(Wh) 변경.
     * 0 이면 한도 해제. 진행 중이 아니면 예외.
     */
    void modifyMaxEnergy(String rechargingId, Double maxEnergy, String updUserId);

    void completeRecharging(Recharging recharging);
    
    RechargingDto retrieveRechargingById(String id);
    
    List<RechargingDto> retrieveRechargingDtoByIds(List<String> ids);
    
    Page<RechargingDto> retrieveRechargingByRechargingSearchCond(RechargingSearchCond searchCond);
    
    List<RechargingDto> retrieveRecharging4DownloadByRechargingSearchCond(RechargingSearchCond searchCond);
    
    Page<RechargingDto> retrieveRechargingWithCustomerByRechargingSearchCond(RechargingSearchCond searchCond);
    
    List<RechargingDto> retrieveRechargingWithCustomer4DownloadByRechargingSearchCond(RechargingSearchCond searchCond);
}

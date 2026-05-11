/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfoHis;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusInfoHisSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 1. 16.
 */
public interface ChargerStatusInfoHisService {
    
    void registerChargerStatusHis(ChargerStatusInfo chargerStatusInfo);
    
    Page<ChargerStatusInfoHis> retrieveChargerStatusInfoHisBySearchCond(ChargerStatusInfoHisSearchCond searchCond);
}

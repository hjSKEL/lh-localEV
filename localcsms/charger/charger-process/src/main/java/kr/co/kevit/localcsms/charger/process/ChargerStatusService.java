/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process;

import java.util.List;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusInfoDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */
public interface ChargerStatusService {

    /**
     * IF
     */
    List<ChargerStatusInfo> retrieveChargerStatusByCpIdNCsId(String cpId, String csId);
    
    /**
     * IF
     */
    void modifyChargerStatus(ChargerStatusInfo chargerStatusInfo);
    
    /**
     * IF
     */
    void modifyChargerStatusWithoutHis(ChargerStatusInfo chargerStatusInfo);

    Page<ChargerStatusInfoDto> retrieveChargerStatusBySearchCond(ChargerStatusSearchCond searchCond);

}

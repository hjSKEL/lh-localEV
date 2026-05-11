/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process;

import kr.co.kevit.localcsms.charger.entity.shared.BreakdownMgtInfoDto;
import kr.co.kevit.localcsms.charger.entity.shared.BreakdownSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
public interface BreakdownMgtInfoService {
    
    Page<BreakdownMgtInfoDto> retrieveBreakdownMgtInfoBySearchCond(BreakdownSearchCond searchCond);
    
    BreakdownMgtInfoDto retrieveBreakdownMgtInfoById(String id);

}

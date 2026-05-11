/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process;

import kr.co.kevit.localcsms.charger.entity.domain.BreakdownInfo;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
public interface BreakdownInfoService {
    
    void registerBreakdownInfo(BreakdownInfo info);
    
    void modifyBreakdownInfo(BreakdownInfo info);
    
    BreakdownInfo retrieveBreakdownInfo(String id);

}

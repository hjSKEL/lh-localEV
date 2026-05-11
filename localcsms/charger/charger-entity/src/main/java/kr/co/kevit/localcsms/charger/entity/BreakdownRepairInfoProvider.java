/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity;

import kr.co.kevit.localcsms.charger.entity.domain.BreakdownRepairInfo;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
public interface BreakdownRepairInfoProvider {
    
    void registerBreakdownRepairInfo(BreakdownRepairInfo info);
    
    void modifyBreakdownRepairInfo(BreakdownRepairInfo info);
    
    BreakdownRepairInfo retrieveBreakdownRepairInfo(String id);

}

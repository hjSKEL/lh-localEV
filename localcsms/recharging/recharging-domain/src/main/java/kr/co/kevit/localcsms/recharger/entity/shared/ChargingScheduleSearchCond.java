/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 10. 16.
 */
public class ChargingScheduleSearchCond extends PageCriteria{
    
    private String csUniqId;

    /**
     * Get csUniqId
     * @return csUniqId
     */
    public String getCsUniqId() {
        return csUniqId;
    }

    /**
     * Set csUniqId
     * @param csUniqId
     */
    public void setCsUniqId(String csUniqId) {
        this.csUniqId = csUniqId;
    }

}

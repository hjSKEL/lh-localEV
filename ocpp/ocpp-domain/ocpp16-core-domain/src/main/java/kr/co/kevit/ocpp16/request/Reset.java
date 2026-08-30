/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import kr.co.kevit.ocpp16.enumtype.ResetTypeEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class Reset {
    
    /**
     * required
     */
    private ResetTypeEnum type;

    public ResetTypeEnum getType() {
        return type;
    }

    public void setType(ResetTypeEnum type) {
        this.type = type;
    }
}

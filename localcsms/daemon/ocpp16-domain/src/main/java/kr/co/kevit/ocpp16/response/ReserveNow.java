/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.response;

import kr.co.kevit.ocpp16.enumtype.ReserveNowStatusEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class ReserveNow {
    
    private ReserveNowStatusEnum status;

    public ReserveNowStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ReserveNowStatusEnum status) {
        this.status = status;
    }

}

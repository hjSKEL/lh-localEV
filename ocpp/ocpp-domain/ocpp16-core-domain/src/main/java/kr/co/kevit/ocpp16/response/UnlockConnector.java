/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.ocpp16.response;

import kr.co.kevit.ocpp16.enumtype.UnlockConnectorResponseStatusEnum;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 2. 22.
 */
public class UnlockConnector {

    private UnlockConnectorResponseStatusEnum status;

    public UnlockConnectorResponseStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UnlockConnectorResponseStatusEnum status) {
        this.status = status;
    }
}

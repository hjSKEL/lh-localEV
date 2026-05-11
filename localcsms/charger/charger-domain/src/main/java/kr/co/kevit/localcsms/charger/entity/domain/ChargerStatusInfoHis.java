/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;

/**
 * 충전 상태 정보 이력 TB : TB_CHCS006
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 06. 10.
 */
public class ChargerStatusInfoHis extends ChargerStatusInfo implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -6731215134836062556L;

    private long csStatusId;

    public long getCsStatusId() {
        return csStatusId;
    }

    public void setCsStatusId(long csStatusId) {
        this.csStatusId = csStatusId;
    }

}

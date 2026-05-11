/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.domain;

import java.util.Date;

/**
 * 충전기 CSM 정보
 * TB_CHCS002
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 12.
 */
public class ChargingStationCsm extends ChargingStation {

    /**
     * UID
     */
    private static final long serialVersionUID = -3997536702701966750L;
    
    /**
     * 충전기 버전
     * FW_VER` VARCHAR(20)
     */
    private String fwVer;
    
    /**
     * 모델 명
     * MODEL_NM
     */
    private String modelName;
    
    /**
     * 시리얼번호
     * SER_NO
     */
    private String serialNumber;
    
    /**
     * 최근 부팅 시간
     * LAST_BOOT_DT` DATETIME
     */
    private Date lastBootDate;

    /**
     * Get fwVer
     * @return fwVer
     */
    public String getFwVer() {
        return fwVer;
    }

    /**
     * Set fwVer
     * @param fwVer
     */
    public void setFwVer(String fwVer) {
        this.fwVer = fwVer;
    }

    /**
     * Get lastBootDate
     * @return lastBootDate
     */
    public Date getLastBootDate() {
        return lastBootDate;
    }

    /**
     * Set lastBootDate
     * @param lastBootDate
     */
    public void setLastBootDate(Date lastBootDate) {
        this.lastBootDate = lastBootDate;
    }

    /**
     * Get modelName
     * @return modelName
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * Set modelName
     * @param modelName
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    /**
     * Get serialNumber
     * @return serialNumber
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Set serialNumber
     * @param serialNumber
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
}

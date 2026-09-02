/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * TB_SYCF001
 * 시스템설정 (싱글턴, ID=1 고정 1행)
 * @since 2026. 9. 2.
 */
public class SystemConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 고정값 1(싱글턴)
     * ID     INT    NOT NULL
     */
    private Integer id;

    /**
     * 이상충전 판단 충전시간(시간)
     * CHARGING_TIME_BASED_ON_ABNORMAL_CHARGING     INT
     */
    private Integer chargingTimeBasedOnAbnormalCharging;

    /**
     * 이상충전 판단 충전량(Wh)
     * ABNORMAL_CHARGE_AMOUNT     INT
     */
    private Integer abnormalChargeAmount;

    /**
     * 설치장소
     * EMPLACEMENT     VARCHAR(250)
     */
    private String emplacement;

    /**
     * 서버주소
     * SERVER_ADDRESS     VARCHAR(250)
     */
    private String serverAddress;

    /**
     * 홈넷적용 (TB_SYCO001.HNET00 하위 코드)
     * USE_HOME_NET     VARCHAR(8)
     */
    private String useHomeNet;

    /**
     * 파일저장경로
     * FILE_STORE_PATH     VARCHAR(250)
     */
    private String fileStorePath;

    /**
     * 고객지원
     * CUSTOMER_SUPPORT     VARCHAR(250)
     */
    private String customerSupport;

    /**
     * DB백업 경로
     * DB_BACKUP_PATH     VARCHAR(250)
     */
    private String dbBackupPath;

    /**
     * DB백업 주기(일)
     * DB_BACKUP_CYCLE     INT
     */
    private Integer dbBackupCycle;

    /**
     * DB백업 보관기간(일)
     * DB_BACKUP_RETENTION_PERIOD     INT
     */
    private Integer dbBackupRetentionPeriod;

    private Date regDt;

    private String regId;

    private Date updDt;

    private String updId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChargingTimeBasedOnAbnormalCharging() {
        return chargingTimeBasedOnAbnormalCharging;
    }

    public void setChargingTimeBasedOnAbnormalCharging(Integer chargingTimeBasedOnAbnormalCharging) {
        this.chargingTimeBasedOnAbnormalCharging = chargingTimeBasedOnAbnormalCharging;
    }

    public Integer getAbnormalChargeAmount() {
        return abnormalChargeAmount;
    }

    public void setAbnormalChargeAmount(Integer abnormalChargeAmount) {
        this.abnormalChargeAmount = abnormalChargeAmount;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getUseHomeNet() {
        return useHomeNet;
    }

    public void setUseHomeNet(String useHomeNet) {
        this.useHomeNet = useHomeNet;
    }

    public String getFileStorePath() {
        return fileStorePath;
    }

    public void setFileStorePath(String fileStorePath) {
        this.fileStorePath = fileStorePath;
    }

    public String getCustomerSupport() {
        return customerSupport;
    }

    public void setCustomerSupport(String customerSupport) {
        this.customerSupport = customerSupport;
    }

    public String getDbBackupPath() {
        return dbBackupPath;
    }

    public void setDbBackupPath(String dbBackupPath) {
        this.dbBackupPath = dbBackupPath;
    }

    public Integer getDbBackupCycle() {
        return dbBackupCycle;
    }

    public void setDbBackupCycle(Integer dbBackupCycle) {
        this.dbBackupCycle = dbBackupCycle;
    }

    public Integer getDbBackupRetentionPeriod() {
        return dbBackupRetentionPeriod;
    }

    public void setDbBackupRetentionPeriod(Integer dbBackupRetentionPeriod) {
        this.dbBackupRetentionPeriod = dbBackupRetentionPeriod;
    }

    public Date getRegDt() {
        return regDt;
    }

    public void setRegDt(Date regDt) {
        this.regDt = regDt;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public Date getUpdDt() {
        return updDt;
    }

    public void setUpdDt(Date updDt) {
        this.updDt = updDt;
    }

    public String getUpdId() {
        return updId;
    }

    public void setUpdId(String updId) {
        this.updId = updId;
    }

}

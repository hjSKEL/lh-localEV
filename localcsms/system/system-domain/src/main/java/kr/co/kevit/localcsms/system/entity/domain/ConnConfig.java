/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * TB_SYCN001
 * 연결설정 (싱글턴, ID=1 고정 1행)
 * @since 2026. 9. 2.
 */
public class ConnConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 고정값 1(싱글턴)
     * ID     INT    NOT NULL
     */
    private Integer id;

    /**
     * LH CSMS 주소
     * LH_CSMS_ADDRESS     VARCHAR(250)
     */
    private String lhCsmsAddress;

    /**
     * CPO CSMS 주소
     * CPO_CSMS_ADDRESS     VARCHAR(250)
     */
    private String cpoCsmsAddress;

    /**
     * 로컬시스템 아이디
     * LOCAL_SYSTEM_ID     VARCHAR(250)
     */
    private String localSystemId;

    /**
     * 로컬시스템 시리얼넘버
     * LOCAL_SYSTEM_SN     VARCHAR(250)
     */
    private String localSystemSn;

    /**
     * 로컬서버 운영모드 (TB_SYCO001.OPMD00 하위 코드)
     * LOCAL_OPERATION_TYPE     VARCHAR(8)
     */
    private String localOperationType;

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

    public String getLhCsmsAddress() {
        return lhCsmsAddress;
    }

    public void setLhCsmsAddress(String lhCsmsAddress) {
        this.lhCsmsAddress = lhCsmsAddress;
    }

    public String getCpoCsmsAddress() {
        return cpoCsmsAddress;
    }

    public void setCpoCsmsAddress(String cpoCsmsAddress) {
        this.cpoCsmsAddress = cpoCsmsAddress;
    }

    public String getLocalSystemId() {
        return localSystemId;
    }

    public void setLocalSystemId(String localSystemId) {
        this.localSystemId = localSystemId;
    }

    public String getLocalSystemSn() {
        return localSystemSn;
    }

    public void setLocalSystemSn(String localSystemSn) {
        this.localSystemSn = localSystemSn;
    }

    public String getLocalOperationType() {
        return localOperationType;
    }

    public void setLocalOperationType(String localOperationType) {
        this.localOperationType = localOperationType;
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

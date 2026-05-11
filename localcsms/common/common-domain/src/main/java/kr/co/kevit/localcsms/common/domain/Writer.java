/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 공통 - 등록정보
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 4. 2.
 */
public class Writer implements Serializable {

    /**  */
    private static final long serialVersionUID = 328850302740641471L;

    public Writer() {
        //
        this.regUserId = null;
        this.updUserId = null;
        this.registrationDate = new Date();
        this.updateDate = new Date();
    }

    public Writer(String user) {
        this.regUserId = user;
        this.updUserId = user;
        this.registrationDate = new Date();
        this.updateDate = new Date();
    }

    /**
     * 등록일 REG_DT
     *
     */
    private Date registrationDate;

    /**
     * 등록자 REG_ID
     */
    private String regUserId;
    
    /**
     * 등록자 명 (VO)
     */
    private String regUserName;

    /**
     * 수정일 UPD_DT
     *
     */
    private Date updateDate;

    /**
     * 수정자 UPD_ID
     */
    private String updUserId;
    
    /**
     * 수정자 명(VO)
     */
    private String updUserName;

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegUserId() {
        return regUserId;
    }

    public void setRegUserId(String regUserId) {
        this.regUserId = regUserId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdUserId() {
        return updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    /**
     * Get regUserName
     * @return regUserName
     */
    public String getRegUserName() {
        return regUserName;
    }

    /**
     * Set regUserName
     * @param regUserName
     */
    public void setRegUserName(String regUserName) {
        this.regUserName = regUserName;
    }

    /**
     * Get updUserName
     * @return updUserName
     */
    public String getUpdUserName() {
        return updUserName;
    }

    /**
     * Set updUserName
     * @param updUserName
     */
    public void setUpdUserName(String updUserName) {
        this.updUserName = updUserName;
    }
    
}

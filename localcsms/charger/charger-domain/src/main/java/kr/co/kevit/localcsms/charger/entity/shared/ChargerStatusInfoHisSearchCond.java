/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.shared;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 16.
 */
public class ChargerStatusInfoHisSearchCond extends PageCriteria implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 7826350870652221199L;

    private String cpId;

    private String csId;
    
    private int evseId = 0;

    private String fromDate;

    private String toDate;
    
    private int csStatusId = 0;

    /**
     * Get cpId
     * @return cpId
     */
    public String getCpId() {
        return cpId;
    }

    /**
     * Set cpId
     * @param cpId
     */
    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    /**
     * Get csId
     * @return csId
     */
    public String getCsId() {
        return csId;
    }

    /**
     * Set csId
     * @param csId
     */
    public void setCsId(String csId) {
        this.csId = csId;
    }

    /**
     * Get evseId
     * @return evseId
     */
    public int getEvseId() {
        return evseId;
    }

    /**
     * Set evseId
     * @param evseId
     */
    public void setEvseId(int evseId) {
        this.evseId = evseId;
    }

    /**
     * Get fromDate
     * @return fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Set fromDate
     * @param fromDate
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Get toDate
     * @return toDate
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * Set toDate
     * @param toDate
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    /**
     * Get csStatusId
     * @return csStatusId
     */
    public int getCsStatusId() {
        return csStatusId;
    }

    /**
     * Set csStatusId
     * @param csStatusId
     */
    public void setCsStatusId(int csStatusId) {
        this.csStatusId = csStatusId;
    }
    
}
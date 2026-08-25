/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.shared;

import java.io.Serializable;
import java.util.List;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 2.
 */
public class ChargerStatusSearchCond extends PageCriteria implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -6713197314907534981L;

    private String cpId;
    
    private String csId;
    
    private String cpName;

    private String cxName;

    private String makerType;

    private List<String> status;

    private String csErrorStatus;

    /**
     * 정렬 기준
     *  A:단지 ASC   B:단지 DESC
     *  C:충전소명 ASC D:충전소명 DESC
     *  E:충전기ID ASC F:충전기ID DESC
     */
    private String sortOrder = "C";

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

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
     * Get makerType
     * @return makerType
     */
    public String getMakerType() {
        return makerType;
    }

    /**
     * Set makerType
     * @param makerType
     */
    public void setMakerType(String makerType) {
        this.makerType = makerType;
    }

    /**
     * Get status
     * @return status
     */
    public List<String> getStatus() {
        return status;
    }

    /**
     * Set status
     * @param status
     */
    public void setStatus(List<String> status) {
        this.status = status;
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
     * Get csErrorStatus
     * @return csErrorStatus
     */
    public String getCsErrorStatus() {
        return csErrorStatus;
    }

    /**
     * Set csErrorStatus
     * @param csErrorStatus
     */
    public void setCsErrorStatus(String csErrorStatus) {
        this.csErrorStatus = csErrorStatus;
    }

    /**
     * Get cpName
     * @return cpName
     */
    public String getCpName() {
        return cpName;
    }

    /**
     * Set cpName
     * @param cpName
     */
    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    /**
     * Get cxName
     * @return cxName
     */
    public String getCxName() {
        return cxName;
    }

    /**
     * Set cxName
     * @param cxName
     */
    public void setCxName(String cxName) {
        this.cxName = cxName;
    }

}

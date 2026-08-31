/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 10. 29.
 */
public class ChargingStationSearchCond extends PageCriteria {

    private String cpId;

    private String cpName;

    private String makerType;
    
    private String csKindType;

    private String ocppVersion;

    /** 충전 서비스 방식 (CSST00): CSST01=일반충전, CSST02=배터리교환 */
    private String csServiceType;

    /**
     * 정렬 (A/B=충전소명, C/D=충전기ID, E/F=충전기용량, G/H=설치년월, I/J=등록일, 각 오름/내림. 기본은 CP_NM)
     */
    private String sortOrder;

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Get cpId
     * 
     * @return cpId
     */
    public String getCpId() {
        return cpId;
    }

    /**
     * Set cpId
     * 
     * @param cpId
     */
    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    /**
     * Get cpName
     * 
     * @return cpName
     */
    public String getCpName() {
        return cpName;
    }

    /**
     * Set cpName
     * 
     * @param cpName
     */
    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    /**
     * Get makerType
     * 
     * @return makerType
     */
    public String getMakerType() {
        return makerType;
    }

    /**
     * Set makerType
     * 
     * @param makerType
     */
    public void setMakerType(String makerType) {
        this.makerType = makerType;
    }

    /**
     * Get csKindType
     * @return csKindType
     */
    public String getCsKindType() {
        return csKindType;
    }

    /**
     * Set csKindType
     * @param csKindType
     */
    public void setCsKindType(String csKindType) {
        this.csKindType = csKindType;
    }

    /**
     * Get ocppVersion
     * @return ocppVersion
     */
    public String getOcppVersion() {
        return ocppVersion;
    }

    /**
     * Set ocppVersion
     * @param ocppVersion
     */
    public void setOcppVersion(String ocppVersion) {
        this.ocppVersion = ocppVersion;
    }

    /** Get csServiceType @return csServiceType */
    public String getCsServiceType() {
        return csServiceType;
    }

    /** Set csServiceType @param csServiceType */
    public void setCsServiceType(String csServiceType) {
        this.csServiceType = csServiceType;
    }

}

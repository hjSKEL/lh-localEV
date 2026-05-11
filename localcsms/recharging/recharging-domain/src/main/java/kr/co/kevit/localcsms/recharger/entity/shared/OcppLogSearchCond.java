/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 7. 2.
 */
public class OcppLogSearchCond extends PageCriteria{

    /**
     * Maximum of 36 characters, to allow for GUIDs
     */
    private String id;
    
    /**
     * MessageTypeId
     * 2 : 요청
     * 3 : 응답
     * 4 : 응답 에러
     */
    private String messageTypeId;
    
    /**
     * 충전소 ID
     */
    private String cpId;
    
    /**
     * 충전기 ID
     */
    private String csId;
    
    /**
     * Action name
     */
    private String action;
    
    /**
     * 서버 수집 시간
     */
    private String fromDate;
    
    /**
     * 
     */
    private String toDate;

    /**
     * Get id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Set id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get messageTypeId
     * @return messageTypeId
     */
    public String getMessageTypeId() {
        return messageTypeId;
    }

    /**
     * Set messageTypeId
     * @param messageTypeId
     */
    public void setMessageTypeId(String messageTypeId) {
        this.messageTypeId = messageTypeId;
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
     * Get action
     * @return action
     */
    public String getAction() {
        return action;
    }

    /**
     * Set action
     * @param action
     */
    public void setAction(String action) {
        this.action = action;
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

}

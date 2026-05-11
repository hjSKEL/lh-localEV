/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;
import java.util.List;

/**
 * Reservation Search Condition
 */
public class ReservationSearchCond extends PageCriteria {

    private String cpId;
    private String csId;
    private String cutCardNo;
    private List<String> status;
    private Long rsvId;

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getCsId() {
        return csId;
    }

    public void setCsId(String csId) {
        this.csId = csId;
    }

    public String getCutCardNo() {
        return cutCardNo;
    }

    public void setCutCardNo(String cutCardNo) {
        this.cutCardNo = cutCardNo;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public Long getRsvId() {
        return rsvId;
    }

    public void setRsvId(Long rsvId) {
        this.rsvId = rsvId;
    }
}

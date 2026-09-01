/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 조정내역(TB_RCRC003) 목록 검색조건
 *
 * @since 2026. 9. 1.
 */
public class RechargingAdjustmentSearchCond extends PageCriteria {

    private String rechargingId;

    /** 조정등록일시(REG_DT) 검색 범위 - 'YYYY-MM-DD HH:mm:ss' */
    private String fromDate;

    private String toDate;

    public String getRechargingId() {
        return rechargingId;
    }

    public void setRechargingId(String rechargingId) {
        this.rechargingId = rechargingId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

}

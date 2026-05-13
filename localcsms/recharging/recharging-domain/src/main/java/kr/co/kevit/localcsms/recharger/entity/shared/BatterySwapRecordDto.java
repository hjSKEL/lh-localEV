/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.shared;

import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecord;

/**
 * 배터리 교체 기록 DTO (목록조회용)
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class BatterySwapRecordDto extends BatterySwapRecord {

    /** UID */
    private static final long serialVersionUID = -1184430721081202270L;

    /** 충전소명 (조인) */
    private String cpName;

    /** BatteryIn 측 고객명 (조인) */
    private String inCustomerName;

    /** BatteryOut 측 고객명 (조인) */
    private String outCustomerName;

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getInCustomerName() {
        return inCustomerName;
    }

    public void setInCustomerName(String inCustomerName) {
        this.inCustomerName = inCustomerName;
    }

    public String getOutCustomerName() {
        return outCustomerName;
    }

    public void setOutCustomerName(String outCustomerName) {
        this.outCustomerName = outCustomerName;
    }

}

/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 충전이력 조정내역 (TB_RCRC003) - TB_RCRC001.RC_ID 기준으로 값만 연결(FK 없음)
 *
 * @since 2026. 9. 1.
 */
public class RechargingAdjustment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long seq;

    private String rechargingId;

    private Integer adjustAmount;

    private String adjustReason;

    private Date regDate;

    private String regId;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getRechargingId() {
        return rechargingId;
    }

    public void setRechargingId(String rechargingId) {
        this.rechargingId = rechargingId;
    }

    public Integer getAdjustAmount() {
        return adjustAmount;
    }

    public void setAdjustAmount(Integer adjustAmount) {
        this.adjustAmount = adjustAmount;
    }

    public String getAdjustReason() {
        return adjustReason;
    }

    public void setAdjustReason(String adjustReason) {
        this.adjustReason = adjustReason;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

}

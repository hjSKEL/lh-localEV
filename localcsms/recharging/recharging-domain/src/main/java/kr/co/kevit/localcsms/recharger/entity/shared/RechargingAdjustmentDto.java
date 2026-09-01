/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.shared;

import java.util.Date;

import kr.co.kevit.localcsms.recharger.entity.domain.RechargingAdjustment;

/**
 * 조정내역 목록/충전이력 조회 전용 - TB_RCRC001/TB_CHCP001/TB_ORCX001/TB_CUCU001 조인 결과(저장 안 함)
 *
 * @since 2026. 9. 1.
 */
public class RechargingAdjustmentDto extends RechargingAdjustment {

    private static final long serialVersionUID = 1L;

    private String cutCardNo;
    private Date chStartDate;
    private Date chEndDate;
    private String cpName;
    private String complexName;
    private String custName;
    private String dong;
    private String ho;

    public String getCutCardNo() {
        return cutCardNo;
    }

    public void setCutCardNo(String cutCardNo) {
        this.cutCardNo = cutCardNo;
    }

    public Date getChStartDate() {
        return chStartDate;
    }

    public void setChStartDate(Date chStartDate) {
        this.chStartDate = chStartDate;
    }

    public Date getChEndDate() {
        return chEndDate;
    }

    public void setChEndDate(Date chEndDate) {
        this.chEndDate = chEndDate;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getComplexName() {
        return complexName;
    }

    public void setComplexName(String complexName) {
        this.complexName = complexName;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getDong() {
        return dong;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

}

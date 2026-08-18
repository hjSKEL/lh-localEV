/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.shared;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 16.
 */
public class ChargerStatusInfoDto extends ChargerStatusInfo{
    
    /**  */
    private static final long serialVersionUID = -2898382509078354766L;

    private String cpName;

    /**
     * 단지명 (TB_ORCX001 조인 조회 전용, 저장 안 함)
     */
    private String complexName;

    /**
     * 사용자명 (CUT_CRD_NO -> TB_CUCU002 -> TB_CUCU001 조인 조회 전용, 저장 안 함)
     */
    private String custName;

    /**
     * 거주지 동 (CUT_CRD_NO -> TB_CUCU002 -> TB_CUCU001 조인 조회 전용, 저장 안 함)
     */
    private String dong;

    /**
     * 거주지 호 (CUT_CRD_NO -> TB_CUCU002 -> TB_CUCU001 조인 조회 전용, 저장 안 함)
     */
    private String ho;

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

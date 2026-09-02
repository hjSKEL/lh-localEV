/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.shared;

import java.io.Serializable;

/**
 * 월별 고객별 충전요금 집계(엑셀 다운로드 전용) - TB_RCRC001/TB_CUCU001 조인 후 고객 단위 합계, 저장 안 함
 *
 * @since 2026. 9. 2.
 */
public class RechargingMonthlyCustomerDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dong;
    private String ho;
    private String custName;

    /** 해당 월 충전 횟수 */
    private Integer totalChCnt;

    /** 해당 월 사용금액(CH_US_SUM) 합계 */
    private Long totalChUsSum;

    /** 해당 월 조정금액(TB_RCRC003) 합계 */
    private Long totalAdjAmtSum;

    /** 해당 월 청구금액(사용금액+조정금액) 합계 */
    private Long totalPaySum;

    /** 조회 대상 연도 - 엑셀 제목 표시용, 행마다 동일 */
    private Integer year;

    /** 조회 대상 월 - 엑셀 제목 표시용, 행마다 동일 */
    private Integer month;

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

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public Integer getTotalChCnt() {
        return totalChCnt;
    }

    public void setTotalChCnt(Integer totalChCnt) {
        this.totalChCnt = totalChCnt;
    }

    public Long getTotalChUsSum() {
        return totalChUsSum;
    }

    public void setTotalChUsSum(Long totalChUsSum) {
        this.totalChUsSum = totalChUsSum;
    }

    public Long getTotalAdjAmtSum() {
        return totalAdjAmtSum;
    }

    public void setTotalAdjAmtSum(Long totalAdjAmtSum) {
        this.totalAdjAmtSum = totalAdjAmtSum;
    }

    public Long getTotalPaySum() {
        return totalPaySum;
    }

    public void setTotalPaySum(Long totalPaySum) {
        this.totalPaySum = totalPaySum;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

}

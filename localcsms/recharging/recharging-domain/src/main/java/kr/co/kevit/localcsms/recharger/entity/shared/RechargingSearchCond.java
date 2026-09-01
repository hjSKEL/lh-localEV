/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.shared;

import java.util.List;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 4. 17.
 */
public class RechargingSearchCond extends PageCriteria {

    /**
     */
    private List<String> status;

    private List<String> cpIds;

    /**
     * 2019.01.16 추가
     */
    private String csId;

    private String cpId;

    private String cpName;

    /**
     * S : Start E : End
     */
    private String dateType = "S";

    private String fromDate;

    private String toDate;

    // 위탁사 번호
    private String companyId;

    private String customerId;

    private String cutCardNo;

    private String rechargingId;

    private String complexId;

    private String dong;

    private String ho;

    /**
     * 정렬 기준
     *  A:충전시작시간 ASC
     *  B:충전시작시간 DESC
     *  C:충전종료시간 ASC
     *  D:충전종료시간 DESC
     *  E:충전ID ASC
     *  F:충전ID DESC
     *  G:충전기ID ASC
     *  H:충전기ID DESC
     *  I:동 ASC
     *  J:동 DESC
     *  K:호 ASC
     *  L:호 DESC
     *  M:단지 ASC
     *  N:단지 DESC
     *  O:회원카드번호 ASC
     *  P:회원카드번호 DESC
     *  Z:기본정렬(단지 ASC, 동 ASC, 호 ASC, 충전시작시간 ASC)
     */
    private String dateOrder = "D";

    /** 검색조건에 걸리는 전체 결과의 청구금액(PAY_SUM) 합계 - 페이징 무관, 조회 시 서버가 채워줌(요청 파라미터 아님) */
    private Long paySumTotal;

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCsId() {
        return csId;
    }

    public void setCsId(String csId) {
        this.csId = csId;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCutCardNo() {
        return cutCardNo;
    }

    public void setCutCardNo(String cutCardNo) {
        this.cutCardNo = cutCardNo;
    }

    public String getRechargingId() {
        return rechargingId;
    }

    public void setRechargingId(String rechargingId) {
        this.rechargingId = rechargingId;
    }

    public String getComplexId() {
        return complexId;
    }

    public void setComplexId(String complexId) {
        this.complexId = complexId;
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

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public List<String> getCpIds() {
        return cpIds;
    }

    public void setCpIds(List<String> cpIds) {
        this.cpIds = cpIds;
    }

    /**
     * Get dateType
     * 
     * @return dateType
     */
    public String getDateType() {
        return dateType;
    }

    /**
     * Set dateType
     * 
     * @param dateType
     */
    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public Long getPaySumTotal() {
        return paySumTotal;
    }

    public void setPaySumTotal(Long paySumTotal) {
        this.paySumTotal = paySumTotal;
    }

}
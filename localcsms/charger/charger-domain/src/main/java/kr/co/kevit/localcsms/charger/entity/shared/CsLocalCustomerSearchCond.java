package kr.co.kevit.localcsms.charger.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * TB_CHLC001 조건 검색 (페이지 처리)
 */
public class CsLocalCustomerSearchCond extends PageCriteria {

    /** 충전소 ID (옵션) */
    private String cpId;

    /** 충전기 ID (옵션) */
    private String csId;

    /** 전송결과 상태 필터 (옵션) */
    private String status;

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

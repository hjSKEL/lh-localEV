package kr.co.kevit.localcsms.charger.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * TB_CHDM001 조회 조건
 */
public class DisplayMessageSearchCond extends PageCriteria {

    /** 충전소 ID (필수) */
    private String cpId;

    /** 충전기 ID (필수) */
    private String csId;

    /** 메시지 우선순위 필터 (선택) */
    private String priority;

    /** 메시지 상태 필터 (선택) */
    private String status;

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

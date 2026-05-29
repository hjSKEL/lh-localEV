package kr.co.kevit.localcsms.smartcharging.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * TB_CHPF001 조건 검색 (페이지 처리)
 */
public class ChargingProfileSearchCond extends PageCriteria {

    /** 충전소 ID */
    private String cpId;

    /** 충전기 ID */
    private String csId;

    /** EVSE ID (null=전체) */
    private Integer evseId;

    /** 프로파일 목적 공통코드 CHPP00 (null=전체) */
    private String purpose;

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public Integer getEvseId() { return evseId; }
    public void setEvseId(Integer evseId) { this.evseId = evseId; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
}

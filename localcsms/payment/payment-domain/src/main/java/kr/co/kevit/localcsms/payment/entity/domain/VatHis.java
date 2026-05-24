/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * VAT 검증 이력 — 충전기로부터 들어온 VatNumberValidationRequest 처리 결과 감사 로그.
 *
 * TB : TB_PAVAT02
 *
 * @author bckim
 * @since 2026. 5. 24.
 */
public class VatHis implements Serializable {

    private static final long serialVersionUID = 4187522001841052202L;

    /** Accepted */
    public static final String STATUS_ACCEPTED = "Accepted";
    /** Rejected */
    public static final String STATUS_REJECTED = "Rejected";

    /** PK auto increment */
    private Long seq;
    /** 요청 VAT 번호 */
    private String vatNo;
    /** 요청 발신 충전기 cpId */
    private String cpId;
    /** 요청 발신 충전기 csId */
    private String csId;
    /** EVSE id */
    private Integer evseId;
    /** 검증 결과: Accepted / Rejected */
    private String resultStatus;
    /** Rejected 사유코드 */
    private String reasonCd;
    /** 부가 정보 */
    private String additionalInfo;
    /** 검증 시각 */
    private Date verifiedDt;

    public Long getSeq() { return seq; }
    public void setSeq(Long seq) { this.seq = seq; }

    public String getVatNo() { return vatNo; }
    public void setVatNo(String vatNo) { this.vatNo = vatNo; }

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public Integer getEvseId() { return evseId; }
    public void setEvseId(Integer evseId) { this.evseId = evseId; }

    public String getResultStatus() { return resultStatus; }
    public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }

    public String getReasonCd() { return reasonCd; }
    public void setReasonCd(String reasonCd) { this.reasonCd = reasonCd; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }

    public Date getVerifiedDt() { return verifiedDt; }
    public void setVerifiedDt(Date verifiedDt) { this.verifiedDt = verifiedDt; }
}

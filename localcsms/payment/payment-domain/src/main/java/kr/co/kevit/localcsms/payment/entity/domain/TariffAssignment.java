/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.domain;

import java.io.Serializable;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * Tariff Assignment — Tariff 가 어디(EVSE) 또는 누구(idToken) 에 적용되는지 매핑.
 *
 * <p>DEFAULT_EVSE: CSMS → CS push (SetDefaultTariff) 필요. PENDING → ACTIVE.<br>
 * DRIVER_IDTOKEN: 등록 즉시 ACTIVE. Authorize 응답에서 lookup.</p>
 *
 * TB : TB_PATRF02
 *
 * @author bckim
 * @since 2026. 5. 25.
 */
public class TariffAssignment implements Serializable {

    private static final long serialVersionUID = 5301025001801251202L;

    public static final String TYPE_DEFAULT_EVSE   = "DEFAULT_EVSE";
    public static final String TYPE_DRIVER_IDTOKEN = "DRIVER_IDTOKEN";

    public static final String STATUS_PENDING  = "PENDING";
    public static final String STATUS_ACTIVE   = "ACTIVE";
    public static final String STATUS_REPLACED = "REPLACED";
    public static final String STATUS_CLEARED  = "CLEARED";
    public static final String STATUS_REJECTED = "REJECTED";

    private Long seq;
    private String tariffId;
    private String assignType;

    private String cpId;
    private String csId;
    private Integer evseId;

    private String idToken;

    private Date validFrom;
    private Date validTo;
    private String statusCd = STATUS_PENDING;
    private Date csAckDt;
    private String reasonCd;
    private Writer writer;

    public Long getSeq() { return seq; }
    public void setSeq(Long seq) { this.seq = seq; }

    public String getTariffId() { return tariffId; }
    public void setTariffId(String tariffId) { this.tariffId = tariffId; }

    public String getAssignType() { return assignType; }
    public void setAssignType(String assignType) { this.assignType = assignType; }

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public Integer getEvseId() { return evseId; }
    public void setEvseId(Integer evseId) { this.evseId = evseId; }

    public String getIdToken() { return idToken; }
    public void setIdToken(String idToken) { this.idToken = idToken; }

    public Date getValidFrom() { return validFrom; }
    public void setValidFrom(Date validFrom) { this.validFrom = validFrom; }

    public Date getValidTo() { return validTo; }
    public void setValidTo(Date validTo) { this.validTo = validTo; }

    public String getStatusCd() { return statusCd; }
    public void setStatusCd(String statusCd) { this.statusCd = statusCd; }

    public Date getCsAckDt() { return csAckDt; }
    public void setCsAckDt(Date csAckDt) { this.csAckDt = csAckDt; }

    public String getReasonCd() { return reasonCd; }
    public void setReasonCd(String reasonCd) { this.reasonCd = reasonCd; }

    public Writer getWriter() { return writer; }
    public void setWriter(Writer writer) { this.writer = writer; }
}

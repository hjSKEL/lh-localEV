/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.domain;

import java.io.Serializable;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * Tariff 마스터 (OCPP 2.1 use case I07/I08/I09).
 *
 * <p>불변(immutable): 같은 {@code tariffId} 의 내용 변경 금지. 수정 = 신규 버전 생성 후 기존 REPLACED 전이.</p>
 *
 * TB : TB_PATRF01
 *
 * @author bckim
 * @since 2026. 5. 25.
 */
public class Tariff implements Serializable {

    private static final long serialVersionUID = 5301025001801251201L;

    public static final String KIND_DEFAULT = "DEFAULT";
    public static final String KIND_DRIVER  = "DRIVER";
    public static final String KIND_ADHOC   = "ADHOC";

    public static final String STATUS_ACTIVE   = "ACTIVE";
    public static final String STATUS_REPLACED = "REPLACED";
    public static final String STATUS_CLEARED  = "CLEARED";
    public static final String STATUS_EXPIRED  = "EXPIRED";

    private String tariffId;
    private String tariffKind;
    private String currency;
    private Date validFrom;
    private Date validTo;
    private String tariffJson;
    private String statusCd = STATUS_ACTIVE;
    private String description;
    private Writer writer;

    public String getTariffId() { return tariffId; }
    public void setTariffId(String tariffId) { this.tariffId = tariffId; }

    public String getTariffKind() { return tariffKind; }
    public void setTariffKind(String tariffKind) { this.tariffKind = tariffKind; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Date getValidFrom() { return validFrom; }
    public void setValidFrom(Date validFrom) { this.validFrom = validFrom; }

    public Date getValidTo() { return validTo; }
    public void setValidTo(Date validTo) { this.validTo = validTo; }

    public String getTariffJson() { return tariffJson; }
    public void setTariffJson(String tariffJson) { this.tariffJson = tariffJson; }

    public String getStatusCd() { return statusCd; }
    public void setStatusCd(String statusCd) { this.statusCd = statusCd; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Writer getWriter() { return writer; }
    public void setWriter(Writer writer) { this.writer = writer; }
}

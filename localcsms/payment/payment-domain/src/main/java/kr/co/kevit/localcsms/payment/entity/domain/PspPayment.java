/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * PSP 결제 마스터.
 *
 * <p>OCPP 2.1 use case C18/C24 (Ad-hoc payment) 의 결제 인증 정보를 보관한다.
 * DirectPayment 타입의 {@code idToken=pspRef} 와 매칭되어 TransactionEvent 처리 시 lookup 된다.</p>
 *
 * TB : TB_PAPSP01
 *
 * @author bckim
 * @since 2026. 5. 25.
 */
public class PspPayment implements Serializable {

    private static final long serialVersionUID = 5301025001801251001L;

    /** 결제 인증 완료, 트랜잭션 미시작 */
    public static final String STATUS_AUTHORIZED = "AUTHORIZED";
    /** 트랜잭션 시작 (TransactionEvent.Started 도착) */
    public static final String STATUS_STARTED    = "STARTED";
    /** 정산 완료 */
    public static final String STATUS_SETTLED    = "SETTLED";
    /** 취소 */
    public static final String STATUS_CANCELED   = "CANCELED";

    /** PK — PSP 발급 결제 참조 ID */
    private String pspRef;
    /** 충전 트랜잭션 ID (Started 이후 연결) */
    private String rechargingId;
    /** RequestStartTransaction.remoteStartId */
    private Integer remoteStartId;

    private String cpId;
    private String csId;
    private Integer evseId;

    private String cardBin;
    private String cardLast4;
    private String cardholderNm;

    private BigDecimal authAmount;
    /** 금액 한도 (C18/C24 카드 결제) → response.transactionLimit.maxCost */
    private BigDecimal maxCost;
    /** 에너지 한도 Wh (C25 QR 결제) → response.transactionLimit.maxEnergy */
    private BigDecimal maxEnergy;
    private BigDecimal settledAmount;
    private String currency = "KRW";

    /** AUTHORIZED / STARTED / SETTLED / CANCELED */
    private String statusCd = STATUS_AUTHORIZED;
    private String reasonCd;

    private Date authDate;
    private Date startedDate;
    private Date settledDate;
    private Date canceledDate;

    private String receiptUrl;
    private String receiptId;

    /** TOSS / NICE / KICC / INICIS / KCP / OTHER */
    private String pspProvider;
    private String extraInfo;

    private Writer writer;

    public String getPspRef() { return pspRef; }
    public void setPspRef(String pspRef) { this.pspRef = pspRef; }

    public String getRechargingId() { return rechargingId; }
    public void setRechargingId(String rechargingId) { this.rechargingId = rechargingId; }

    public Integer getRemoteStartId() { return remoteStartId; }
    public void setRemoteStartId(Integer remoteStartId) { this.remoteStartId = remoteStartId; }

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public Integer getEvseId() { return evseId; }
    public void setEvseId(Integer evseId) { this.evseId = evseId; }

    public String getCardBin() { return cardBin; }
    public void setCardBin(String cardBin) { this.cardBin = cardBin; }

    public String getCardLast4() { return cardLast4; }
    public void setCardLast4(String cardLast4) { this.cardLast4 = cardLast4; }

    public String getCardholderNm() { return cardholderNm; }
    public void setCardholderNm(String cardholderNm) { this.cardholderNm = cardholderNm; }

    public BigDecimal getAuthAmount() { return authAmount; }
    public void setAuthAmount(BigDecimal authAmount) { this.authAmount = authAmount; }

    public BigDecimal getMaxCost() { return maxCost; }
    public void setMaxCost(BigDecimal maxCost) { this.maxCost = maxCost; }

    public BigDecimal getMaxEnergy() { return maxEnergy; }
    public void setMaxEnergy(BigDecimal maxEnergy) { this.maxEnergy = maxEnergy; }

    public BigDecimal getSettledAmount() { return settledAmount; }
    public void setSettledAmount(BigDecimal settledAmount) { this.settledAmount = settledAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getStatusCd() { return statusCd; }
    public void setStatusCd(String statusCd) { this.statusCd = statusCd; }

    public String getReasonCd() { return reasonCd; }
    public void setReasonCd(String reasonCd) { this.reasonCd = reasonCd; }

    public Date getAuthDate() { return authDate; }
    public void setAuthDate(Date authDate) { this.authDate = authDate; }

    public Date getStartedDate() { return startedDate; }
    public void setStartedDate(Date startedDate) { this.startedDate = startedDate; }

    public Date getSettledDate() { return settledDate; }
    public void setSettledDate(Date settledDate) { this.settledDate = settledDate; }

    public Date getCanceledDate() { return canceledDate; }
    public void setCanceledDate(Date canceledDate) { this.canceledDate = canceledDate; }

    public String getReceiptUrl() { return receiptUrl; }
    public void setReceiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; }

    public String getReceiptId() { return receiptId; }
    public void setReceiptId(String receiptId) { this.receiptId = receiptId; }

    public String getPspProvider() { return pspProvider; }
    public void setPspProvider(String pspProvider) { this.pspProvider = pspProvider; }

    public String getExtraInfo() { return extraInfo; }
    public void setExtraInfo(String extraInfo) { this.extraInfo = extraInfo; }

    public Writer getWriter() { return writer; }
    public void setWriter(Writer writer) { this.writer = writer; }
}

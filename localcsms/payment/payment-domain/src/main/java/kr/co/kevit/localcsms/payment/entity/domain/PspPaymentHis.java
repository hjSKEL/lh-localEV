/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * PSP 결제 상태변경 이력.
 *
 * TB : TB_PAPSP02
 *
 * @author bckim
 * @since 2026. 5. 25.
 */
public class PspPaymentHis implements Serializable {

    private static final long serialVersionUID = 5301025001801251002L;

    private Long seq;
    private String pspRef;
    private String preStatusCd;
    private String postStatusCd;
    private BigDecimal amount;
    private String reasonCd;
    private String remark;
    private String operId;
    private Date occurredDate;

    public Long getSeq() { return seq; }
    public void setSeq(Long seq) { this.seq = seq; }

    public String getPspRef() { return pspRef; }
    public void setPspRef(String pspRef) { this.pspRef = pspRef; }

    public String getPreStatusCd() { return preStatusCd; }
    public void setPreStatusCd(String preStatusCd) { this.preStatusCd = preStatusCd; }

    public String getPostStatusCd() { return postStatusCd; }
    public void setPostStatusCd(String postStatusCd) { this.postStatusCd = postStatusCd; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getReasonCd() { return reasonCd; }
    public void setReasonCd(String reasonCd) { this.reasonCd = reasonCd; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getOperId() { return operId; }
    public void setOperId(String operId) { this.operId = operId; }

    public Date getOccurredDate() { return occurredDate; }
    public void setOccurredDate(Date occurredDate) { this.occurredDate = occurredDate; }
}

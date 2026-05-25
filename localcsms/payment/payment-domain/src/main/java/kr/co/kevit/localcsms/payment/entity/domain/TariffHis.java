/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Tariff 변경 이력 (append-only audit).
 *
 * TB : TB_PATRF03
 *
 * @author bckim
 * @since 2026. 5. 25.
 */
public class TariffHis implements Serializable {

    private static final long serialVersionUID = 5301025001801251203L;

    public static final String ACTION_REGISTER = "REGISTER";
    public static final String ACTION_MODIFY   = "MODIFY";
    public static final String ACTION_PUSH     = "PUSH";
    public static final String ACTION_ACK      = "ACK";
    public static final String ACTION_REPLACE  = "REPLACE";
    public static final String ACTION_CLEAR    = "CLEAR";

    private Long seq;
    private String tariffId;
    private Long assignmentSeq;
    private String actionCd;
    private String preStatusCd;
    private String postStatusCd;
    private String remark;
    private String operId;
    private Date occurredDate;

    public Long getSeq() { return seq; }
    public void setSeq(Long seq) { this.seq = seq; }

    public String getTariffId() { return tariffId; }
    public void setTariffId(String tariffId) { this.tariffId = tariffId; }

    public Long getAssignmentSeq() { return assignmentSeq; }
    public void setAssignmentSeq(Long assignmentSeq) { this.assignmentSeq = assignmentSeq; }

    public String getActionCd() { return actionCd; }
    public void setActionCd(String actionCd) { this.actionCd = actionCd; }

    public String getPreStatusCd() { return preStatusCd; }
    public void setPreStatusCd(String preStatusCd) { this.preStatusCd = preStatusCd; }

    public String getPostStatusCd() { return postStatusCd; }
    public void setPostStatusCd(String postStatusCd) { this.postStatusCd = postStatusCd; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getOperId() { return operId; }
    public void setOperId(String operId) { this.operId = operId; }

    public Date getOccurredDate() { return occurredDate; }
    public void setOccurredDate(Date occurredDate) { this.occurredDate = occurredDate; }
}

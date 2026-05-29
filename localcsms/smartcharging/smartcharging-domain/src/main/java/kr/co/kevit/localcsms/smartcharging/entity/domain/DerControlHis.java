/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.smartcharging.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * DER Control 변경 이력 (append-only).
 *
 * TB : TB_DRCTL02
 *
 * @author bckim
 * @since 2026. 5. 26.
 */
public class DerControlHis implements Serializable {

    private static final long serialVersionUID = 5302026001801260002L;

    public static final String ACTION_REGISTER  = "REGISTER";
    public static final String ACTION_PUSH      = "PUSH";
    public static final String ACTION_ACK       = "ACK";
    public static final String ACTION_REPLACE   = "REPLACE";
    public static final String ACTION_CLEAR     = "CLEAR";
    public static final String ACTION_REPORT_IN = "REPORT_IN";

    private Long seq;
    private String controlId;
    private String originCd;
    private String actionCd;
    private String preStatusCd;
    private String postStatusCd;
    private String remark;
    private String operId;
    private Date occurredDate;

    public Long getSeq() { return seq; }
    public void setSeq(Long seq) { this.seq = seq; }

    public String getControlId() { return controlId; }
    public void setControlId(String controlId) { this.controlId = controlId; }

    public String getOriginCd() { return originCd; }
    public void setOriginCd(String originCd) { this.originCd = originCd; }

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

package kr.co.kevit.localcsms.charger.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * TB_CHDM001
 * OCPP 2.x SetDisplayMessage / GetDisplayMessages - 충전기 디스플레이 메시지
 */
public class DisplayMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /** PK: 메시지 ID  MSG_ID INT */
    private int messageId;

    /** 충전소 ID  CP_ID CHAR(6) */
    private String cpId;

    /** 충전기 ID  CS_ID CHAR(2) */
    private String csId;

    /** 메시지 우선순위 (AlwaysFront / InFront / NormalCycle)  PRIORITY VARCHAR(20) */
    private String priority;

    /** 메시지 상태 공통코드 DMST00  STAT CHAR(6) */
    private String status;

    /** 표시 시작일시  ST_DT DATETIME */
    private Date startDate;

    /** 표시 종료일시  EN_DT DATETIME */
    private Date endDate;

    /** 연관 충전 트랜잭션 ID  RC_ID VARCHAR(36) */
    private String rcId;

    /** 표시 Component.name  DISP_NM VARCHAR(50) */
    private String displayName;

    /** 표시 Component.instance  DISP_INST VARCHAR(50) */
    private String displayInstance;

    /** 표시 EVSE ID  DISP_EVSE_ID INT */
    private Integer displayEvseId;

    /** 표시 Connector ID  DISP_CONN_ID INT */
    private Integer displayConnId;

    /** 충전기 응답 상태  CS_STATUS VARCHAR(20) */
    private String csStatus;

    /** 등록/수정 정보 */
    private Writer writer;

    /** 메시지 내용 목록 (TB_CHDM002) */
    private List<DisplayMessageContent> contents;

    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getRcId() { return rcId; }
    public void setRcId(String rcId) { this.rcId = rcId; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getDisplayInstance() { return displayInstance; }
    public void setDisplayInstance(String displayInstance) { this.displayInstance = displayInstance; }

    public Integer getDisplayEvseId() { return displayEvseId; }
    public void setDisplayEvseId(Integer displayEvseId) { this.displayEvseId = displayEvseId; }

    public Integer getDisplayConnId() { return displayConnId; }
    public void setDisplayConnId(Integer displayConnId) { this.displayConnId = displayConnId; }

    public String getCsStatus() { return csStatus; }
    public void setCsStatus(String csStatus) { this.csStatus = csStatus; }

    public Writer getWriter() { return writer; }
    public void setWriter(Writer writer) { this.writer = writer; }

    public List<DisplayMessageContent> getContents() { return contents; }
    public void setContents(List<DisplayMessageContent> contents) { this.contents = contents; }
}

package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * TB_CHFW001 - 충전기 펌웨어 업데이트 정보
 */
public class CsFirmware implements Serializable {

    private static final long serialVersionUID = 1L;

    /** PK: 충전소 ID  CP_ID CHAR(6) */
    private String cpId;
    /** PK: 충전기 ID  CS_ID CHAR(2) */
    private String csId;
    /** 펌웨어 다운로드 URL  URL VARCHAR(255) */
    private String url;
    /** 상태 (CSFW01~CSFW07)  STATUS CHAR(6) */
    private String status;
    /** 요청자 ID  REQ_ID CHAR(9) */
    private String requestEmployeeId;
    /** 요청일시  REQ_DT DATETIME */
    private Date requestDate;
    /** 수정일시  UPT_DT DATETIME */
    private Date updateDate;

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRequestEmployeeId() { return requestEmployeeId; }
    public void setRequestEmployeeId(String requestEmployeeId) { this.requestEmployeeId = requestEmployeeId; }

    public Date getRequestDate() { return requestDate; }
    public void setRequestDate(Date requestDate) { this.requestDate = requestDate; }

    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}

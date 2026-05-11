package kr.co.kevit.localcsms.charger.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * TB_CHLC001 - 충전기 로컬 고객 (Local Authorization List Header)
 *
 * 충전기별 로컬 인증 목록의 버전 및 전송 결과를 관리한다.
 */
public class CsLocalCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 충전소 ID  CP_ID VARCHAR(6) */
    private String cpId;

    /** 충전기 ID  CS_ID VARCHAR(2) */
    private String csId;

    /** 로컬목록 버전번호  VER_NO INT */
    private int versionNo;

    /** 마지막 업데이트타입 (Full/Differential)  LAST_UPD_TYPE VARCHAR(20) */
    private String lastUpdateType;

    /** 마지막 전송일시  LAST_SEND_DT DATETIME */
    private Date lastSendDate;

    /** 전송결과 상태 (Accepted/Failed/NotSupported/VersionMismatch)  STATUS VARCHAR(30) */
    private String status;

    /** 등록/수정 정보 */
    private Writer writer;

    /** 토큰 목록 (조회 시 조합, DB 컬럼 아님) */
    private List<CsLocalCustomerToken> tokens;

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public int getVersionNo() { return versionNo; }
    public void setVersionNo(int versionNo) { this.versionNo = versionNo; }

    public String getLastUpdateType() { return lastUpdateType; }
    public void setLastUpdateType(String lastUpdateType) { this.lastUpdateType = lastUpdateType; }

    public Date getLastSendDate() { return lastSendDate; }
    public void setLastSendDate(Date lastSendDate) { this.lastSendDate = lastSendDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Writer getWriter() { return writer; }
    public void setWriter(Writer writer) { this.writer = writer; }

    public List<CsLocalCustomerToken> getTokens() { return tokens; }
    public void setTokens(List<CsLocalCustomerToken> tokens) { this.tokens = tokens; }
}

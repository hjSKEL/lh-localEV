package kr.co.kevit.localcsms.charger.entity.shared;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * TB_CHFW001 - 충전기 펌웨어 검색 조건
 */
public class CsFirmwareSearchCond extends PageCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cpId;
    private String csId;
    private String status;

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

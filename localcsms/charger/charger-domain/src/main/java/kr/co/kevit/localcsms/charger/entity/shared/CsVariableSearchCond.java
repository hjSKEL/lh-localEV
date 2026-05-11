package kr.co.kevit.localcsms.charger.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * TB_CHCF002 조회 조건
 */
public class CsVariableSearchCond extends PageCriteria {

    /** 충전소 ID (필수) */
    private String cpId;
    /** 충전기 ID (필수) */
    private String csId;
    /** Component 이름 필터 (선택) */
    private String compNm;
    /** Variable 이름 필터 (선택) */
    private String varNm;
    /** Attribute 타입 필터 (선택, 기본 Actual) */
    private String attrTp;

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getCompNm() { return compNm; }
    public void setCompNm(String compNm) { this.compNm = compNm; }

    public String getVarNm() { return varNm; }
    public void setVarNm(String varNm) { this.varNm = varNm; }

    public String getAttrTp() { return attrTp; }
    public void setAttrTp(String attrTp) { this.attrTp = attrTp; }
}

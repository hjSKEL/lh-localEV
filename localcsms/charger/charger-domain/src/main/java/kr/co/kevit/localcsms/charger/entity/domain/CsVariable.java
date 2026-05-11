package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * TB_CHCF002
 * OCPP 2.x GetVariables / GetBaseReport 결과 - 충전기 설정 변수
 */
public class CsVariable implements Serializable {

    private static final long serialVersionUID = 1L;

    /** PK: 충전소 ID  CP_ID VARCHAR(6) */
    private String cpId;
    /** PK: 충전기 ID  CS_ID VARCHAR(2) */
    private String csId;
    /** PK: Component.name  COMP_NM VARCHAR(50) */
    private String compNm;
    /** PK: Component.instance  COMP_INST VARCHAR(50) DEFAULT '' */
    private String compInst = "";
    /** PK: Component.evse.id  EVSE_ID INT DEFAULT 0 */
    private int evseId = 0;
    /** PK: Component.evse.connectorId  CONN_ID INT DEFAULT 0 */
    private int connId = 0;
    /** PK: Variable.name  VAR_NM VARCHAR(50) */
    private String varNm;
    /** PK: Variable.instance  VAR_INST VARCHAR(50) DEFAULT '' */
    private String varInst = "";
    /** PK: Attribute 타입  ATTR_TP VARCHAR(20) DEFAULT 'Actual' */
    private String attrTp = "Actual";

    /** Attribute 값  ATTR_VAL VARCHAR(2000) */
    private String attrVal;
    /** GetVariableStatus  ATTR_STAT VARCHAR(30) */
    private String attrStat;
    /** 최종 갱신일시  UPD_DT DATETIME */
    private Date updDt;

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getCompNm() { return compNm; }
    public void setCompNm(String compNm) { this.compNm = compNm; }

    public String getCompInst() { return compInst; }
    public void setCompInst(String compInst) { this.compInst = compInst != null ? compInst : ""; }

    public int getEvseId() { return evseId; }
    public void setEvseId(int evseId) { this.evseId = evseId; }

    public int getConnId() { return connId; }
    public void setConnId(int connId) { this.connId = connId; }

    public String getVarNm() { return varNm; }
    public void setVarNm(String varNm) { this.varNm = varNm; }

    public String getVarInst() { return varInst; }
    public void setVarInst(String varInst) { this.varInst = varInst != null ? varInst : ""; }

    public String getAttrTp() { return attrTp; }
    public void setAttrTp(String attrTp) { this.attrTp = attrTp != null ? attrTp : "Actual"; }

    public String getAttrVal() { return attrVal; }
    public void setAttrVal(String attrVal) { this.attrVal = attrVal; }

    public String getAttrStat() { return attrStat; }
    public void setAttrStat(String attrStat) { this.attrStat = attrStat; }

    public Date getUpdDt() { return updDt; }
    public void setUpdDt(Date updDt) { this.updDt = updDt; }
}

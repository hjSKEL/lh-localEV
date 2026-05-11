/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 공통코드
 * 
 * TB_SYCO001
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */
public class Code implements Serializable {

    /**  */
    private static final long serialVersionUID = 318850302740641471L;

    /**
     * 상위 코드 HI_CD VARCHAR(8)
     * 
     */
    private String highCode;

    /**
     * 코드 CD VARCHAR(8) NOT NULL
     */
    private String code;

    /**
     * CD_NM VARCHAR(50),
     */
    private String codeName;

    /**
     * CD_NM_EN VARCHAR(50),
     */
    private String codeNameEn;

    /**
     * ORD_PRI INT(11),
     */
    private int ordPriority;

    /**
     * 코드 설명 CD_DES VARCHAR2(500 BYTE)
     */
    private String codeDescription;

    private List<Code> codes;

    public String getHighCode() {
        return highCode;
    }

    public void setHighCode(String highCode) {
        this.highCode = highCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeNameEn() {
        return codeNameEn;
    }

    public void setCodeNameEn(String codeNameEn) {
        this.codeNameEn = codeNameEn;
    }

    public int getOrdPriority() {
        return ordPriority;
    }

    public void setOrdPriority(int ordPriority) {
        this.ordPriority = ordPriority;
    }

    public String getCodeDescription() {
        return codeDescription;
    }

    public void setCodeDescription(String codeDescription) {
        this.codeDescription = codeDescription;
    }

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }
}

/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 공통 변수
 * 
 * TB_SYCO002
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */
public class CodeVal implements Serializable {

    /**  */
    private static final long serialVersionUID = 8575816148807152747L;

    /**
     * 상위 코드 HI_CD CHAR(6)
     * 
     */
    private String highCode;

    /**
     * 코드 CD CHAR(6)
     */
    private String code;

    /**
     * 코드명 CD_NM VARCHAR(100 BYTE),
     */
    private String codeName;

    /**
     * 코드값 CD_VAL VARCHAR(500 BYTE),
     */
    private String codeValue;

    /**
     * 우선순위 ORD_PRI INT(11),
     */
    private int ordPriority;

    /**
     * 코드 설명 CD_DES VARCHAR(500 BYTE)
     */
    private String codeDescription;

    private List<CodeVal> codeVals;

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

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
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

    public List<CodeVal> getCodeVals() {
        return codeVals;
    }

    public void setCodeVals(List<CodeVal> codeVals) {
        this.codeVals = codeVals;
    }

}

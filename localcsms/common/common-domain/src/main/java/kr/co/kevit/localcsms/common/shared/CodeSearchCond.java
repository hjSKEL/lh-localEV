/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 8. 31.
 */
public class CodeSearchCond extends PageCriteria implements Serializable {

    /**  */
    private static final long serialVersionUID = -8961825910108528130L;
    private String codeName;
    
    private String highCode;
    
    private List<String> highCodes;
    
    private String subCode;

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getHighCode() {
        return highCode;
    }

    public void setHighCode(String highCode) {
        this.highCode = highCode;
    }

    public List<String> getHighCodes() {
        return highCodes;
    }

    public void setHighCodes(List<String> highCodes) {
        this.highCodes = highCodes;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

}

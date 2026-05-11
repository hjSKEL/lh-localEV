/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.process;

import kr.co.kevit.localcsms.common.domain.CodeVal;

import java.util.List;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 2. 21.
 */
public interface CodeValService {

    void registerCodeVal(CodeVal codeVal);

    void modifyCodeVal(CodeVal codeVal);

    /**
     * for IF
     * 
     * @param code
     * @return
     */
    CodeVal retrieveCodeValByCode(String code);

    List<CodeVal> retrieveCodeValCodeValByParentCode(String parentCode);

    CodeVal retrieveCodeValWithSubCodeByParentCode(String parentCode);

    List<CodeVal> retrieveCodeValCodeValByParentCodes(List<String> parentCodes);

}

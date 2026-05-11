/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity;

import kr.co.kevit.localcsms.common.domain.CodeVal;

import java.util.List;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */
public interface CodeValProvider {

    void registerCodeVal(CodeVal codeVal);

    void modifyCodeVal(CodeVal codeVal);

    CodeVal retrieveCodeValByCode(String code);

    List<CodeVal> retrieveCodeValCodeValByParentCode(String highCode);

    CodeVal retrieveCodeValWithSubCodeByParentCode(String highCode);

}

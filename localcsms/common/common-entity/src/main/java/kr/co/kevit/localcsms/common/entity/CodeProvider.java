/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity;

import kr.co.kevit.localcsms.common.domain.Code;
import kr.co.kevit.localcsms.common.shared.CodeSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

import java.util.List;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */

public interface CodeProvider {

    void registerCode(Code code);

    void modifyCode(Code code);

    Code retrieveCodeByCode(String code);

    List<Code> retrieveCodeByParentCode(String highCode);

    Code retrieveCodeWithSubCodeByParentCode(String highCode);

    Page<Code> retrieveCodeByCodeSearchCond(CodeSearchCond searchCond);

    List<Code> retrieveSubCodeByParentCodes(List<String> parentCodes);
}

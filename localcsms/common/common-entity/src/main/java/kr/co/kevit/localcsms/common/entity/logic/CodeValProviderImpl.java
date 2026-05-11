/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.logic;

import kr.co.kevit.localcsms.common.domain.CodeVal;
import kr.co.kevit.localcsms.common.entity.CodeValProvider;
import kr.co.kevit.localcsms.common.entity.dao.CodeValMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */
@Component
public class CodeValProviderImpl implements CodeValProvider {

    @Autowired
    private CodeValMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCodeVal(CodeVal codeVal) {
        //
        mapper.insertCodeVal(codeVal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyCodeVal(CodeVal codeVal) {
        //
        mapper.updateCodeVal(codeVal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeVal retrieveCodeValByCode(String code) {
        //
        return mapper.selectCodeValByCode(code);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CodeVal> retrieveCodeValCodeValByParentCode(String highCode) {
        //
        return mapper.selectCodeValByParentCode(highCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeVal retrieveCodeValWithSubCodeByParentCode(String highCode) {
        //
        CodeVal codeVal = mapper.selectCodeValByCode(highCode);
        if (codeVal == null)
            return null;

        codeVal.setCodeVals(mapper.selectCodeValByParentCode(highCode));
        return codeVal;
    }

}

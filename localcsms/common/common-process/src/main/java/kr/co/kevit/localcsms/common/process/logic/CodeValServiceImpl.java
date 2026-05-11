/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.process.logic;

import kr.co.kevit.localcsms.common.domain.CodeVal;
import kr.co.kevit.localcsms.common.entity.CodeValProvider;
import kr.co.kevit.localcsms.common.process.CodeValService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 2. 21.
 */
@Service
@Transactional
public class CodeValServiceImpl implements CodeValService {

    @Autowired
    private CodeValProvider provider;

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void registerCodeVal(CodeVal codeVal) {
        //
        provider.registerCodeVal(codeVal);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void modifyCodeVal(CodeVal codeVal) {
        //
        provider.modifyCodeVal(codeVal);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public CodeVal retrieveCodeValByCode(String code) {
        //
        return provider.retrieveCodeValByCode(code);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<CodeVal> retrieveCodeValCodeValByParentCode(String parentCode) {
        //
        return provider.retrieveCodeValCodeValByParentCode(parentCode);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public CodeVal retrieveCodeValWithSubCodeByParentCode(String parentCode) {
        //
        return provider.retrieveCodeValWithSubCodeByParentCode(parentCode);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<CodeVal> retrieveCodeValCodeValByParentCodes(List<String> parentCodes) {
        //
        List<CodeVal> resultSet = new ArrayList<CodeVal>();
        for (String parentCode : parentCodes) {
            CodeVal e1 = provider.retrieveCodeValWithSubCodeByParentCode(parentCode);
            resultSet.add(e1);
        }
        return resultSet;
    }

}

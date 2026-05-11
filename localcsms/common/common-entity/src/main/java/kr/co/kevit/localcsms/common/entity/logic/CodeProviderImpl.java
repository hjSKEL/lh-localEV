/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.logic;

import kr.co.kevit.localcsms.common.domain.Code;
import kr.co.kevit.localcsms.common.shared.CodeSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.entity.CodeProvider;
import kr.co.kevit.localcsms.common.entity.dao.CodeMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */
@Component
public class CodeProviderImpl implements CodeProvider {

    @Autowired
    private CodeMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCode(Code code) {
        //
        mapper.insertCode(code);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyCode(Code code) {
        //
        mapper.updateCode(code);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Code retrieveCodeByCode(String code) {
        //
        return mapper.selectCodeByCode(code);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Code> retrieveCodeByParentCode(String highCode) {
        //
        return mapper.selectCodeByParentCode(highCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Code retrieveCodeWithSubCodeByParentCode(String highCode) {
        //
        Code code = mapper.selectCodeByCode(highCode);
        if (code == null)
            return null;
        code.setCodes(mapper.selectCodeByParentCode(highCode));
        return code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Code> retrieveCodeByCodeSearchCond(CodeSearchCond searchCond) {
        //
        int totalItemCount = mapper.countCodeByCodeSearchCond(searchCond);
        Page<Code> resultSet = new Page<>();
        searchCond.setTotalItemCount(totalItemCount);
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0) {
            return resultSet;
        }
        resultSet.setResult(mapper.selectCodeByCodeSearchCond(searchCond));
        return resultSet;
    }

    @Override
    public List<Code> retrieveSubCodeByParentCodes(List<String> highCodes) {
        //
        return mapper.selectSubCodeByParentCodes(highCodes);
    }
}

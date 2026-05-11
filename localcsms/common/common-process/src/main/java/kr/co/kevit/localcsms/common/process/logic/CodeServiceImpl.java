/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.process.logic;

import kr.co.kevit.localcsms.common.domain.Code;
import kr.co.kevit.localcsms.common.entity.CodeProvider;
import kr.co.kevit.localcsms.common.shared.CodeSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.process.CodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 10. 31.
 */
@Service
@Transactional
public class CodeServiceImpl implements CodeService {
    
    @Autowired
    private CodeProvider provider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCode(Code code) {
        // 
        provider.registerCode(code);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyCode(Code code) {
        // 
        provider.modifyCode(code);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Code retrieveCodeByCode(String code) {
        // 
        return provider.retrieveCodeByCode(code);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Code> retrieveCodeByParentCode(String parentCode) {
        // 
        return provider.retrieveCodeByParentCode(parentCode);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Code retrieveCodeWithSubCodeByParentCode(String parentCode) {
        // 
        return provider.retrieveCodeWithSubCodeByParentCode(parentCode);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<Code> retrieveCodeByCodeSearchCond(CodeSearchCond searchCond) {
        // 
        return provider.retrieveCodeByCodeSearchCond(searchCond);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Code> retrieveCodeByParentCodes(List<String> parentCodes) {
        // 
        List<Code> subCodes = provider.retrieveSubCodeByParentCodes(parentCodes);

        List<Code> resultSet = new ArrayList<Code>();
        Map<String, Code> resultMap = new HashMap<String, Code>();
        for(String parentCode : parentCodes) {
            Code e = new Code();
            e.setHighCode(StringConstants.ZERO6);
            e.setCode(parentCode);
            e.setCodes(new ArrayList<Code>());
            resultSet.add(e);
            resultMap.put(parentCode, e);
        }
        for(Code subCode : subCodes) {
            Code highCode = resultMap.get(subCode.getHighCode());
            if(highCode == null)
                continue;
            highCode.getCodes().add(subCode);
        }
        return resultSet;
    }


}

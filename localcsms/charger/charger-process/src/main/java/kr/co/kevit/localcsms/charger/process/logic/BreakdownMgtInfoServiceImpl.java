/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process.logic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.charger.entity.BreakdownInfoProvider;
import kr.co.kevit.localcsms.charger.entity.BreakdownMgtInfoProvider;
import kr.co.kevit.localcsms.charger.entity.BreakdownRepairInfoProvider;
import kr.co.kevit.localcsms.charger.entity.domain.BreakdownInfo;
import kr.co.kevit.localcsms.charger.entity.domain.BreakdownRepairInfo;
import kr.co.kevit.localcsms.charger.entity.shared.BreakdownMgtInfoDto;
import kr.co.kevit.localcsms.charger.entity.shared.BreakdownSearchCond;
import kr.co.kevit.localcsms.charger.process.BreakdownMgtInfoService;
import kr.co.kevit.localcsms.common.entity.CryptoKeyProvider;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.security.AES256Util;
import kr.co.kevit.localcsms.organization.entity.domain.Employee;
import kr.co.kevit.localcsms.organization.external.EmployeeExtProcess;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
@Service
@Transactional
public class BreakdownMgtInfoServiceImpl implements BreakdownMgtInfoService{
    
    @Autowired
    private BreakdownMgtInfoProvider provider;
    
    @Autowired
    private BreakdownInfoProvider bdInfoProvider;
    
    @Autowired
    private CryptoKeyProvider cryptoKeyProvider;
    
    @Autowired
    private BreakdownRepairInfoProvider repairProvider;
    
    @Autowired
    private EmployeeExtProcess emplProcess;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<BreakdownMgtInfoDto> retrieveBreakdownMgtInfoBySearchCond(BreakdownSearchCond searchCond) {
        // 
        return provider.retrieveBreakdownMgtInfoBySearchCond(searchCond);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public BreakdownMgtInfoDto retrieveBreakdownMgtInfoById(String id) {
        // 
        BreakdownMgtInfoDto result = provider.retrieveBreakdownMgtInfoById(id);
        if(result == null) {
            return null;
        }
        result.setReceptInfo(bdInfoProvider.retrieveBreakdownInfo(result.getId()));
        result.setRepairInfo(repairProvider.retrieveBreakdownRepairInfo(result.getId()));
        
        Map<String,String> emplMap = new HashMap<>();
        String name = emplMap.get(result.getWriter().getRegUserId());
        Employee empl;
        if(name == null) {                
            empl = emplProcess.retrieveEmployeeById(result.getWriter().getRegUserId());
            result.getWriter().setRegUserName(empl.getEmplName());
            emplMap.put(result.getWriter().getRegUserId(), empl.getEmplName());
        }
        name = emplMap.get(result.getWriter().getUpdUserId());
        if(name == null) {                
            empl = emplProcess.retrieveEmployeeById(result.getWriter().getUpdUserId());
            result.getWriter().setUpdUserName(empl.getEmplName());
            emplMap.put(result.getWriter().getUpdUserId(), empl.getEmplName());
        }
        BreakdownInfo bdInfo = result.getReceptInfo();
        name = emplMap.get(bdInfo.getWriter().getRegUserId());
        if(name == null) {                
            empl = emplProcess.retrieveEmployeeById(bdInfo.getWriter().getRegUserId());
            bdInfo.getWriter().setRegUserName(empl.getEmplName());
            emplMap.put(bdInfo.getWriter().getRegUserId(), empl.getEmplName());
        }
        name = emplMap.get(bdInfo.getWriter().getUpdUserId());
        if(name == null) {                
            empl = emplProcess.retrieveEmployeeById(bdInfo.getWriter().getUpdUserId());
            bdInfo.getWriter().setUpdUserName(empl.getEmplName());
            emplMap.put(bdInfo.getWriter().getUpdUserId(), empl.getEmplName());
        }
        
        BreakdownRepairInfo repairInfo = result.getRepairInfo();
        if(repairInfo != null) {
            name = emplMap.get(repairInfo.getWriter().getRegUserId());
            if(name == null) {                
                empl = emplProcess.retrieveEmployeeById(repairInfo.getWriter().getRegUserId());
                repairInfo.getWriter().setRegUserName(empl.getEmplName());
                emplMap.put(repairInfo.getWriter().getRegUserId(), empl.getEmplName());
            }
            name = emplMap.get(repairInfo.getWriter().getUpdUserId());
            if(name == null) {                
                empl = emplProcess.retrieveEmployeeById(repairInfo.getWriter().getUpdUserId());
                repairInfo.getWriter().setUpdUserName(empl.getEmplName());
                emplMap.put(repairInfo.getWriter().getUpdUserId(), empl.getEmplName());
            }
        }
        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(BreakdownInfo.class);
        result.getReceptInfo().setReporterPhoneNum(AES256Util.decryption(keyData, result.getReceptInfo().getReporterPhoneNum()));
        return result;
    }
    

}

/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.charger.entity.BreakdownInfoProvider;
import kr.co.kevit.localcsms.charger.entity.domain.BreakdownInfo;
import kr.co.kevit.localcsms.charger.process.BreakdownInfoService;
import kr.co.kevit.localcsms.common.entity.CryptoKeyProvider;
import kr.co.kevit.localcsms.common.util.security.AES256Util;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
@Service
@Transactional
public class BreakdownInfoServiceImpl implements BreakdownInfoService{
    
    @Autowired
    private BreakdownInfoProvider provider;

    @Autowired
    private CryptoKeyProvider cryptoKeyProvider;
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBreakdownInfo(BreakdownInfo info) {
        // 
    	 byte[] keyData = cryptoKeyProvider.retriveCryptoKey(BreakdownInfo.class);
         info.setReporterPhoneNum(AES256Util.encryption(keyData, info.getReporterPhoneNum()));
         provider.registerBreakdownInfo(info);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyBreakdownInfo(BreakdownInfo info) {
        // 
        provider.modifyBreakdownInfo(info);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public BreakdownInfo retrieveBreakdownInfo(String id) {
        // 
        return provider.retrieveBreakdownInfo(id);
    }


}

/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.logic;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.charger.entity.BreakdownInfoProvider;
import kr.co.kevit.localcsms.charger.entity.dao.BreakdownInfoMapper;
import kr.co.kevit.localcsms.charger.entity.dao.BreakdownMgtInfoMapper;
import kr.co.kevit.localcsms.charger.entity.domain.BreakdownInfo;
import kr.co.kevit.localcsms.charger.entity.domain.BreakdownMgtInfo;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
@Component
public class BreakdownInfoProviderImpl implements BreakdownInfoProvider{
    
    @Autowired
    private BreakdownInfoMapper mapper;
    
    @Autowired
    private BreakdownMgtInfoMapper mgtMapper;
    
    private String generateBreakdownId() {
        // 
        BigInteger seq = mapper.getBreakdownSequence();
        StringBuilder buffer = new StringBuilder(13);
        buffer.append("BD");
        int length = 11 - String.valueOf(seq.longValue()).length();
        for(int i = 0; i < length ; ++i) {
            buffer.append(StringConstants.ZERO);
        }
        buffer.append(seq);
        return buffer.toString();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBreakdownInfo(BreakdownInfo info) {
        //
        info.setId(generateBreakdownId());
        mapper.insertBreakdownInfo(info);
        BreakdownMgtInfo mgtInfo = info.getBreakdownMgtInfo();
        mgtInfo.setId(info.getId());
        mgtInfo.setWriter(info.getWriter());
        String receiptDt = DateUtils.dateToString(info.getWriter().getRegistrationDate(), DateUtils.YYYYMMDDHHMMSS);
        mgtInfo.setReceiptDate(receiptDt.substring(0, 8));
        mgtInfo.setReceiptTime(receiptDt.substring(8, 14));
        mgtMapper.insertBreakdownMgtInfo(mgtInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyBreakdownInfo(BreakdownInfo info) {
        // 
        mapper.updateBreakdownInfo(info);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BreakdownInfo retrieveBreakdownInfo(String id) {
        // 
        return mapper.selectBreakdownInfo(id);
    }
    

}

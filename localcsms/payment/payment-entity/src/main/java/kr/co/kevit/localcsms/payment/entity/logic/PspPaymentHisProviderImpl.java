/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.PspPaymentHisProvider;
import kr.co.kevit.localcsms.payment.entity.dao.PspPaymentHisMapper;
import kr.co.kevit.localcsms.payment.entity.domain.PspPaymentHis;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentHisSearchCond;

/**
 * @author bckim
 * @since 2026. 5. 25.
 */
@Component
public class PspPaymentHisProviderImpl implements PspPaymentHisProvider {

    @Autowired
    private PspPaymentHisMapper mapper;

    @Override
    public void registerPspPaymentHis(PspPaymentHis his) {
        mapper.insertPspPaymentHis(his);
    }

    @Override
    public Page<PspPaymentHisDto> retrievePspPaymentHisBySearchCond(PspPaymentHisSearchCond searchCond) {
        int totCnt = mapper.countPspPaymentHisBySearchCond(searchCond);
        Page<PspPaymentHisDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<PspPaymentHisDto> result = mapper.selectPspPaymentHisBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }
}

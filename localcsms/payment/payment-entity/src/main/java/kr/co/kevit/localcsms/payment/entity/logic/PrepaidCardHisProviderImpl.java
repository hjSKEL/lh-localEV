/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.PrepaidCardHisProvider;
import kr.co.kevit.localcsms.payment.entity.dao.PrepaidCardHisMapper;
import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCardHis;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardHisSearchCond;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Component
public class PrepaidCardHisProviderImpl implements PrepaidCardHisProvider {

    @Autowired
    private PrepaidCardHisMapper mapper;

    @Override
    public void registerPrepaidCardHis(PrepaidCardHis history) {
        mapper.insertPrepaidCardHis(history);
    }

    @Override
    public PrepaidCardHis retrievePrepaidCardHis(Long seq) {
        return mapper.selectPrepaidCardHis(seq);
    }

    @Override
    public Page<PrepaidCardHisDto> retrievePrepaidCardHisBySearchCond(PrepaidCardHisSearchCond searchCond) {
        int totCnt = mapper.countPrepaidCardHisBySearchCond(searchCond);
        Page<PrepaidCardHisDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<PrepaidCardHisDto> result = mapper.selectPrepaidCardHisBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

}

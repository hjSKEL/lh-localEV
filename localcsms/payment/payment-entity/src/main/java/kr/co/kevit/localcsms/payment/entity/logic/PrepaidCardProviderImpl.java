/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.PrepaidCardProvider;
import kr.co.kevit.localcsms.payment.entity.dao.PrepaidCardMapper;
import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCard;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardDto;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardSearchCond;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Component
public class PrepaidCardProviderImpl implements PrepaidCardProvider {

    @Autowired
    private PrepaidCardMapper mapper;

    @Override
    public void registerPrepaidCard(PrepaidCard card) {
        mapper.insertPrepaidCard(card);
    }

    @Override
    public void modifyPrepaidCard(PrepaidCard card) {
        mapper.updatePrepaidCard(card);
    }

    @Override
    public int modifyBalance(String cardNo, Long expectedBalance, Long newBalance, String updUserId) {
        return mapper.updateBalance(cardNo, expectedBalance, newBalance, updUserId);
    }

    @Override
    public int modifyCardStatus(String cardNo, String cardStatCode, String updUserId) {
        return mapper.updateCardStatus(cardNo, cardStatCode, updUserId);
    }

    @Override
    public PrepaidCard retrievePrepaidCard(String cardNo) {
        return mapper.selectPrepaidCard(cardNo);
    }

    @Override
    public PrepaidCard retrievePrepaidCardForUpdate(String cardNo) {
        return mapper.selectPrepaidCardForUpdate(cardNo);
    }

    @Override
    public Page<PrepaidCardDto> retrievePrepaidCardBySearchCond(PrepaidCardSearchCond searchCond) {
        int totCnt = mapper.countPrepaidCardBySearchCond(searchCond);
        Page<PrepaidCardDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<PrepaidCardDto> result = mapper.selectPrepaidCardBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

}

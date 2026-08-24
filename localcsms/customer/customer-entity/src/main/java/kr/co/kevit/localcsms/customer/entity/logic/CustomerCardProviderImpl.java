/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.customer.entity.CustomerCardProvider;
import kr.co.kevit.localcsms.customer.entity.dao.CustomerCardMapper;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerCard;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardSearchCond;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 10.
 */
@Component
public class CustomerCardProviderImpl implements CustomerCardProvider {
    
    @Autowired
    private CustomerCardMapper mapper;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerMemberCard(CustomerCard memberCard) {
        // 
        mapper.insertMemberCard(memberCard);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyMemberCard(CustomerCard memberCard) {
        // 
        mapper.updateMemberCard(memberCard);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomerCard retrieveMemberCard(String cutCardNo) {
        // 
        return mapper.selectMemberCard(cutCardNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CustomerCardDto> retrieveMemberCardByMemberCardSearchCond(CustomerCardSearchCond searchCond) {
        // 
        int totCnt = mapper.countMemberCardByMemberCardSearchCond(searchCond);
        Page<CustomerCardDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if(totCnt == 0) {
            return resultSet;
        }
        List<CustomerCardDto> result = mapper.selectMemberCardByMemberCardSearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countMemberCardByMemberCardSearchCond(CustomerCardSearchCond searchCond) {
        //
        return mapper.countMemberCardByMemberCardSearchCond(searchCond);
    }
}

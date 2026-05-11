/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.process.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.customer.entity.CustomerCardProvider;
import kr.co.kevit.localcsms.customer.entity.CustomerMgtProvider;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerCard;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardSearchCond;
import kr.co.kevit.localcsms.customer.process.CustomerCardService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 10.
 */
@Service
@Transactional
public class CustomerCardServiceImpl implements CustomerCardService {
    
    @Autowired
    private CustomerCardProvider provider;
    
    @Autowired
    private CustomerMgtProvider cuMgtProvider;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerMemberCard(CustomerCard memberCard) {
        // 
        provider.registerMemberCard(memberCard);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyMemberCard(CustomerCard memberCard) {
        // 
        CustomerCard oldCard = provider.retrieveMemberCard(memberCard.getCutCardNo());
        if(oldCard == null) {
            throw new KEVITException("존재하는 카드번호가 없습니다.");
        }
        if(!"MEML01".equals(oldCard.getCustStatCode())){
            throw new KEVITException("이미 정지된 카드번호 입니다.");
        }
        
        if("MEML02".equals(memberCard.getCustStatCode())){
            oldCard.setLossDate(memberCard.getWriter().getUpdateDate());
            oldCard.setLossId(memberCard.getWriter().getUpdUserId());
        }else{
            oldCard.setDelDate(memberCard.getWriter().getUpdateDate());
            oldCard.setDeleteId(memberCard.getWriter().getUpdUserId());
        }
        oldCard.setCustStatCode(memberCard.getCustStatCode());
        oldCard.setWriter(memberCard.getWriter());
        provider.modifyMemberCard(oldCard);
        
        CustomerMgt customerMgt = cuMgtProvider.retrieveCustomerMgtByCustomerCardNo(memberCard.getCutCardNo());
        customerMgt.setStopYn(StringConstants.Y);
        customerMgt.setStopDate(memberCard.getWriter().getUpdateDate());
        customerMgt.setUpdateDate(memberCard.getWriter().getUpdateDate());
        cuMgtProvider.modifyCustomerMgt(customerMgt);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public CustomerCard retrieveMemberCard(String cutCardNo) {
        // 
        return provider.retrieveMemberCard(cutCardNo);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<CustomerCardDto> retrieveMemberCardByMemberCardSearchCond(CustomerCardSearchCond searchCond) {
        // 
        return provider.retrieveMemberCardByMemberCardSearchCond(searchCond);
    }

    @Transactional(readOnly = true)

    @Override
    public CustomerCard retrieveMemberCardByCustomerId(String customerId, String custStatCode) {
        //
        return provider.retrieveMemberCardByCustomerId(customerId, custStatCode);
    }
}

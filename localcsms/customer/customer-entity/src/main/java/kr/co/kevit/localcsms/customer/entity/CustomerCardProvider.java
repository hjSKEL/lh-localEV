/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerCard;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardSearchCond;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 10.
 */
public interface CustomerCardProvider {
    
    void registerMemberCard(CustomerCard memberCard);
    
    void modifyMemberCard(CustomerCard memberCard);
    
    CustomerCard retrieveMemberCard(String cutCardNo);
    
    Page<CustomerCardDto> retrieveMemberCardByMemberCardSearchCond(CustomerCardSearchCond searchCond);

    CustomerCard retrieveMemberCardByCustomerId(String customerId, String custStatCode);
}

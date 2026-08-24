/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.process;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerCard;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 13.
 */
public class CustomerCardServiceTest extends AbstractTestCase {
    
    @Autowired
    private CustomerCardService service;

    private CustomerCard registerMemberCard() {
        CustomerCard memberCard = new CustomerCard();
        memberCard.setCutCardNo("1234567890123456");
        memberCard.setCustomerId("C00000001");
        memberCard.setLossYn("N");
        memberCard.setStopYn("N");
        Writer writer = new Writer("E00000001");
        memberCard.setWriter(writer);
        service.registerMemberCard(memberCard);
        return memberCard;
    }

    @Test
    public void testRegisterMemberCard() {
        //
        CustomerCard memberCard = registerMemberCard();
        assertNotNull(memberCard);
    }

    @Test
    public void testModifyMemberCard() {
        //
        CustomerCard memberCard = registerMemberCard();
        memberCard.setCustomerId("C00000001");
        memberCard.setLossYn("Y");
        memberCard.setStopYn("Y");
        memberCard.setStopDate(new Date());
        memberCard.setLossDate(new Date());
        memberCard.setWriter(new Writer("E00000001"));
        service.modifyMemberCard(memberCard);
    }

    @Test
    public void testRetrieveMemberCard() {
        //
        CustomerCard oldCard = registerMemberCard();
        CustomerCard memberCard = service.retrieveMemberCard(oldCard.getCutCardNo());
        assertNotNull(memberCard);
    }

    @Test
    public void testRetrieveMemberCardByMemberCardSearchCond() {
        //
        CustomerCard oldCard = registerMemberCard();
        CustomerCardSearchCond searchCond = new CustomerCardSearchCond();
        searchCond.setCustomerId(oldCard.getCustomerId());
        searchCond.setStopYn(oldCard.getStopYn());
        searchCond.setCutCardNo(oldCard.getCutCardNo());
        Page<CustomerCardDto> resultSet = service.retrieveMemberCardByMemberCardSearchCond(searchCond);
        assertTrue(resultSet.getCriteria().getTotalItemCount() > 0);
    }

}

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
    
/*
'MEML01', 'MEML00', '미사용', 1, '회원카드(티머니) 상태-미사용
'MEML02', 'MEML00', '발송', 2, '회원카드(티머니) 상태-발송
'MEML03', 'MEML00', '수령', 3, '회원카드(티머니) 상태-수령
'MEML04', 'MEML00', '분실', 4, '회원카드(티머니) 상태-분실
'MEML05', 'MEML00', '삭제/불량', 5, '회원카드(티머니) 상태-삭제/불량
'MEML06', 'MEML00', '재발급요청', 5, '회원카드(티머니) 상태-재발급요청
*/
    
    private CustomerCard registerMemberCard() {
        CustomerCard memberCard = new CustomerCard();
        memberCard.setCutCardNo("1234567890123456");
        memberCard.setCustomerId("C00000001");
        memberCard.setCustStatCode("MEML01");
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
        memberCard.setCustStatCode("MEML02");
        memberCard.setCustomerId("C00000001");
        memberCard.setDelDate(new Date());
        memberCard.setDeleteId("C00000001");
        memberCard.setLossDate(new Date());
        memberCard.setLossId("C00000001");
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
    public void testRetrieveMemberCardByCustomerId() {
        //
        CustomerCard oldCard = registerMemberCard();
        CustomerCard memberCard = service.retrieveMemberCardByCustomerId(oldCard.getCustomerId(), oldCard.getCustStatCode());
        assertNotNull(memberCard);
    }
    
    @Test
    public void testRetrieveMemberCardByMemberCardSearchCond() {
        //
        CustomerCard oldCard = registerMemberCard();
        CustomerCardSearchCond searchCond = new CustomerCardSearchCond();
        searchCond.setCustomerId(oldCard.getCustomerId());
        searchCond.setCustStatCode(oldCard.getCustStatCode());
        searchCond.setCutCardNo(oldCard.getCutCardNo());
        Page<CustomerCardDto> resultSet = service.retrieveMemberCardByMemberCardSearchCond(searchCond);
        assertTrue(resultSet.getCriteria().getTotalItemCount() > 0);
    }

}

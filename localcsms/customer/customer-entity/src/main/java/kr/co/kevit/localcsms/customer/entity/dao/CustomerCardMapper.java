/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.customer.entity.domain.CustomerCard;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardSearchCond;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 10.
 */
@Repository
public interface CustomerCardMapper {
    
    int insertMemberCard(@Param("memberCard") CustomerCard memberCard);
    
    int updateMemberCard(@Param("memberCard") CustomerCard memberCard);
    
    CustomerCardDto selectMemberCard(@Param("cutCardNo") String cutCardNo);
    
    int countMemberCardByMemberCardSearchCond(@Param("searchCond") CustomerCardSearchCond searchCond);
    
    List<CustomerCardDto> selectMemberCardByMemberCardSearchCond(@Param("searchCond") CustomerCardSearchCond searchCond);

    CustomerCard selectMemberCardByCustomerId(@Param("customerId") String customerId, @Param("custStatCode")  String custStatCode);

    CustomerCard selectMemberCardByCustomerIdIsNull();
}

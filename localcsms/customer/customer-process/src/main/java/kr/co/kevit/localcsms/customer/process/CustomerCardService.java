/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.process;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerCard;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardSearchCond;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 10.
 */
public interface CustomerCardService {
    
    void registerMemberCard(CustomerCard memberCard);
    
    void modifyMemberCard(CustomerCard memberCard);
    
    CustomerCard retrieveMemberCard(String cutCardNo);

    Page<CustomerCardDto> retrieveMemberCardByMemberCardSearchCond(CustomerCardSearchCond searchCond);

    /**
     * 회원 기본 충전 한도(금액/에너지/시간/SoC) 저장 — 실제로는 TB_CUCU002(CustomerMgt)에 반영된다.
     * cutCardNo 카드에 짝이 되는 TB_CUCU002 행이 없으면 KEVITException.
     */
    void modifyChargeLimit(String cutCardNo, Double maxCost, Double maxEnergy, Integer maxTime, Integer maxSoC);
}

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
        if(memberCard.getCustomerId() != null) {
            CustomerCardSearchCond cond = new CustomerCardSearchCond();
            cond.setCustomerId(memberCard.getCustomerId());
            cond.setStopYn(StringConstants.N);
            int activeCardCount = provider.countMemberCardByMemberCardSearchCond(cond);
            if(activeCardCount >= 5) {
                throw new KEVITException("세대당 회원카드는 5개까지 등록 가능합니다.");
            }
        }
        provider.registerMemberCard(memberCard);

        // 실시간 충전인증(TB_CUCU002)에도 같은 카드를 동기화 — 카드별 독립 인증행
        if(memberCard.getCustomerId() != null) {
            CustomerMgt customerMgt = new CustomerMgt();
            customerMgt.setCutCardNo(memberCard.getCutCardNo());
            customerMgt.setCustomerId(memberCard.getCustomerId());
            customerMgt.setStopYn(StringConstants.N);
            customerMgt.setRegistrationDate(memberCard.getWriter().getRegistrationDate());
            customerMgt.setUpdateDate(memberCard.getWriter().getRegistrationDate());
            customerMgt.setRegCertDate(memberCard.getWriter().getRegistrationDate());
            cuMgtProvider.registerCustomerMgt(customerMgt);
        }
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
        if(StringConstants.Y.equals(oldCard.getStopYn())){
            throw new KEVITException("이미 정지된 카드번호 입니다.");
        }

        // 상태변경(정지)은 분실/삭제·불량 두 경우뿐 — 어느 쪽이든 카드는 정지되고, lossYn으로 사유만 구분
        if(StringConstants.Y.equals(memberCard.getLossYn())){
            oldCard.setLossYn(StringConstants.Y);
            oldCard.setLossDate(memberCard.getWriter().getUpdateDate());
        }else{
            oldCard.setLossYn(StringConstants.N);
        }
        oldCard.setStopYn(StringConstants.Y);
        oldCard.setStopDate(memberCard.getWriter().getUpdateDate());
        oldCard.setWriter(memberCard.getWriter());
        provider.modifyMemberCard(oldCard);
        
        // 이 fix 이전에 등록된 카드는 짝이 되는 TB_CUCU002 행이 없을 수 있음 — 있으면만 같이 정지
        CustomerMgt customerMgt = cuMgtProvider.retrieveCustomerMgtByCustomerCardNo(memberCard.getCutCardNo());
        if(customerMgt != null) {
            customerMgt.setStopYn(StringConstants.Y);
            customerMgt.setStopDate(memberCard.getWriter().getUpdateDate());
            customerMgt.setUpdateDate(memberCard.getWriter().getUpdateDate());
            cuMgtProvider.modifyCustomerMgt(customerMgt);
        }
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
}

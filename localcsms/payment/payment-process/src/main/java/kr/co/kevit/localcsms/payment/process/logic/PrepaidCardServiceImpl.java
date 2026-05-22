/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.process.logic;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.PrepaidCardHisProvider;
import kr.co.kevit.localcsms.payment.entity.PrepaidCardProvider;
import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCard;
import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCardHis;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardDto;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardHisSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardSearchCond;
import kr.co.kevit.localcsms.payment.process.PrepaidCardService;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Service
@Transactional
public class PrepaidCardServiceImpl implements PrepaidCardService {

    /** 활성 상태 */
    private static final String STAT_ACTIVE = "PPCS01";

    @Autowired
    private PrepaidCardProvider cardProvider;

    @Autowired
    private PrepaidCardHisProvider hisProvider;

    @Override
    public void issuePrepaidCard(PrepaidCard card) {
        if (card.getCardNo() == null || card.getCardNo().isEmpty()) {
            throw new KEVITException("선불카드번호가 비어 있습니다.");
        }
        if (cardProvider.retrievePrepaidCard(card.getCardNo()) != null) {
            throw new KEVITException("이미 등록된 선불카드번호 입니다.");
        }
        if (card.getBalance() == null || card.getBalance() < 0L) {
            throw new KEVITException("초기 잔액은 0 이상이어야 합니다.");
        }
        if (card.getCardStatCode() == null || card.getCardStatCode().isEmpty()) {
            card.setCardStatCode(STAT_ACTIVE);
        }

        cardProvider.registerPrepaidCard(card);

        if (card.getBalance() > 0L) {
            PrepaidCardHis history = new PrepaidCardHis();
            history.setCardNo(card.getCardNo());
            history.setTypeCode(PrepaidCardHis.TYPE_ISSUE);
            history.setAmount(card.getBalance());
            history.setBalanceBefore(0L);
            history.setBalanceAfter(card.getBalance());
            history.setRechargingId(null);
            history.setWriter(card.getWriter());
            hisProvider.registerPrepaidCardHis(history);
        }
    }

    @Override
    public PrepaidCardHis usePrepaidCard(String cardNo, Long amount, String rechargingId, String updUserId) {
        if (cardNo == null || cardNo.isEmpty()) {
            throw new KEVITException("선불카드번호가 비어 있습니다.");
        }
        if (amount == null || amount <= 0L) {
            throw new KEVITException("차감 금액은 0보다 커야 합니다.");
        }
        if (rechargingId == null || rechargingId.isEmpty()) {
            throw new KEVITException("충전ID가 필요합니다.");
        }

        PrepaidCard card = cardProvider.retrievePrepaidCardForUpdate(cardNo);
        if (card == null) {
            throw new KEVITException("등록되지 않은 선불카드 입니다.");
        }
        if (!STAT_ACTIVE.equals(card.getCardStatCode())) {
            throw new KEVITException("사용할 수 없는 상태의 카드 입니다. 상태=" + card.getCardStatCode());
        }
        if (card.getExpireDate() != null && card.getExpireDate().before(new Date())) {
            throw new KEVITException("만료된 카드 입니다.");
        }

        Long before = card.getBalance() == null ? 0L : card.getBalance();
        if (before < amount) {
            throw new KEVITException("잔액이 부족합니다. 잔액=" + before + ", 요청=" + amount);
        }
        Long after = before - amount;

        int affected = cardProvider.modifyBalance(cardNo, before, after, updUserId);
        if (affected != 1) {
            throw new KEVITException("선불카드 잔액 갱신 실패(동시성 충돌). 카드=" + cardNo);
        }

        Date now = new Date();
        PrepaidCardHis history = new PrepaidCardHis();
        history.setCardNo(cardNo);
        history.setTypeCode(PrepaidCardHis.TYPE_USE);
        history.setAmount(-amount);
        history.setBalanceBefore(before);
        history.setBalanceAfter(after);
        history.setRechargingId(rechargingId);
        Writer writer = new Writer(updUserId);
        writer.setRegistrationDate(now);
        writer.setUpdateDate(now);
        history.setWriter(writer);
        hisProvider.registerPrepaidCardHis(history);

        return history;
    }

    @Override
    public void modifyPrepaidCard(PrepaidCard card) {
        if (card == null || card.getCardNo() == null || card.getCardNo().isEmpty()) {
            throw new KEVITException("선불카드번호가 비어 있습니다.");
        }
        cardProvider.modifyPrepaidCard(card);
    }

    @Override
    public void modifyCardStatus(String cardNo, String cardStatCode, String updUserId) {
        if (cardNo == null || cardNo.isEmpty()) {
            throw new KEVITException("선불카드번호가 비어 있습니다.");
        }
        if (cardStatCode == null || cardStatCode.isEmpty()) {
            throw new KEVITException("카드상태코드가 비어 있습니다.");
        }
        PrepaidCard card = cardProvider.retrievePrepaidCard(cardNo);
        if (card == null) {
            throw new KEVITException("등록되지 않은 선불카드 입니다.");
        }
        int affected = cardProvider.modifyCardStatus(cardNo, cardStatCode, updUserId);
        if (affected != 1) {
            throw new KEVITException("선불카드 상태 변경에 실패했습니다. 카드=" + cardNo);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public PrepaidCard retrievePrepaidCard(String cardNo) {
        return cardProvider.retrievePrepaidCard(cardNo);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PrepaidCardDto> retrievePrepaidCardBySearchCond(PrepaidCardSearchCond searchCond) {
        return cardProvider.retrievePrepaidCardBySearchCond(searchCond);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PrepaidCardHisDto> retrievePrepaidCardHisBySearchCond(PrepaidCardHisSearchCond searchCond) {
        return hisProvider.retrievePrepaidCardHisBySearchCond(searchCond);
    }

}

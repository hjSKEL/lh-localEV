/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.logic;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.PspPaymentProvider;
import kr.co.kevit.localcsms.payment.entity.dao.PspPaymentMapper;
import kr.co.kevit.localcsms.payment.entity.domain.PspPayment;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentDto;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentSearchCond;

/**
 * @author bckim
 * @since 2026. 5. 25.
 */
@Component
public class PspPaymentProviderImpl implements PspPaymentProvider {

    @Autowired
    private PspPaymentMapper mapper;

    @Override
    public void registerPspPayment(PspPayment payment) {
        mapper.insertPspPayment(payment);
    }

    @Override
    public int modifyPspPayment(PspPayment payment) {
        return mapper.updatePspPayment(payment);
    }

    @Override
    public int modifyStatus(String pspRef, String statusCd, String reasonCd, String updUserId) {
        return mapper.updateStatus(pspRef, statusCd, reasonCd, updUserId);
    }

    @Override
    public int linkRechargingId(String pspRef, String rechargingId, Date startedDate, String updUserId) {
        return mapper.updateLinkRechargingId(pspRef, rechargingId, startedDate, updUserId);
    }

    @Override
    public int settle(String pspRef, BigDecimal settledAmount, String receiptUrl, String receiptId,
                      Date settledDate, String updUserId) {
        return mapper.updateSettle(pspRef, settledAmount, receiptUrl, receiptId, settledDate, updUserId);
    }

    @Override
    public int cancel(String pspRef, String reasonCd, Date canceledDate, String updUserId) {
        return mapper.updateCancel(pspRef, reasonCd, canceledDate, updUserId);
    }

    @Override
    public PspPayment retrievePspPayment(String pspRef) {
        return mapper.selectPspPayment(pspRef);
    }

    @Override
    public PspPayment retrievePspPaymentByRechargingId(String rechargingId) {
        return mapper.selectPspPaymentByRechargingId(rechargingId);
    }

    @Override
    public Page<PspPaymentDto> retrievePspPaymentBySearchCond(PspPaymentSearchCond searchCond) {
        int totCnt = mapper.countPspPaymentBySearchCond(searchCond);
        Page<PspPaymentDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<PspPaymentDto> result = mapper.selectPspPaymentBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

    @Override
    public int retrieveDailySequence(String yyyymmdd) {
        Integer next = mapper.selectDailySequence(yyyymmdd);
        return next == null ? 1 : (next + 1);
    }
}

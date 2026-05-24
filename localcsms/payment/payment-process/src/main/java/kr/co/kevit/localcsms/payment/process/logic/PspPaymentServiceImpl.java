/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.process.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.PspPaymentHisProvider;
import kr.co.kevit.localcsms.payment.entity.PspPaymentProvider;
import kr.co.kevit.localcsms.payment.entity.domain.PspPayment;
import kr.co.kevit.localcsms.payment.entity.domain.PspPaymentHis;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentDto;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentHisSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentSearchCond;
import kr.co.kevit.localcsms.payment.process.PspPaymentService;

/**
 * @author bckim
 * @since 2026. 5. 25.
 */
@Service
@Transactional
public class PspPaymentServiceImpl implements PspPaymentService {

    @Autowired
    private PspPaymentProvider pspPaymentProvider;

    @Autowired
    private PspPaymentHisProvider pspPaymentHisProvider;

    @Override
    public String generateNextPspRef() {
        String yyyymmdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int seq = pspPaymentProvider.retrieveDailySequence(yyyymmdd);
        return String.format("PSP-%s-%04d", yyyymmdd, seq);
    }

    @Override
    public PspPayment registerPspPayment(PspPayment payment) {
        if (payment == null) {
            throw new KEVITException("입력값이 비어 있습니다.");
        }
        if (isBlank(payment.getCpId()) || isBlank(payment.getCsId())) {
            throw new KEVITException("충전소/충전기 ID 가 비어 있습니다.");
        }
        if (payment.getAuthAmount() == null) {
            throw new KEVITException("승인 금액이 비어 있습니다.");
        }
        if (isBlank(payment.getPspRef())) {
            payment.setPspRef(generateNextPspRef());
        } else if (pspPaymentProvider.retrievePspPayment(payment.getPspRef()) != null) {
            throw new KEVITException("이미 등록된 PSP Ref 입니다: " + payment.getPspRef());
        }
        Date now = new Date();
        if (payment.getAuthDate() == null) payment.setAuthDate(now);
        if (isBlank(payment.getStatusCd())) payment.setStatusCd(PspPayment.STATUS_AUTHORIZED);
        if (isBlank(payment.getCurrency())) payment.setCurrency("KRW");

        Writer writer = payment.getWriter();
        if (writer == null) {
            writer = new Writer();
            payment.setWriter(writer);
        }
        if (writer.getRegistrationDate() == null) writer.setRegistrationDate(now);
        if (writer.getUpdateDate() == null) writer.setUpdateDate(now);
        if (isBlank(writer.getRegUserId())) writer.setRegUserId("system");
        if (isBlank(writer.getUpdUserId())) writer.setUpdUserId(writer.getRegUserId());

        pspPaymentProvider.registerPspPayment(payment);
        recordHistory(payment.getPspRef(), null, payment.getStatusCd(), payment.getAuthAmount(),
                payment.getReasonCd(), null, writer.getRegUserId(), now);
        return payment;
    }

    @Override
    public void modifyStatus(String pspRef, String postStatus, String reasonCd, String remark, String operId) {
        PspPayment current = mustExist(pspRef);
        validateTransition(current.getStatusCd(), postStatus);
        int affected = pspPaymentProvider.modifyStatus(pspRef, postStatus, reasonCd, operId);
        if (affected != 1) {
            throw new KEVITException("상태 변경 실패. PSP=" + pspRef);
        }
        recordHistory(pspRef, current.getStatusCd(), postStatus, null, reasonCd, remark, operId, new Date());
    }

    @Override
    public void linkTransaction(String pspRef, String rechargingId, Date startedDate, String operId) {
        PspPayment current = mustExist(pspRef);
        if (!PspPayment.STATUS_AUTHORIZED.equals(current.getStatusCd())) {
            throw new KEVITException("AUTHORIZED 상태에서만 트랜잭션 연동이 가능합니다. 현재=" + current.getStatusCd());
        }
        Date dt = startedDate != null ? startedDate : new Date();
        int affected = pspPaymentProvider.linkRechargingId(pspRef, rechargingId, dt, operId);
        if (affected != 1) {
            throw new KEVITException("트랜잭션 연동 실패. PSP=" + pspRef);
        }
        recordHistory(pspRef, PspPayment.STATUS_AUTHORIZED, PspPayment.STATUS_STARTED, null,
                null, "rechargingId=" + rechargingId, operId, dt);
    }

    @Override
    public void settle(String pspRef, BigDecimal settledAmount, String receiptUrl, String receiptId, String operId) {
        PspPayment current = mustExist(pspRef);
        if (!PspPayment.STATUS_STARTED.equals(current.getStatusCd())
                && !PspPayment.STATUS_AUTHORIZED.equals(current.getStatusCd())) {
            throw new KEVITException("AUTHORIZED/STARTED 상태에서만 정산 가능합니다. 현재=" + current.getStatusCd());
        }
        if (settledAmount == null) {
            throw new KEVITException("정산 금액이 비어 있습니다.");
        }
        Date now = new Date();
        int affected = pspPaymentProvider.settle(pspRef, settledAmount, receiptUrl, receiptId, now, operId);
        if (affected != 1) {
            throw new KEVITException("정산 처리 실패. PSP=" + pspRef);
        }
        recordHistory(pspRef, current.getStatusCd(), PspPayment.STATUS_SETTLED, settledAmount,
                null, "receiptId=" + receiptId, operId, now);
    }

    @Override
    public void cancel(String pspRef, String reasonCd, String remark, String operId) {
        PspPayment current = mustExist(pspRef);
        if (PspPayment.STATUS_SETTLED.equals(current.getStatusCd())) {
            throw new KEVITException("정산 완료 건은 취소할 수 없습니다.");
        }
        if (PspPayment.STATUS_CANCELED.equals(current.getStatusCd())) {
            throw new KEVITException("이미 취소된 건입니다.");
        }
        Date now = new Date();
        int affected = pspPaymentProvider.cancel(pspRef, reasonCd, now, operId);
        if (affected != 1) {
            throw new KEVITException("취소 처리 실패. PSP=" + pspRef);
        }
        recordHistory(pspRef, current.getStatusCd(), PspPayment.STATUS_CANCELED, null,
                reasonCd, remark, operId, now);
    }

    @Transactional(readOnly = true)
    @Override
    public PspPayment retrievePspPayment(String pspRef) {
        return pspPaymentProvider.retrievePspPayment(pspRef);
    }

    @Transactional(readOnly = true)
    @Override
    public PspPayment retrievePspPaymentByRechargingId(String rechargingId) {
        return pspPaymentProvider.retrievePspPaymentByRechargingId(rechargingId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PspPaymentDto> retrievePspPaymentBySearchCond(PspPaymentSearchCond searchCond) {
        return pspPaymentProvider.retrievePspPaymentBySearchCond(searchCond);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PspPaymentHisDto> retrievePspPaymentHisBySearchCond(PspPaymentHisSearchCond searchCond) {
        return pspPaymentHisProvider.retrievePspPaymentHisBySearchCond(searchCond);
    }

    private void recordHistory(String pspRef, String pre, String post, BigDecimal amount,
                               String reasonCd, String remark, String operId, Date occurredDate) {
        PspPaymentHis his = new PspPaymentHis();
        his.setPspRef(pspRef);
        his.setPreStatusCd(pre);
        his.setPostStatusCd(post);
        his.setAmount(amount);
        his.setReasonCd(reasonCd);
        his.setRemark(remark);
        his.setOperId(operId);
        his.setOccurredDate(occurredDate != null ? occurredDate : new Date());
        pspPaymentHisProvider.registerPspPaymentHis(his);
    }

    private PspPayment mustExist(String pspRef) {
        if (isBlank(pspRef)) {
            throw new KEVITException("PSP Ref 가 비어 있습니다.");
        }
        PspPayment current = pspPaymentProvider.retrievePspPayment(pspRef);
        if (current == null) {
            throw new KEVITException("등록되지 않은 PSP Ref 입니다: " + pspRef);
        }
        return current;
    }

    private void validateTransition(String from, String to) {
        if (PspPayment.STATUS_SETTLED.equals(from) || PspPayment.STATUS_CANCELED.equals(from)) {
            throw new KEVITException("종결 상태(" + from + ")에서는 변경할 수 없습니다.");
        }
        if (from != null && from.equals(to)) {
            throw new KEVITException("이미 동일한 상태입니다: " + to);
        }
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}

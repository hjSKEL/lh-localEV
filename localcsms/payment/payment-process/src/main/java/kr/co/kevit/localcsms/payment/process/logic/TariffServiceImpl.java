/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.process.logic;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.TariffAssignmentProvider;
import kr.co.kevit.localcsms.payment.entity.TariffHisProvider;
import kr.co.kevit.localcsms.payment.entity.TariffProvider;
import kr.co.kevit.localcsms.payment.entity.domain.Tariff;
import kr.co.kevit.localcsms.payment.entity.domain.TariffAssignment;
import kr.co.kevit.localcsms.payment.entity.domain.TariffHis;
import kr.co.kevit.localcsms.payment.entity.shared.TariffAssignmentDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffAssignmentSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.TariffDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffHisSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.TariffSearchCond;
import kr.co.kevit.localcsms.payment.process.TariffService;

/**
 * @author bckim
 * @since 2026. 5. 25.
 */
@Service
@Transactional
public class TariffServiceImpl implements TariffService {

    @Autowired
    private TariffProvider tariffProvider;

    @Autowired
    private TariffAssignmentProvider assignmentProvider;

    @Autowired
    private TariffHisProvider hisProvider;

    @Override
    public String generateNextTariffId(String tariffKind) {
        String yyyymmdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int seq = tariffProvider.retrieveDailySequence(tariffKind, yyyymmdd);
        return String.format("T-%s-%s-%04d", tariffKind, yyyymmdd, seq);
    }

    @Override
    public Tariff registerTariff(Tariff tariff, String operId) {
        if (tariff == null) {
            throw new KEVITException("입력값이 비어 있습니다.");
        }
        if (isBlank(tariff.getTariffKind())) {
            throw new KEVITException("Tariff 종류(kind) 가 비어 있습니다.");
        }
        if (isBlank(tariff.getCurrency())) {
            throw new KEVITException("통화(currency) 가 비어 있습니다.");
        }
        if (isBlank(tariff.getTariffJson())) {
            throw new KEVITException("Tariff JSON 이 비어 있습니다.");
        }
        if (isBlank(tariff.getTariffId())) {
            tariff.setTariffId(generateNextTariffId(tariff.getTariffKind()));
        } else if (tariffProvider.retrieveTariff(tariff.getTariffId()) != null) {
            throw new KEVITException("이미 등록된 tariffId 입니다: " + tariff.getTariffId());
        }
        Date now = new Date();
        if (tariff.getValidFrom() == null) tariff.setValidFrom(now);
        if (isBlank(tariff.getStatusCd())) tariff.setStatusCd(Tariff.STATUS_ACTIVE);

        Writer writer = tariff.getWriter();
        if (writer == null) {
            writer = new Writer();
            tariff.setWriter(writer);
        }
        if (writer.getRegistrationDate() == null) writer.setRegistrationDate(now);
        if (writer.getUpdateDate() == null)       writer.setUpdateDate(now);
        if (isBlank(writer.getRegUserId()))       writer.setRegUserId(isBlank(operId) ? "system" : operId);
        if (isBlank(writer.getUpdUserId()))       writer.setUpdUserId(writer.getRegUserId());

        tariffProvider.registerTariff(tariff);
        recordHistory(tariff.getTariffId(), null, TariffHis.ACTION_REGISTER, null, tariff.getStatusCd(),
                "kind=" + tariff.getTariffKind(), operId, now);
        return tariff;
    }

    @Override
    public void deprecateTariff(String tariffId, String newStatus, String reason, String operId) {
        Tariff cur = tariffProvider.retrieveTariff(tariffId);
        if (cur == null) {
            throw new KEVITException("존재하지 않는 tariffId 입니다: " + tariffId);
        }
        if (Tariff.STATUS_REPLACED.equals(cur.getStatusCd())
                || Tariff.STATUS_CLEARED.equals(cur.getStatusCd())
                || Tariff.STATUS_EXPIRED.equals(cur.getStatusCd())) {
            return;
        }
        Date now = new Date();
        int affected = tariffProvider.modifyTariffStatus(tariffId, newStatus, now, operId);
        if (affected != 1) {
            throw new KEVITException("Tariff 상태 변경 실패. id=" + tariffId);
        }
        recordHistory(tariffId, null,
                Tariff.STATUS_CLEARED.equals(newStatus) ? TariffHis.ACTION_CLEAR : TariffHis.ACTION_REPLACE,
                cur.getStatusCd(), newStatus, reason, operId, now);
    }

    @Transactional(readOnly = true)
    @Override
    public Tariff retrieveTariff(String tariffId) {
        return tariffProvider.retrieveTariff(tariffId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TariffDto> retrieveTariffBySearchCond(TariffSearchCond cond) {
        return tariffProvider.retrieveTariffBySearchCond(cond);
    }

    // === Driver Assignment ===

    @Override
    public TariffAssignment assignDriverTariff(String tariffId, String idToken, String operId) {
        Tariff master = mustExistMaster(tariffId);
        if (!Tariff.KIND_DRIVER.equals(master.getTariffKind())
                && !Tariff.KIND_ADHOC.equals(master.getTariffKind())) {
            throw new KEVITException("Driver Tariff 매핑에는 kind=DRIVER 또는 ADHOC 만 가능합니다. 현재=" + master.getTariffKind());
        }
        if (isBlank(idToken)) {
            throw new KEVITException("idToken 이 비어 있습니다.");
        }
        // 기존 활성 매핑은 REPLACED 로 전이
        assignmentProvider.modifyStatusForActiveDriver(idToken, TariffAssignment.STATUS_REPLACED, operId);

        Date now = new Date();
        TariffAssignment a = new TariffAssignment();
        a.setTariffId(tariffId);
        a.setAssignType(TariffAssignment.TYPE_DRIVER_IDTOKEN);
        a.setIdToken(idToken);
        a.setValidFrom(master.getValidFrom() != null ? master.getValidFrom() : now);
        a.setStatusCd(TariffAssignment.STATUS_ACTIVE);   // driver 는 즉시 ACTIVE
        a.setWriter(makeWriter(operId, now));
        assignmentProvider.registerAssignment(a);

        recordHistory(tariffId, a.getSeq(), TariffHis.ACTION_REGISTER, null, TariffAssignment.STATUS_ACTIVE,
                "driver idToken=" + idToken, operId, now);
        return a;
    }

    @Override
    public void clearDriverTariff(String idToken, String operId) {
        int affected = assignmentProvider.modifyStatusForActiveDriver(idToken, TariffAssignment.STATUS_CLEARED, operId);
        if (affected > 0) {
            recordHistory(null, null, TariffHis.ACTION_CLEAR, TariffAssignment.STATUS_ACTIVE,
                    TariffAssignment.STATUS_CLEARED, "driver idToken=" + idToken, operId, new Date());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Tariff retrieveActiveDriverTariff(String idToken) {
        if (isBlank(idToken)) return null;
        TariffAssignment a = assignmentProvider.retrieveActiveDriverByIdToken(idToken);
        if (a == null) return null;
        return tariffProvider.retrieveTariff(a.getTariffId());
    }

    // === Default EVSE Assignment ===

    @Override
    public TariffAssignment assignDefaultTariffPending(String tariffId, String cpId, String csId, Integer evseId,
                                                       String operId) {
        Tariff master = mustExistMaster(tariffId);
        if (!Tariff.KIND_DEFAULT.equals(master.getTariffKind())) {
            throw new KEVITException("Default Tariff 매핑에는 kind=DEFAULT 만 가능합니다. 현재=" + master.getTariffKind());
        }
        if (isBlank(cpId) || isBlank(csId)) {
            throw new KEVITException("충전소/충전기 ID 가 비어 있습니다.");
        }

        Date now = new Date();
        TariffAssignment a = new TariffAssignment();
        a.setTariffId(tariffId);
        a.setAssignType(TariffAssignment.TYPE_DEFAULT_EVSE);
        a.setCpId(cpId);
        a.setCsId(csId);
        a.setEvseId(evseId);
        a.setValidFrom(master.getValidFrom() != null ? master.getValidFrom() : now);
        a.setStatusCd(TariffAssignment.STATUS_PENDING);    // push 결과 받기 전까지 PENDING
        a.setWriter(makeWriter(operId, now));
        assignmentProvider.registerAssignment(a);

        recordHistory(tariffId, a.getSeq(), TariffHis.ACTION_REGISTER, null, TariffAssignment.STATUS_PENDING,
                "default evse cpId=" + cpId + ",csId=" + csId + ",evseId=" + evseId, operId, now);
        return a;
    }

    @Override
    public void acknowledgeDefaultTariffPush(long assignmentSeq, String csStatus, String reason, String operId) {
        TariffAssignment a = assignmentProvider.retrieveAssignment(assignmentSeq);
        if (a == null) {
            throw new KEVITException("Assignment 가 존재하지 않습니다. seq=" + assignmentSeq);
        }
        Date now = new Date();
        String newStatus;
        if ("Accepted".equalsIgnoreCase(csStatus)) {
            // 기존 활성 매핑 REPLACED
            assignmentProvider.modifyStatusForActiveDefault(a.getCpId(), a.getCsId(), a.getEvseId(),
                    TariffAssignment.STATUS_REPLACED, operId);
            newStatus = TariffAssignment.STATUS_ACTIVE;
            assignmentProvider.modifyStatus(assignmentSeq, newStatus, now, null, operId);
        } else {
            newStatus = TariffAssignment.STATUS_REJECTED;
            assignmentProvider.modifyStatus(assignmentSeq, newStatus, null, reason, operId);
        }
        recordHistory(a.getTariffId(), assignmentSeq, TariffHis.ACTION_ACK,
                a.getStatusCd(), newStatus, "csStatus=" + csStatus + ", reason=" + reason, operId, now);
    }

    @Override
    public void acknowledgeClearTariffs(long assignmentSeq, String csStatus, String reason, String operId) {
        TariffAssignment a = assignmentProvider.retrieveAssignment(assignmentSeq);
        if (a == null) return;
        Date now = new Date();
        if ("Accepted".equalsIgnoreCase(csStatus)) {
            assignmentProvider.modifyStatus(assignmentSeq, TariffAssignment.STATUS_CLEARED, now, null, operId);
            recordHistory(a.getTariffId(), assignmentSeq, TariffHis.ACTION_CLEAR,
                    a.getStatusCd(), TariffAssignment.STATUS_CLEARED, "ClearTariffs accepted", operId, now);
        } else {
            recordHistory(a.getTariffId(), assignmentSeq, TariffHis.ACTION_ACK,
                    a.getStatusCd(), a.getStatusCd(), "ClearTariffs rejected: " + reason, operId, now);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Tariff retrieveActiveDefaultTariff(String cpId, String csId, int evseId) {
        TariffAssignment a = assignmentProvider.retrieveActiveDefaultByEvse(cpId, csId, evseId);
        if (a == null) return null;
        return tariffProvider.retrieveTariff(a.getTariffId());
    }

    // === 조회 ===

    @Transactional(readOnly = true)
    @Override
    public TariffAssignment retrieveAssignment(long seq) {
        return assignmentProvider.retrieveAssignment(seq);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TariffAssignmentDto> retrieveAssignmentBySearchCond(TariffAssignmentSearchCond cond) {
        return assignmentProvider.retrieveAssignmentBySearchCond(cond);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TariffHisDto> retrieveHisBySearchCond(TariffHisSearchCond cond) {
        return hisProvider.retrieveTariffHisBySearchCond(cond);
    }

    @Override
    public void recordHistory(String tariffId, Long assignmentSeq, String actionCd,
                              String preStatusCd, String postStatusCd, String remark, String operId,
                              Date occurredDate) {
        TariffHis his = new TariffHis();
        his.setTariffId(tariffId);
        his.setAssignmentSeq(assignmentSeq);
        his.setActionCd(actionCd);
        his.setPreStatusCd(preStatusCd);
        his.setPostStatusCd(postStatusCd);
        his.setRemark(remark);
        his.setOperId(operId);
        his.setOccurredDate(occurredDate != null ? occurredDate : new Date());
        hisProvider.registerTariffHis(his);
    }

    private Tariff mustExistMaster(String tariffId) {
        if (isBlank(tariffId)) {
            throw new KEVITException("tariffId 가 비어 있습니다.");
        }
        Tariff t = tariffProvider.retrieveTariff(tariffId);
        if (t == null) {
            throw new KEVITException("존재하지 않는 tariffId 입니다: " + tariffId);
        }
        if (!Tariff.STATUS_ACTIVE.equals(t.getStatusCd())) {
            throw new KEVITException("ACTIVE 상태의 Tariff 만 매핑 가능합니다. 현재=" + t.getStatusCd());
        }
        return t;
    }

    private Writer makeWriter(String operId, Date now) {
        Writer w = new Writer();
        w.setRegistrationDate(now);
        w.setUpdateDate(now);
        String uid = isBlank(operId) ? "system" : operId;
        w.setRegUserId(uid);
        w.setUpdUserId(uid);
        return w;
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}

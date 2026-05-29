/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.smartcharging.process.logic;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.smartcharging.entity.DerAlarmProvider;
import kr.co.kevit.localcsms.smartcharging.entity.DerControlHisProvider;
import kr.co.kevit.localcsms.smartcharging.entity.DerControlProvider;
import kr.co.kevit.localcsms.smartcharging.entity.DerStartStopProvider;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerAlarm;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerControl;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerControlHis;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerStartStop;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerAlarmDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerAlarmSearchCond;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerControlDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerControlHisDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerControlHisSearchCond;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerControlSearchCond;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerStartStopDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerStartStopSearchCond;
import kr.co.kevit.localcsms.smartcharging.process.DerControlService;

@Service
@Transactional
public class DerControlServiceImpl implements DerControlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DerControlServiceImpl.class);

    /** ReportDERControlRequest 내 sub-object key 화이트리스트 */
    private static final Set<String> REPORT_KEYS = new HashSet<>(Arrays.asList(
            "enterService", "freqDroop", "fixedPFAbsorb", "fixedPFInject",
            "fixedVar", "gradient", "limitMaxDischarge", "curve"
    ));

    @Autowired
    private DerControlProvider provider;

    @Autowired
    private DerControlHisProvider hisProvider;

    @Autowired
    private DerAlarmProvider alarmProvider;

    @Autowired
    private DerStartStopProvider startStopProvider;

    @Override
    public String generateControlId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public DerControl registerDerControl(DerControl ctrl, String operId) {
        if (ctrl == null) throw new KEVITException("입력값이 비어 있습니다.");
        if (isBlank(ctrl.getCpId()) || isBlank(ctrl.getCsId())) {
            throw new KEVITException("충전소/충전기 ID 가 비어 있습니다.");
        }
        if (isBlank(ctrl.getControlType())) {
            throw new KEVITException("controlType 이 비어 있습니다.");
        }
        if (isBlank(ctrl.getParamJson())) {
            throw new KEVITException("paramJson 이 비어 있습니다.");
        }
        if (isBlank(ctrl.getIsDefault())) ctrl.setIsDefault(DerControl.NO);
        if (isBlank(ctrl.getOriginCd())) ctrl.setOriginCd(DerControl.ORIGIN_CSMS);
        if (isBlank(ctrl.getControlId())) ctrl.setControlId(generateControlId());

        // CSMS origin 은 PENDING 으로 시작. CS_REPORT origin 은 ACTIVE 로 시작.
        if (isBlank(ctrl.getStatusCd())) {
            ctrl.setStatusCd(DerControl.ORIGIN_CSMS.equals(ctrl.getOriginCd())
                    ? DerControl.STATUS_PENDING : DerControl.STATUS_ACTIVE);
        }
        if (isBlank(ctrl.getIsSuperseded())) ctrl.setIsSuperseded(DerControl.NO);

        Date now = new Date();
        Writer w = ctrl.getWriter();
        if (w == null) {
            w = new Writer();
            ctrl.setWriter(w);
        }
        if (w.getRegistrationDate() == null) w.setRegistrationDate(now);
        if (w.getUpdateDate() == null)       w.setUpdateDate(now);
        String uid = isBlank(operId) ? "system" : operId;
        if (isBlank(w.getRegUserId())) w.setRegUserId(uid);
        if (isBlank(w.getUpdUserId())) w.setUpdUserId(w.getRegUserId());

        provider.registerDerControl(ctrl);
        recordHistory(ctrl.getControlId(), ctrl.getOriginCd(), DerControlHis.ACTION_REGISTER,
                null, ctrl.getStatusCd(),
                "origin=" + ctrl.getOriginCd() + ", type=" + ctrl.getControlType(), operId, now);
        return ctrl;
    }

    @Override
    public void acknowledgeSetPush(String controlId, String csStatus, String reason, String operId) {
        DerControl cur = provider.retrieveDerControl(controlId, DerControl.ORIGIN_CSMS);
        if (cur == null) {
            LOGGER.warn("[DER-ACK] no CSMS-origin row for controlId={}", controlId);
            return;
        }
        Date now = new Date();
        String newStatus;
        if ("Accepted".equalsIgnoreCase(csStatus)) {
            // 기존 ACTIVE 행 REPLACED 처리
            provider.modifyStatusForActive(cur.getCpId(), cur.getCsId(),
                    cur.getControlType(), cur.getIsDefault(),
                    DerControl.ORIGIN_CSMS, DerControl.STATUS_REPLACED, operId);
            newStatus = DerControl.STATUS_ACTIVE;
            provider.modifyStatus(controlId, DerControl.ORIGIN_CSMS, newStatus, now, null, operId);
        } else {
            newStatus = DerControl.STATUS_REJECTED;
            provider.modifyStatus(controlId, DerControl.ORIGIN_CSMS, newStatus, null, reason, operId);
        }
        recordHistory(controlId, DerControl.ORIGIN_CSMS, DerControlHis.ACTION_ACK,
                cur.getStatusCd(), newStatus, "csStatus=" + csStatus + ", reason=" + reason, operId, now);
    }

    @Override
    public void acknowledgeClear(String controlId, String csStatus, String reason, String operId) {
        DerControl cur = provider.retrieveDerControl(controlId, DerControl.ORIGIN_CSMS);
        if (cur == null) return;
        Date now = new Date();
        if ("Accepted".equalsIgnoreCase(csStatus)) {
            provider.modifyStatus(controlId, DerControl.ORIGIN_CSMS, DerControl.STATUS_CLEARED, now, null, operId);
            recordHistory(controlId, DerControl.ORIGIN_CSMS, DerControlHis.ACTION_CLEAR,
                    cur.getStatusCd(), DerControl.STATUS_CLEARED, "Clear accepted", operId, now);
        } else {
            recordHistory(controlId, DerControl.ORIGIN_CSMS, DerControlHis.ACTION_ACK,
                    cur.getStatusCd(), cur.getStatusCd(),
                    "Clear rejected: " + reason, operId, now);
        }
    }

    @Override
    public void deprecate(String controlId, String originCd, String newStatus, String reason, String operId) {
        DerControl cur = provider.retrieveDerControl(controlId, originCd);
        if (cur == null) throw new KEVITException("존재하지 않는 controlId: " + controlId);
        if (DerControl.STATUS_REPLACED.equals(cur.getStatusCd())
                || DerControl.STATUS_CLEARED.equals(cur.getStatusCd())
                || DerControl.STATUS_EXPIRED.equals(cur.getStatusCd())) {
            return;
        }
        Date now = new Date();
        provider.modifyStatus(controlId, originCd, newStatus, null, reason, operId);
        recordHistory(controlId, originCd,
                DerControl.STATUS_CLEARED.equals(newStatus) ? DerControlHis.ACTION_CLEAR : DerControlHis.ACTION_REPLACE,
                cur.getStatusCd(), newStatus, reason, operId, now);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void recordReport(String cpId, String csId, Map<String, Object> reportPayload, String operId) {
        if (reportPayload == null) return;
        Date now = new Date();
        for (Map.Entry<String, Object> entry : reportPayload.entrySet()) {
            if (!REPORT_KEYS.contains(entry.getKey())) continue;
            Object value = entry.getValue();
            if (!(value instanceof Map)) continue;
            Map<String, Object> sub = (Map<String, Object>) value;

            DerControl c = new DerControl();
            c.setOriginCd(DerControl.ORIGIN_CS_REPORT);
            c.setCpId(cpId);
            c.setCsId(csId);
            c.setControlType(deriveControlTypeFromKey(entry.getKey(), sub));
            c.setSubId(asString(sub.get("id")));
            c.setControlId(c.getSubId() != null ? c.getSubId() : generateControlId());
            c.setIsDefault(asYesNo(sub.get("isDefault"), DerControl.NO));
            c.setIsSuperseded(asYesNo(sub.get("isSuperseded"), DerControl.NO));
            c.setStatusCd(DerControl.STATUS_ACTIVE);

            Map<String, Object> inner = extractInnerSubObject(sub, entry.getKey());
            c.setPriority(asInteger(inner != null ? inner.get("priority") : sub.get("priority")));
            c.setParamJson(toJsonString(value));

            // 기존 ACTIVE CS_REPORT 행 REPLACED
            provider.modifyStatusForActive(cpId, csId, c.getControlType(), c.getIsDefault(),
                    DerControl.ORIGIN_CS_REPORT, DerControl.STATUS_REPLACED, operId);
            try {
                provider.registerDerControl(buildWithWriter(c, operId, now));
                recordHistory(c.getControlId(), DerControl.ORIGIN_CS_REPORT,
                        DerControlHis.ACTION_REPORT_IN, null, DerControl.STATUS_ACTIVE,
                        "ReportDERControl sub=" + entry.getKey(), operId, now);
            } catch (Exception ex) {
                LOGGER.warn("[DER-REPORT] register failed key={} subId={} err={}",
                        entry.getKey(), c.getSubId(), ex.getMessage());
            }
        }
    }

    @Override
    public void recordAlarm(DerAlarm alarm) {
        if (alarm.getReceivedDate() == null) alarm.setReceivedDate(new Date());
        if (alarm.getAlarmEnded() == null)   alarm.setAlarmEnded(DerControl.NO);
        alarmProvider.registerAlarm(alarm);
    }

    @Override
    public void recordStartStop(DerStartStop event) {
        if (event.getReceivedDate() == null) event.setReceivedDate(new Date());
        startStopProvider.registerStartStop(event);
    }

    @Transactional(readOnly = true)
    @Override
    public DerControl retrieveDerControl(String controlId, String originCd) {
        return provider.retrieveDerControl(controlId, originCd);
    }

    @Transactional(readOnly = true)
    @Override
    public DerControl retrieveActiveCsms(String cpId, String csId, String controlType, String isDefault) {
        return provider.retrieveActive(cpId, csId, controlType, isDefault, DerControl.ORIGIN_CSMS);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DerControlDto> retrieveDerControlBySearchCond(DerControlSearchCond cond) {
        return provider.retrieveDerControlBySearchCond(cond);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DerControlHisDto> retrieveHisBySearchCond(DerControlHisSearchCond cond) {
        return hisProvider.retrieveHisBySearchCond(cond);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DerAlarmDto> retrieveAlarmBySearchCond(DerAlarmSearchCond cond) {
        return alarmProvider.retrieveAlarmBySearchCond(cond);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DerStartStopDto> retrieveStartStopBySearchCond(DerStartStopSearchCond cond) {
        return startStopProvider.retrieveStartStopBySearchCond(cond);
    }

    @Override
    public void recordHistory(String controlId, String originCd, String actionCd,
                              String preStatus, String postStatus, String remark, String operId,
                              Date occurredDate) {
        DerControlHis his = new DerControlHis();
        his.setControlId(controlId);
        his.setOriginCd(originCd);
        his.setActionCd(actionCd);
        his.setPreStatusCd(preStatus);
        his.setPostStatusCd(postStatus);
        his.setRemark(remark);
        his.setOperId(operId);
        his.setOccurredDate(occurredDate != null ? occurredDate : new Date());
        hisProvider.registerHis(his);
    }

    // ===== helpers =====

    private DerControl buildWithWriter(DerControl c, String operId, Date now) {
        Writer w = new Writer();
        w.setRegistrationDate(now);
        w.setUpdateDate(now);
        String uid = isBlank(operId) ? "system" : operId;
        w.setRegUserId(uid);
        w.setUpdUserId(uid);
        c.setWriter(w);
        return c;
    }

    private String deriveControlTypeFromKey(String key, Map<String, Object> sub) {
        if ("curve".equals(key)) {
            // curve.curveType 에서 추출 (FreqWatt/VoltVar/...)
            Object t = sub.get("curveType");
            return t != null ? t.toString() : DerControl.CTRL_VOLT_WATT;
        }
        switch (key) {
            case "enterService":      return DerControl.CTRL_ENTER_SERVICE;
            case "freqDroop":         return DerControl.CTRL_FREQ_DROOP;
            case "fixedPFAbsorb":     return DerControl.CTRL_FIXED_PF_ABSORB;
            case "fixedPFInject":     return DerControl.CTRL_FIXED_PF_INJECT;
            case "fixedVar":          return DerControl.CTRL_FIXED_VAR;
            case "gradient":          return DerControl.CTRL_GRADIENTS;
            case "limitMaxDischarge": return DerControl.CTRL_LIMIT_MAX_DISCHARGE;
            default:                  return key;
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractInnerSubObject(Map<String, Object> sub, String key) {
        // ReportDERControl 의 구조: enterService.enterService.priority, freqDroop.freqDroop.priority 등 nested
        Object inner = sub.get(key);
        if (inner instanceof Map) return (Map<String, Object>) inner;
        // curve 는 curve.curve.priority 구조
        if ("curve".equals(key)) {
            Object cc = sub.get("curve");
            if (cc instanceof Map) return (Map<String, Object>) cc;
        }
        return null;
    }

    private String asString(Object o)   { return o == null ? null : o.toString(); }
    private Integer asInteger(Object o) {
        if (o == null) return null;
        if (o instanceof Number) return ((Number) o).intValue();
        try { return Integer.parseInt(o.toString()); } catch (Exception e) { return null; }
    }
    private String asYesNo(Object o, String def) {
        if (o == null) return def;
        if (o instanceof Boolean) return ((Boolean) o) ? DerControl.YES : DerControl.NO;
        String s = o.toString().trim().toLowerCase();
        return ("true".equals(s) || "y".equals(s)) ? DerControl.YES : DerControl.NO;
    }
    private String toJsonString(Object obj) {
        try {
            return new com.google.gson.Gson().toJson(obj);
        } catch (Exception e) {
            return String.valueOf(obj);
        }
    }
    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}

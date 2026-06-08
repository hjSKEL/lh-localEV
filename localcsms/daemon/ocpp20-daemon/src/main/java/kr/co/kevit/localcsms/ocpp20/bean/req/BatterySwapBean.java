/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import kr.co.kevit.localcsms.charger.entity.domain.SwapSlotStatus;
import kr.co.kevit.localcsms.charger.process.SwapSlotStatusService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.process.CustomerService;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecord;
import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecordDetail;
import kr.co.kevit.localcsms.recharger.process.BatterySwapRecordDetailService;
import kr.co.kevit.localcsms.recharger.process.BatterySwapRecordService;
import kr.co.kevit.ocpp201.domain.BatteryDataType;
import kr.co.kevit.ocpp201.domain.IdTokenType;

/**
 * OCPP 2.1 BatterySwap (CS→CSMS) 처리 Bean.
 *
 * 배터리 반납(BatteryIn) / 수령(BatteryOut) / 타임아웃(BatteryOutTimeout) 전문을
 * BatterySwapRecord (cycle 헤더 — requestId 단위) 및 BatterySwapRecordDetail
 * (슬롯별 — requestId + evseId) 에 기록한다.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Component("BatterySwap")
public class BatterySwapBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatterySwapBean.class);

    /** TB_RCBS001.STAT_CD (BSRS00) */
    private static final String STATUS_IN = "IN";
    private static final String STATUS_OUT = "OUT";
    private static final String STATUS_TIMEOUT = "Timeout";

    /** TB_RCBD001.TYPE_CD (BSDT00) */
    private static final String DETAIL_TYPE_IN = "IN";
    private static final String DETAIL_TYPE_OUT = "OUT";

    /** TB_BSSL001.SLOT_ST_CD (BSSS00) */
    private static final String SLOT_STATE_EMPTY = "EMPTY";
    private static final String SLOT_STATE_CHARGING = "CHARGING";
    private static final String SLOT_STATE_READY = "READY";

    /** SwapSlotStatus.lastEventType */
    private static final String EVT_BATTERY_IN = "BatteryIn";
    private static final String EVT_BATTERY_OUT = "BatteryOut";
    private static final String EVT_BATTERY_OUT_TIMEOUT = "BatteryOutTimeout";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SwapSlotStatusService swapSlotStatusService;

    @Autowired
    private BatterySwapRecordService recordService;

    @Autowired
    private BatterySwapRecordDetailService detailService;

    @Autowired(required = false)
    private CustomerService customerService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String text = msg.getPayload().toString();
        LOGGER.debug("BatterySwapBean Request cpCsId={} payload={}", cpCsId, text);

        kr.co.kevit.ocpp201.request.BatterySwap request = objectMapper.readValue(text,
                kr.co.kevit.ocpp201.request.BatterySwap.class);

        String[] ids = cpCsId.split(StringConstants.DASH, 2);
        String cpId = ids.length > 0 ? ids[0] : null;
        String csId = ids.length > 1 ? ids[1] : null;

        Long requestId = (long) request.getRequestId();
        Date now = new Date();
        String customerId = lookupCustomerId(request.getIdToken());

        switch (request.getEventType()) {
            case BatteryIn:
                handleBatteryIn(cpId, csId, requestId, customerId, request.getBatteryData(), now);
                break;
            case BatteryOut:
                handleBatteryOut(cpId, csId, requestId, customerId, request.getBatteryData(), now);
                break;
            case BatteryOutTimeout:
                handleBatteryOutTimeout(cpId, csId, requestId, now);
                break;
            default:
                LOGGER.warn("BatterySwapBean 알 수 없는 eventType={} requestId={}", request.getEventType(), requestId);
        }

        kr.co.kevit.ocpp201.response.BatterySwap response = new kr.co.kevit.ocpp201.response.BatterySwap();
        return objectMapper.valueToTree(response);
    }

    // ── BatteryIn — 반납 ─────────────────────────────────────────────────────

    private void handleBatteryIn(String cpId, String csId, Long requestId, String customerId,
            List<BatteryDataType> batteryData, Date now) {
        int count = batteryData == null ? 0 : batteryData.size();

        BatterySwapRecord record = new BatterySwapRecord();
        record.setRequestId(requestId);
        record.setCpId(cpId);
        record.setCsId(csId);
        record.setInCustomerId(customerId);
        record.setInCount(count);
        record.setInDateTime(now);
        record.setStatus(STATUS_IN);
        record.setWriter(systemWriter(now));
        recordService.registerBatterySwapRecord(record);

        registerDetails(requestId, batteryData, DETAIL_TYPE_IN, now);
        updateSlotsOnBatteryIn(cpId, csId, batteryData, now);
    }

    // ── BatteryOut — 수령 ────────────────────────────────────────────────────

    private void handleBatteryOut(String cpId, String csId, Long requestId, String customerId,
            List<BatteryDataType> batteryData, Date now) {
        int count = batteryData == null ? 0 : batteryData.size();

        BatterySwapRecord existing = recordService.retrieveBatterySwapRecord(requestId);
        if (existing != null) {
            existing.setOutCustomerId(customerId);
            existing.setOutCount(count);
            existing.setOutDateTime(now);
            existing.setStatus(STATUS_OUT);
            existing.setWriter(touchWriter(existing.getWriter(), now));
            recordService.modifyBatterySwapRecord(existing);
        } else {
            // BatteryIn 없이 BatteryOut 가 먼저 도착한 비정상 케이스 — Out 정보만 기록.
            LOGGER.warn("BatterySwapBean BatteryIn 없이 BatteryOut 수신 — requestId={}", requestId);
            BatterySwapRecord record = new BatterySwapRecord();
            record.setRequestId(requestId);
            record.setCpId(cpId);
            record.setCsId(csId);
            record.setOutCustomerId(customerId);
            record.setOutCount(count);
            record.setOutDateTime(now);
            record.setStatus(STATUS_OUT);
            record.setWriter(systemWriter(now));
            recordService.registerBatterySwapRecord(record);
        }

        registerDetails(requestId, batteryData, DETAIL_TYPE_OUT, now);
        updateSlotsOnBatteryOut(cpId, csId, batteryData, now);
    }

    // ── BatteryOutTimeout — 시간 초과 ────────────────────────────────────────

    private void handleBatteryOutTimeout(String cpId, String csId, Long requestId, Date now) {
        BatterySwapRecord existing = recordService.retrieveBatterySwapRecord(requestId);
        if (existing != null) {
            existing.setStatus(STATUS_TIMEOUT);
            existing.setWriter(touchWriter(existing.getWriter(), now));
            recordService.modifyBatterySwapRecord(existing);
            revertSlotsOnTimeout(cpId, csId, requestId, now);
            return;
        }
        LOGGER.warn("BatterySwapBean BatteryIn 없이 Timeout 수신 — requestId={}", requestId);
        BatterySwapRecord record = new BatterySwapRecord();
        record.setRequestId(requestId);
        record.setCpId(cpId);
        record.setCsId(csId);
        record.setStatus(STATUS_TIMEOUT);
        record.setWriter(systemWriter(now));
        recordService.registerBatterySwapRecord(record);
        revertSlotsOnTimeout(cpId, csId, requestId, now);
    }

    // ── 디테일 등록 ───────────────────────────────────────────────────────────

    private void registerDetails(Long requestId, List<BatteryDataType> batteryData, String type, Date now) {
        if (batteryData == null || batteryData.isEmpty())
            return;
        for (BatteryDataType data : batteryData) {
            BatterySwapRecordDetail detail = new BatterySwapRecordDetail();
            detail.setRequestId(requestId);
            detail.setEvseId(data.getEvseId());
            detail.setType(type);
            detail.setSoc(toBigDecimal(data.getSoC()));
            detail.setSoh(toBigDecimal(data.getSoH()));
            detail.setSerialNumber(data.getSerialNumber());
            detail.setProductionDate(parseProductionDate(data.getProductionDate()));
            detail.setWriter(systemWriter(now));
            detailService.registerBatterySwapRecordDetail(detail);
        }
    }

    // ── 슬롯 현재상태(SwapSlotStatus) 반영 ─────────────────────────────────────

    /**
     * BatteryIn(반납) — 슬롯에 배터리 적재 후 자체 충전 시작.
     * slotState → CHARGING, 배터리 스냅샷(시리얼/SoC/SoH/제조일) 갱신, 충전시작시각 기록.
     * 미등록 슬롯은 무시(warn). 슬롯 1건 실패가 전체 처리를 막지 않도록 개별 try/catch.
     */
    private void updateSlotsOnBatteryIn(String cpId, String csId, List<BatteryDataType> batteryData, Date now) {
        if (batteryData == null || batteryData.isEmpty())
            return;
        for (BatteryDataType data : batteryData) {
            SwapSlotStatus slot = swapSlotStatusService.retrieveSwapSlotStatus(cpId, csId, data.getEvseId());
            if (slot == null)
                continue;
            try {
                slot.setSlotState(SLOT_STATE_CHARGING);
                slot.setBatterySerialNo(data.getSerialNumber());
                slot.setCurrentSoC(toBigDecimal(data.getSoC()));
                slot.setCurrentSoH(toBigDecimal(data.getSoH()));
                slot.setProductionDate(parseProductionDate(data.getProductionDate()));
                slot.setChargingStartDate(now);
                slot.setLastEventType(EVT_BATTERY_IN);
                slot.setLastEventDate(now);
                slot.setWriter(touchWriter(slot.getWriter(), now));
                swapSlotStatusService.modifySwapSlotStatus(slot);
            } catch (Exception e) {
                LOGGER.warn("BatterySwapBean BatteryIn 슬롯상태 갱신 실패 — cpId={} csId={} evseId={} : {}",
                        cpId, csId, data.getEvseId(), e.getMessage());
            }
        }
    }

    /**
     * BatteryOut(수령) — 슬롯에서 배터리 반출.
     * slotState → EMPTY, 배터리 스냅샷/충전정보/예약 잠금 해제.
     */
    private void updateSlotsOnBatteryOut(String cpId, String csId, List<BatteryDataType> batteryData, Date now) {
        if (batteryData == null || batteryData.isEmpty())
            return;
        for (BatteryDataType data : batteryData) {
            SwapSlotStatus slot = swapSlotStatusService.retrieveSwapSlotStatus(cpId, csId, data.getEvseId());
            if (slot == null)
                continue;
            try {
                slot.setSlotState(SLOT_STATE_EMPTY);
                slot.setBatterySerialNo(null);
                slot.setCurrentSoC(null);
                slot.setCurrentSoH(null);
                slot.setProductionDate(null);
                slot.setChargingStartDate(null);
                slot.setEstimatedReadyDate(null);
                slot.setReservedRequestId(null);
                slot.setReservedUntil(null);
                slot.setLastEventType(EVT_BATTERY_OUT);
                slot.setLastEventDate(now);
                slot.setWriter(touchWriter(slot.getWriter(), now));
                swapSlotStatusService.modifySwapSlotStatus(slot);
            } catch (Exception e) {
                LOGGER.warn("BatterySwapBean BatteryOut 슬롯상태 갱신 실패 — cpId={} csId={} evseId={} : {}",
                        cpId, csId, data.getEvseId(), e.getMessage());
            }
        }
    }

    /**
     * BatteryOutTimeout — 시간 내 미수령. 반출 예정이던 슬롯을 출고대기(READY)로 복귀.
     * 어느 슬롯이었는지는 해당 requestId 의 OUT 디테일을 역추적해 식별하고,
     * 디테일에 기록된 배터리 스냅샷을 슬롯에 복원한다.
     */
    private void revertSlotsOnTimeout(String cpId, String csId, Long requestId, Date now) {
        List<BatterySwapRecordDetail> details = detailService.retrieveBatterySwapRecordDetailByRequestId(requestId);
        if (details == null || details.isEmpty()) {
            LOGGER.warn("BatterySwapBean Timeout 역추적할 OUT 디테일 없음 — requestId={}", requestId);
            return;
        }
        for (BatterySwapRecordDetail detail : details) {
            if (!DETAIL_TYPE_OUT.equals(detail.getType()))
                continue;
            SwapSlotStatus slot = swapSlotStatusService.retrieveSwapSlotStatus(cpId, csId, detail.getEvseId());
            if (slot == null)
                continue;
            try {
                slot.setSlotState(SLOT_STATE_READY);
                slot.setBatterySerialNo(detail.getSerialNumber());
                slot.setCurrentSoC(detail.getSoc());
                slot.setCurrentSoH(detail.getSoh());
                slot.setProductionDate(detail.getProductionDate());
                slot.setReservedRequestId(null);
                slot.setReservedUntil(null);
                slot.setLastEventType(EVT_BATTERY_OUT_TIMEOUT);
                slot.setLastEventDate(now);
                slot.setWriter(touchWriter(slot.getWriter(), now));
                swapSlotStatusService.modifySwapSlotStatus(slot);
            } catch (Exception e) {
                LOGGER.warn("BatterySwapBean Timeout 슬롯상태 복귀 실패 — cpId={} csId={} evseId={} : {}",
                        cpId, csId, detail.getEvseId(), e.getMessage());
            }
        }
    }

    // ── 헬퍼 ─────────────────────────────────────────────────────────────────

    /**
     * AuthorizeBean 의 idToken → customerId 매핑 규칙을 따른다.
     * - eMAID : CustomerCert.emaid → customerId
     * - NoAuthorization : 매핑 없음
     * - 그 외 카드/물리식별자 : 카드번호로 Customer 조회 → customerId
     */
    private String lookupCustomerId(IdTokenType idToken) {
        if (idToken == null || idToken.getType() == null)
            return null;

        String idTag = idToken.getIdToken();
        if (idTag == null || idTag.isEmpty())
            return null;

        switch (idToken.getType()) {
            case NoAuthorization:
                return null;
            case KeyCode:
            case ISO15693:
            case ISO14443:
            case Central:
            case Local:
            default: {
                if (customerService == null)
                    return null;
                Customer customer = customerService.retrieveCustomerByCustomerCardNo(idTag);
                return customer != null ? customer.getCustomerId() : null;
            }
        }
    }

    private BigDecimal toBigDecimal(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP);
    }

    private Date parseProductionDate(String s) {
        if (s == null || s.isEmpty())
            return null;
        Date d = DateUtils.stringToDate(s, DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC);
        if (d == null)
            d = DateUtils.stringToDate(s, DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS, DateUtils.UTC);
        if (d == null)
            d = DateUtils.stringToDate(s);
        return d;
    }

    private Writer systemWriter(Date now) {
        Writer w = new Writer(StringConstants.SYSTEM_EMPLOYEE);
        w.setRegistrationDate(now);
        w.setUpdateDate(now);
        return w;
    }

    private Writer touchWriter(Writer src, Date now) {
        Writer w = new Writer(StringConstants.SYSTEM_EMPLOYEE);
        if (src != null) {
            w.setRegistrationDate(src.getRegistrationDate());
            w.setRegUserId(src.getRegUserId());
        } else {
            w.setRegistrationDate(now);
        }
        w.setUpdateDate(now);
        return w;
    }
}

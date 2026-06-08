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
    }

    // ── BatteryOutTimeout — 시간 초과 ────────────────────────────────────────

    private void handleBatteryOutTimeout(String cpId, String csId, Long requestId, Date now) {
        BatterySwapRecord existing = recordService.retrieveBatterySwapRecord(requestId);
        if (existing != null) {
            existing.setStatus(STATUS_TIMEOUT);
            existing.setWriter(touchWriter(existing.getWriter(), now));
            recordService.modifyBatterySwapRecord(existing);
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

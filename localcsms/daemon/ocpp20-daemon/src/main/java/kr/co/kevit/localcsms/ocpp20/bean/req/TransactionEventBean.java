/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.smartcharging.process.SmartChargingService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.enumtype.charger.RechargingStatus;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.process.CustomerService;
import kr.co.kevit.localcsms.customer.process.CustomerVehicleService;
import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCard;
import kr.co.kevit.localcsms.payment.entity.domain.PspPayment;
import kr.co.kevit.localcsms.payment.process.PrepaidCardService;
import kr.co.kevit.localcsms.payment.process.PspPaymentService;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.process.ProductPriceService;
import kr.co.kevit.localcsms.recharger.entity.domain.Discharging;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.fee.FeeCalculator;
import kr.co.kevit.localcsms.recharger.process.DischargingService;
import kr.co.kevit.localcsms.recharger.process.RechargingService;
import kr.co.kevit.ocpp201.domain.IdTokenInfoType;
import kr.co.kevit.ocpp201.domain.IdTokenType;
import kr.co.kevit.ocpp201.domain.MeterValueType;
import kr.co.kevit.ocpp201.domain.SampledValueType;
import kr.co.kevit.ocpp201.domain.TransactionLimitType;
import kr.co.kevit.ocpp201.domain.UnitOfMeasureType;
import kr.co.kevit.ocpp201.enumtype.AuthorizationStatusEnumType;
import kr.co.kevit.ocpp201.enumtype.IdTokenEnumType;
import kr.co.kevit.ocpp201.enumtype.MeasurandEnumType;
import kr.co.kevit.ocpp201.enumtype.TriggerReasonEnumType;

/**
 *
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 6. 26.
 */
@Component("TransactionEvent")
public class TransactionEventBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionEventBean.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private ChargerStatusService chargerStatusService;

    @Autowired(required = false)
    private ChargingStationService chargingStationService;

    @Autowired(required = false)
    private CustomerService customerService;

    @Autowired(required = false)
    private RechargingService rechargingService;

    @Autowired(required = false)
    private DischargingService dischargingService;

    @Autowired(required = false)
    private CustomerVehicleService customerVehicleService;

    @Autowired(required = false)
    private ProductPriceService productPriceService;

    @Autowired(required = false)
    private PrepaidCardService prepaidCardService;

    @Autowired(required = false)
    private PspPaymentService pspPaymentService;

    @Autowired(required = false)
    private SmartChargingService smartChargingService;

    private final String EVT0E6 = "EVT0J7";
    private final String CHRS09 = "CHRS09";
    private final String EVT0J3 = "EVT0J3";

    /** 충전 서비스 방식(CSST00) - 배터리 교체형 */
    private static final String CS_SVC_TYPE_BATTERY_SWAP = "CSST02";

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String[] csIds = cpCsId.split(StringConstants.DASH);

        String text = msg.getPayload().toString();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TransactionEventBean.control : {}", text);
        }
        kr.co.kevit.ocpp201.request.TransactionEvent request = objectMapper.readValue(text,
                kr.co.kevit.ocpp201.request.TransactionEvent.class);

        // 충전 서비스 방식(CSST00) 분기: CSST01=커넥터 연결형(기존 로직), CSST02=배터리 교체형(별도 로직)
        ChargingStation station = chargingStationService.retrieveChargingStationByCpIdNCsId(csIds[0], csIds[1]);
        if (CS_SVC_TYPE_BATTERY_SWAP.equals(station.getCsServiceType())) {
            return controlBatterySwap(request, station);
        } else {
            return controlConnectedCharging(request, station);
        }
    }

    /**
     * 배터리 교체형 충전기(CSST02) TransactionEvent 처리 분기.
     * 커넥터 연결형(CSST01)과 흐름이 달라 별도 처리한다. (eventType 별 처리 골격)
     */
    private ObjectNode controlBatterySwap(kr.co.kevit.ocpp201.request.TransactionEvent request,
            ChargingStation station) {
        switch (request.getEventType()) {
            case Started:
                return startBatterySwap(request, station);
            case Ended:
                return endBatterySwap(request, station);
            case Updated:
                return updateBatterySwap(request, station);
        }
        return objectMapper.createObjectNode();
    }

    /**
     * 배터리 교체형(CSST02) Started 처리. TODO: 전용 로직 구현(예: idTokenInfo.status=Accepted 등
     * TC_S_103 대응).
     */
    private ObjectNode startBatterySwap(kr.co.kevit.ocpp201.request.TransactionEvent request, ChargingStation station) {
        // TODO: 배터리 교체형 전용 Started 처리 구현
        kr.co.kevit.ocpp201.response.TransactionEvent response = new kr.co.kevit.ocpp201.response.TransactionEvent();
        IdTokenInfoType idTokenInfo = new IdTokenInfoType();
        idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
        response.setIdTokenInfo(idTokenInfo);

        String authType = request.getIdToken() != null && request.getIdToken().getType() != null
                ? request.getIdToken().getType().name()
                : null;
        if (!IdTokenEnumType.NoAuthorization.name().equals(authType)) {
            idTokenInfo.setStatus(AuthorizationStatusEnumType.Invalid);
            return objectMapper.valueToTree(response);
        }

        // String custCardNo = StringConstants.TEMP_ID;
        return objectMapper.valueToTree(response);
    }

    /** 배터리 교체형(CSST02) Ended 처리. TODO: 전용 로직 구현. */
    private ObjectNode endBatterySwap(kr.co.kevit.ocpp201.request.TransactionEvent request, ChargingStation station) {
        // TODO: 배터리 교체형 전용 Ended 처리 구현
        kr.co.kevit.ocpp201.response.TransactionEvent response = new kr.co.kevit.ocpp201.response.TransactionEvent();
        IdTokenInfoType idTokenInfo = new IdTokenInfoType();
        idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
        response.setIdTokenInfo(idTokenInfo);

        String authType = request.getIdToken() != null && request.getIdToken().getType() != null
                ? request.getIdToken().getType().name()
                : null;
        if (!IdTokenEnumType.NoAuthorization.name().equals(authType)) {
            idTokenInfo.setStatus(AuthorizationStatusEnumType.Invalid);
            return objectMapper.valueToTree(response);
        }
        return objectMapper.valueToTree(response);
    }

    /** 배터리 교체형(CSST02) Updated 처리. TODO: 전용 로직 구현. */
    private ObjectNode updateBatterySwap(kr.co.kevit.ocpp201.request.TransactionEvent request,
            ChargingStation station) {
        // TODO: 배터리 교체형 전용 Updated 처리 구현
        kr.co.kevit.ocpp201.response.TransactionEvent response = new kr.co.kevit.ocpp201.response.TransactionEvent();
        return objectMapper.valueToTree(response);
    }

    /**
     * 
     */
    private ObjectNode controlConnectedCharging(kr.co.kevit.ocpp201.request.TransactionEvent request,
            ChargingStation station) {
        switch (request.getEventType()) {
            case Started:
                return start(request, station);
            case Ended:
                return end(request, station);
            case Updated:
                return update(request, station);
        }
        return objectMapper.createObjectNode();
    }

    private ObjectNode start(kr.co.kevit.ocpp201.request.TransactionEvent request, ChargingStation station) {
        //
        kr.co.kevit.ocpp201.response.TransactionEvent response = new kr.co.kevit.ocpp201.response.TransactionEvent();
        IdTokenInfoType idTokenInfo = new IdTokenInfoType();
        idTokenInfo.setLanguage1("ko-KR");
        idTokenInfo.setLanguage2("en-US");
        idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
        response.setIdTokenInfo(idTokenInfo);
        response.setChargingPriority(1);
        // E16.FR.16 — CS 가 local 비용 계산(costDetails 동봉) 중이면 totalCost 응답 금지
        if (request.getCostDetails() == null) {
            response.setTotalCost(0.0);
        }

        int evseId = request.getEvse() != null ? request.getEvse().getId() : 1;
        // 사용자인증 이벤트 저장
        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService
                .retrieveChargerStatusByCpIdNCsId(station.getCpId(), station.getCsId());
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.stream().filter(s -> s.getEvseId() == evseId)
                .findFirst().orElse(null);
        if (chargerStatusInfo == null) {
            return objectMapper.valueToTree(response);
        }

        String custCardNo = request.getIdToken() != null ? request.getIdToken().getIdToken()
                : chargerStatusInfo.getCutCardNo();
        String authType = request.getIdToken() != null && request.getIdToken().getType() != null
                ? request.getIdToken().getType().name()
                : null;

        if (StringUtils.isEmpty(custCardNo)) {
            custCardNo = StringConstants.TEMP_ID;
            response.setIdTokenInfo(null);
        }

        // DirectPayment (use case C18/C24) — PSP 결제 사전 인증된 건은 TB_PAPSP01 로 조회
        PspPayment pspPayment = null;
        if (IdTokenEnumType.DirectPayment.name().equals(authType) && pspPaymentService != null) {
            try {
                pspPayment = pspPaymentService.retrievePspPayment(custCardNo);
            } catch (Exception ex) {
                LOGGER.warn("PspPayment lookup failed for {}: {}", custCardNo, ex.getMessage());
            }
        }

        CustomerMgt customerMgt = getCustomerMgtByCardNo(custCardNo);
        boolean isDirectPayment = IdTokenEnumType.DirectPayment.name().equals(authType);
        if (customerMgt == null && pspPayment == null && !isDirectPayment
                && !IdTokenEnumType.NoAuthorization.name().equals(authType)) {
            idTokenInfo.setStatus(AuthorizationStatusEnumType.Invalid);
            idTokenInfo
                    .setCacheExpiryDateTime(DateUtils.dateToString(new Date(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
        } else {
            Date curDt = new Date();
            curDt = DateUtils.changeDateWithDayLevel(curDt, 7);
            idTokenInfo.setCacheExpiryDateTime(DateUtils.dateToString(curDt, DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
            if (authType != null && !IdTokenEnumType.NoAuthorization.name().equals(authType) && customerMgt != null) {
                IdTokenType groupIdToken = new IdTokenType();
                groupIdToken.setIdToken(customerMgt.getParentCardNo());
                groupIdToken.setType(IdTokenEnumType.valueOf(authType));
                idTokenInfo.setGroupIdToken(groupIdToken);
            }
            idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
        }

        // ISO14443(NFC) + 선불카드 활성/잔액>0 → 카드의 expireDate 를 응답의 cacheExpiryDateTime 으로 전달
        if (IdTokenEnumType.ISO14443.name().equals(authType)) {
            PrepaidCard prepaidCard = prepaidCardService.retrievePrepaidCard(custCardNo);
            if (prepaidCard != null) {
                idTokenInfo.setCacheExpiryDateTime(request.getTimestamp());
                TransactionLimitType transactionLimitType = new TransactionLimitType();
                transactionLimitType.setMaxCost(prepaidCard.getBalance().doubleValue());
                response.setTransactionLimit(transactionLimitType);
            }
        }

        String startTime = request.getTimestamp();
        double meterStart = getMeterValue(request.getMeterValue(), MeasurandEnumType.Energy_Active_Import_Register);

        // V2X 양방향(V2XT02) CS 만 방전 export 측 누적 추적
        boolean v2xBidirectional = station != null && "V2XT02".equals(station.getV2xType());
        double meterStartExport = v2xBidirectional
                ? getMeterValue(request.getMeterValue(), MeasurandEnumType.Energy_Active_Export_Register)
                : 0;
        String txId = request.getTransactionInfo().getTransactionId();
        String idTagType = resolveIdTagType(request);
        Recharging recharging = makeNewRecharging(chargerStatusInfo, customerMgt, txId, station);
        recharging.setIdTagType(idTagType);
        recharging.setStartCaEleEnerge(new BigDecimal(meterStart).divide(new BigDecimal(1000))); // 시작 시 전력량 (Wh -> kWh)
        recharging.setEndCaEleEnerge(BigDecimal.ZERO); // 시작 시 전력량
        // CS-set maxEnergy 가 요청에 포함되어 있으면 저장 (E16.FR.01)
        Double reqMaxEnergy = extractMaxEnergy(request);
        if (reqMaxEnergy != null) {
            recharging.setMaxEnergy(reqMaxEnergy);
        }
        // CS 가 보고하지 않은 한도(maxCost/maxEnergy/maxTime/maxSoC)는 CustomerMgt 회원 기본값으로 fallback
        applyCustomerMgtLimitFallback(recharging, customerMgt, request);
        applyTriggerReasonTime(recharging, request);
        rechargingService.registerRecharging(recharging);

        // V2X 양방향: 방전 거래 (TB_RCDC001) 동시 등록. DC_ID = txId (RC_ID 와 동일).
        if (v2xBidirectional && dischargingService != null) {
            Discharging discharging = makeNewDischarging(chargerStatusInfo, customerMgt, txId,
                    resolveEvccId(request), reqMaxEnergy);
            discharging.setIdTagType(idTagType);
            discharging.setStartDaEleEnerge(
                    BigDecimal.valueOf(meterStartExport).divide(BigDecimal.valueOf(1000)));
            discharging.setEndDaEleEnerge(BigDecimal.ZERO);
            dischargingService.registerDischarging(discharging);
        }

        // CSMS override/CustomerMgt 기본값 또는 CS-set 한도 echo (E16.FR.02 / E16.FR.07 / E16.FR.08)
        applyTransactionLimitToResponse(response, recharging, request);

        // DirectPayment: PSP 결제 마스터에 트랜잭션 연동 + maxCost / maxEnergy echo (C18/C24/C25)
        if (pspPayment != null) {
            try {
                pspPaymentService.linkTransaction(pspPayment.getPspRef(), recharging.getRechargingId(),
                        parseTimestamp(request.getTimestamp()), "ocpp20-daemon");
            } catch (Exception ex) {
                LOGGER.warn("PspPayment linkTransaction failed (pspRef={}, rcId={}): {}",
                        pspPayment.getPspRef(), recharging.getRechargingId(), ex.getMessage());
            }
            TransactionLimitType tlt = response.getTransactionLimit();
            if (pspPayment.getMaxCost() != null) {
                if (tlt == null)
                    tlt = new TransactionLimitType();
                tlt.setMaxCost(pspPayment.getMaxCost().doubleValue());
            }
            if (pspPayment.getMaxEnergy() != null) {
                if (tlt == null)
                    tlt = new TransactionLimitType();
                tlt.setMaxEnergy(pspPayment.getMaxEnergy().doubleValue());
                // Recharging.maxEnergy 에도 반영 (E16 정책 일관성, Updated/Ended 이벤트에서도 echo)
                recharging.setMaxEnergy(pspPayment.getMaxEnergy().doubleValue());
            }
            if (tlt != null) {
                response.setTransactionLimit(tlt);
            }
        }

        String rechargingId = recharging.getRechargingId();

        if (request.getTimestamp().contains(StringConstants.DOT)) {
            chargerStatusInfo.setInfoCollDate(
                    DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS));
        } else {
            chargerStatusInfo.setInfoCollDate(
                    DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
        }
        if (startTime.contains(StringConstants.DOT)) {
            chargerStatusInfo
                    .setChStartDate(DateUtils.stringToDate(startTime, DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS));
        } else {
            chargerStatusInfo.setChStartDate(DateUtils.stringToDate(startTime, DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
        }
        chargerStatusInfo.setCaEleEnerge(new BigDecimal(meterStart));
        chargerStatusInfo.setCuEleEnerge(BigDecimal.ZERO);
        // V2X 방전 누적 시작값 (V2XT01 이면 0)
        chargerStatusInfo.setDaEleEnerge(BigDecimal.valueOf(meterStartExport));
        chargerStatusInfo.setCuDaEleEnerge(BigDecimal.ZERO);
        chargerStatusInfo.setInstDchAmont(BigDecimal.ZERO);
        chargerStatusInfo.setInstDchCost(BigDecimal.ZERO);
        chargerStatusInfo.setInstDchSum(BigDecimal.ZERO);
        chargerStatusInfo.setDchSum(BigDecimal.ZERO);
        chargerStatusInfo.setCutCardNo(custCardNo);
        chargerStatusInfo.setRechargingId(rechargingId);
        chargerStatusInfo.setEventCode(EVT0J3);
        chargerStatusInfo.setInstChSum(BigDecimal.ZERO);// 순간충전금액
        chargerStatusInfo.setInstChAmont(BigDecimal.ZERO);// 순간 충전량
        chargerStatusInfo.setInstChCost(BigDecimal.ZERO);// 순간충전단가
        chargerStatusInfo.setChSum(BigDecimal.ZERO);// 충전금액
        chargerStatusInfo.setChEndDate(chargerStatusInfo.getChStartDate());// 충전종료시간
        chargerStatusInfo.setUpdateDate(new Date());
        chargerStatusService.modifyChargerStatus(chargerStatusInfo);

        return objectMapper.valueToTree(response);
    }

    /**
     * meterValue 리스트에서 지정한 measurand 의 sampledValue 를 찾아 Wh 단위로 반환.
     * 못 찾으면 0. measurand null 인 sampledValue 는 OCPP
     * 기본값(Energy.Active.Import.Register) 로 채운다.
     */
    private double getMeterValue(List<MeterValueType> meterValues, MeasurandEnumType target) {
        if (meterValues == null) {
            return 0;
        }
        for (MeterValueType meterValue : meterValues) {
            if (meterValue.getSampledValue() != null) {
                for (SampledValueType sampledValue : meterValue.getSampledValue()) {
                    if (sampledValue.getMeasurand() == null) {
                        sampledValue.setMeasurand(MeasurandEnumType.Energy_Active_Import_Register);
                    }
                    if (sampledValue.getMeasurand() == target) {
                        if (sampledValue.getUnitOfMeasure() == null
                                || sampledValue.getUnitOfMeasure().getUnit() == null) {
                            sampledValue.setUnitOfMeasure(new UnitOfMeasureType());
                        }
                        if (sampledValue.getUnitOfMeasure().getUnit().equals("Wh")) {
                            return sampledValue.getValue();
                        }
                        if (sampledValue.getUnitOfMeasure().getUnit().equals("kWh")) {
                            return sampledValue.getValue() * 1000;
                        }
                    }
                }
            }
        }
        return 0;
    }

    private Recharging makeNewRecharging(ChargerStatusInfo chargerStatusInfo, CustomerMgt customerMgt, String txId,
            ChargingStation station) {
        //
        String customerId = null;
        String cutCardNo = chargerStatusInfo.getCutCardNo();
        String companyId = null;

        if (customerMgt != null) {
            customerId = customerMgt.getCustomerId();
            cutCardNo = customerMgt.getCutCardNo();
            Customer customer = customerService.retrieveCustomerByUserId(customerMgt.getCustomerId());
            if (customer != null) {
                companyId = customer.getCompanyId();
            }
        }

        Date curDt = new Date();
        Recharging recharging = new Recharging();
        recharging.setRechargingId(txId);
        recharging.setChStatCode(RechargingStatus.RECS02.getCode());
        recharging.setCpId(chargerStatusInfo.getCpId());
        recharging.setCsId(chargerStatusInfo.getCsId());
        recharging.setEvseId(chargerStatusInfo.getEvseId());
        recharging.setChEndDate(null);
        recharging.setChStartDate(curDt);
        recharging.setChUseAmount(BigDecimal.ZERO);
        recharging.setChUseCost(BigDecimal.ZERO);
        recharging.setChUseUnitCost(BigDecimal.ZERO);
        recharging.setCustomerId(customerId);
        recharging.setCompanyId(companyId);
        recharging.setCutCardNo(cutCardNo);
        recharging.setFinalPaySum(0);
        recharging.setPaySum(0);
        recharging.setProductId(station.getProdType());
        return recharging;
    }

    /** 방전 거래 신규 생성. DC_ID = RC_ID = OCPP transactionId. */
    private Discharging makeNewDischarging(ChargerStatusInfo chargerStatusInfo, CustomerMgt customerMgt,
            String txId, String evccId, Double maxEnergy) {
        String customerId = null;
        String companyId = null;
        if (customerMgt != null) {
            customerId = customerMgt.getCustomerId();
            Customer customer = customerService.retrieveCustomerByUserId(customerMgt.getCustomerId());
            if (customer != null) {
                companyId = customer.getCompanyId();
            }
        }
        Discharging discharging = new Discharging();
        discharging.setDcId(txId);
        discharging.setCpId(chargerStatusInfo.getCpId());
        discharging.setCsId(chargerStatusInfo.getCsId());
        discharging.setEvseId(chargerStatusInfo.getEvseId());
        discharging.setCustomerId(customerId);
        discharging.setEvccId(evccId);
        discharging.setCompanyId(companyId);
        discharging.setDchStartDate(new Date());
        discharging.setDchStatCode("DCSS01");
        discharging.setDchUseAmount(BigDecimal.ZERO);
        discharging.setDchUseUnitCost(BigDecimal.ZERO);
        discharging.setDchUseCost(BigDecimal.ZERO);
        if (maxEnergy != null) {
            discharging.setMaxDischargeEnergy(maxEnergy);
        }
        return discharging;
    }

    /**
     * TransactionEvent 요청에서 EVCCID 추출.
     * OCPP 2.1 IdTokenEnumType.EVCCID 일 때만 idToken.idToken 이 차량 EVCCID.
     * eMAID 는 결제/계약 식별자이지 차량 ID 가 아니므로 별개로 처리한다.
     */
    private String resolveEvccId(kr.co.kevit.ocpp201.request.TransactionEvent request) {
        if (request.getIdToken() == null || request.getIdToken().getType() == null) {
            return null;
        }
        if (IdTokenEnumType.EVCCID.equals(request.getIdToken().getType())) {
            return request.getIdToken().getIdToken();
        }
        return null;
    }

    /** TransactionEvent.idToken.type 을 안전하게 name() 으로 변환. */
    private String resolveIdTagType(kr.co.kevit.ocpp201.request.TransactionEvent request) {
        if (request.getIdToken() == null || request.getIdToken().getType() == null) {
            return null;
        }
        return request.getIdToken().getType().name();
    }

    private ObjectNode update(kr.co.kevit.ocpp201.request.TransactionEvent request, ChargingStation station) {
        //
        int evseId = request.getEvse() != null ? request.getEvse().getId() : 1;
        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService
                .retrieveChargerStatusByCpIdNCsId(station.getCpId(), station.getCsId());
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.stream().filter(s -> s.getEvseId() == evseId)
                .findFirst().orElse(null);
        if (chargerStatusInfo == null) {
            return objectMapper.createObjectNode();
        }
        //
        String recharingId = request.getTransactionInfo().getTransactionId();

        Recharging recharging = rechargingService.retrieveRecharging4IfById(recharingId);

        if (recharging == null) {
            if (request.getIdToken() != null && request.getIdToken().getIdToken() != null) {
                chargerStatusInfo.setCutCardNo(request.getIdToken().getIdToken());
            } else {
                chargerStatusInfo.setCutCardNo(StringConstants.TEMP_ID);
            }
            recharging = makeNewRecharging(chargerStatusInfo, null, recharingId, station);
        }

        kr.co.kevit.ocpp201.response.TransactionEvent response = new kr.co.kevit.ocpp201.response.TransactionEvent();

        // Check Customer - derive auth type from request
        IdTokenType groupIdToken = new IdTokenType();
        String authType = request.getIdToken() != null && request.getIdToken().getType() != null
                ? request.getIdToken().getType().name()
                : null;

        if (IdTokenEnumType.eMAID.name().equals(authType)) {
            if (TriggerReasonEnumType.ChargingStateChanged.equals(request.getTriggerReason())) {
            } else if (TriggerReasonEnumType.Authorized.equals(request.getTriggerReason())) {
                IdTokenInfoType idTokenInfo = new IdTokenInfoType();
                idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
                response.setIdTokenInfo(idTokenInfo);
            }
            return objectMapper.valueToTree(response);
        }

        String custCardNo = chargerStatusInfo.getCutCardNo();
        if (request.getIdToken() != null && request.getIdToken().getType() != null) {
            custCardNo = request.getIdToken().getIdToken();
            authType = request.getIdToken().getType().name();
        }
        CustomerMgt customerMgt = null;
        if (custCardNo != null) {
            customerMgt = getCustomerMgtByCardNo(custCardNo);
        }

        if (custCardNo != null) {
            if (StringConstants.TEMP_ID.equals(recharging.getCutCardNo())) {
                if (customerMgt == null) {
                    IdTokenInfoType idTokenInfo = new IdTokenInfoType();
                    idTokenInfo.setStatus(AuthorizationStatusEnumType.Invalid);
                    response.setIdTokenInfo(idTokenInfo);
                    return objectMapper.valueToTree(response);
                }
                Customer customer = customerService.retrieveCustomerByUserId(customerMgt.getCustomerId());
                recharging.setCustomerId(customerMgt.getCustomerId());
                if (customer != null) {
                    recharging.setCompanyId(customer.getCompanyId());
                }
                recharging.setCutCardNo(customerMgt.getCutCardNo());
                chargerStatusInfo.setCutCardNo(custCardNo);
            }
            if (customerMgt != null && StringUtils.isNotEmpty(customerMgt.getParentCardNo())) {
                if (!IdTokenEnumType.NoAuthorization.name().equals(authType) && request.getIdToken() != null) {
                    groupIdToken.setIdToken(customerMgt.getParentCardNo());
                    if (authType != null) {
                        groupIdToken.setType(IdTokenEnumType.valueOf(authType));
                    }
                }
            }
        }
        // Started 시점엔 idToken 이 없어 카드 식별이 안 됐던 세션이 이번 이벤트에서 처음 식별된
        // 경우를 대비 — 아직 비어 있는 한도 필드만 CustomerMgt 기본값으로 채움 (fallback, idempotent)
        applyCustomerMgtLimitFallback(recharging, customerMgt, request);

        // 충전종료 이벤트 저장
        if (chargerStatusInfo.getChStartDate() == null) {
            chargerStatusInfo.setChStartDate(chargerStatusInfo.getInfoCollDate());
        }
        if (request.getTimestamp().contains(StringConstants.DOT)) {
            chargerStatusInfo.setInfoCollDate(
                    DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS));
        } else {
            chargerStatusInfo.setInfoCollDate(
                    DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
        }
        String endTime = request.getTimestamp();
        double meterEnd = getMeterValue(request.getMeterValue(), MeasurandEnumType.Energy_Active_Import_Register);

        // V2X 양방향(V2XT02) CS 만 방전 export 측 누적 추적
        boolean v2xBidirectional = station != null && "V2XT02".equals(station.getV2xType());
        double meterEndExport = v2xBidirectional
                ? getMeterValue(request.getMeterValue(), MeasurandEnumType.Energy_Active_Export_Register)
                : 0;

        recharging.setEndCaEleEnerge(new BigDecimal(meterEnd).divide(new BigDecimal(1000))); // 시작 시 전력량 (Wh -> kWh)
        recharging.setChUseAmount(recharging.getEndCaEleEnerge().subtract(recharging.getStartCaEleEnerge()));

        // 방전 누적 갱신 (V2X 양방향 만)
        Discharging discharging = v2xBidirectional && dischargingService != null
                ? dischargingService.retrieveDischargingById(recharging.getRechargingId())
                : null;
        BigDecimal dchAccum = BigDecimal.ZERO;
        if (discharging != null) {
            discharging.setEndDaEleEnerge(BigDecimal.valueOf(meterEndExport).divide(BigDecimal.valueOf(1000)));
            BigDecimal startDa = discharging.getStartDaEleEnerge() != null
                    ? discharging.getStartDaEleEnerge()
                    : BigDecimal.ZERO;
            dchAccum = discharging.getEndDaEleEnerge().subtract(startDa);
            discharging.setDchUseAmount(dchAccum);
        }

        BigDecimal fUseAmount = recharging.getChUseAmount();
        chargerStatusInfo.setInstChAmont(fUseAmount.subtract(chargerStatusInfo.getCuEleEnerge()));// 순간 충전량
        chargerStatusInfo.setCuEleEnerge(fUseAmount);// 충전사용전력량

        // 방전 순간량 / 누적 사용량 갱신
        BigDecimal prevDchCuEle = chargerStatusInfo.getCuDaEleEnerge() != null
                ? chargerStatusInfo.getCuDaEleEnerge()
                : BigDecimal.ZERO;
        chargerStatusInfo.setInstDchAmont(dchAccum.subtract(prevDchCuEle));
        chargerStatusInfo.setCuDaEleEnerge(dchAccum);

        if (chargerStatusInfo.getChStartDate() == null) {
            if (endTime.contains(StringConstants.DOT)) {
                chargerStatusInfo.setChStartDate(
                        DateUtils.stringToDate(endTime, DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS));
            } else {
                chargerStatusInfo
                        .setChStartDate(DateUtils.stringToDate(endTime, DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
            }
        }
        // 요금 계산 — 충전 / 방전 분리
        calculateFee(chargerStatusInfo, station);
        if (v2xBidirectional) {
            calculateDischargeFee(chargerStatusInfo, station);
        }

        chargerStatusInfo.setEventCode(StringConstants.BLANK);
        chargerStatusInfo.setCaEleEnerge(new BigDecimal(meterEnd));
        chargerStatusInfo.setDaEleEnerge(BigDecimal.valueOf(meterEndExport));
        chargerStatusInfo.setChSum(chargerStatusInfo.getChSum().add(chargerStatusInfo.getInstChSum()));// 충전요금
        chargerStatusInfo.setDchSum(chargerStatusInfo.getDchSum().add(chargerStatusInfo.getInstDchSum()));// 방전금액 누적
        chargerStatusInfo.setChEndDate(chargerStatusInfo.getInfoCollDate());
        chargerStatusInfo.setUpdateDate(new Date());
        chargerStatusService.modifyChargerStatus(chargerStatusInfo);

        // CS-set maxEnergy 가 요청에 포함되어 있으면 갱신 (E16.FR.01)
        Double reqMaxEnergy = extractMaxEnergy(request);
        if (reqMaxEnergy != null) {
            recharging.setMaxEnergy(reqMaxEnergy);
        }

        // 충전정보 변경.
        recharging.setChUseAmount(chargerStatusInfo.getCuEleEnerge());
        recharging.setChUseUnitCost(chargerStatusInfo.getInstChCost());
        recharging.setChUseCost(chargerStatusInfo.getChSum());
        recharging.setChStatCode(RechargingStatus.RECS02.getCode());
        // V2X Net-off 정책: paySum = max(0, chSum − dchSum)
        recharging.setPaySum(netOffPaySum(chargerStatusInfo));
        recharging.setChStartDate(chargerStatusInfo.getChStartDate());// 충전 시작 시간
        recharging.setChEndDate(chargerStatusInfo.getChEndDate());// 충전 종료 시간
        applyTriggerReasonTime(recharging, request);
        rechargingService.modifyRecharging(recharging);

        // 방전 거래 갱신
        if (discharging != null) {
            discharging.setDchUseUnitCost(chargerStatusInfo.getInstDchCost());
            discharging.setDchUseCost(chargerStatusInfo.getDchSum());
            discharging.setDchStatCode("DCSS02");
            discharging.setDchEndDate(chargerStatusInfo.getChEndDate());
            if (reqMaxEnergy != null) {
                discharging.setMaxDischargeEnergy(reqMaxEnergy);
            }
            dischargingService.modifyDischarging(discharging);
        }

        // CSMS override/CustomerMgt 기본값 또는 CS-set 한도 echo (E16.FR.02 / E16.FR.07 / E16.FR.08)
        applyTransactionLimitToResponse(response, recharging, request);

        if (request.getIdToken() != null) {
            IdTokenInfoType idTokenInfo = new IdTokenInfoType();
            idTokenInfo.setLanguage1("ko-KR");
            idTokenInfo.setLanguage2("en-US");
            idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
            if (groupIdToken.getIdToken() != null && !IdTokenEnumType.NoAuthorization.name().equals(authType)) {
                idTokenInfo.setGroupIdToken(groupIdToken);
            }
            response.setIdTokenInfo(idTokenInfo);
        }
        response.setChargingPriority(1);
        // E16.FR.16 — CS 가 local 비용 계산(costDetails 동봉) 중이면 totalCost 응답 금지
        if (request.getCostDetails() == null) {
            response.setTotalCost(recharging.getPaySum() != null ? recharging.getPaySum().doubleValue() : 0.0);
        }

        return objectMapper.valueToTree(response);
    }

    private ObjectNode end(kr.co.kevit.ocpp201.request.TransactionEvent request, ChargingStation station) {
        //
        int evseId = request.getEvse() != null ? request.getEvse().getId() : 1;
        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService
                .retrieveChargerStatusByCpIdNCsId(station.getCpId(), station.getCsId());
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.stream().filter(s -> s.getEvseId() == evseId)
                .findFirst().orElse(null);
        if (chargerStatusInfo == null) {
            return objectMapper.createObjectNode();
        }

        String recharingId = request.getTransactionInfo().getTransactionId();
        Recharging recharging = rechargingService.retrieveRecharging4IfById(recharingId);
        // 트랜잭션 종료 → 해당 TxProfile 폐기 (K01 lifecycle)
        if (smartChargingService != null) {
            try {
                smartChargingService.clearTxProfiles(recharingId);
            } catch (Exception ex) {
                LOGGER.warn("clearTxProfiles 실패 transactionId={}: {}", recharingId, ex.getMessage());
            }
        }
        // 충전종료 이벤트 저장
        if (chargerStatusInfo.getChStartDate() == null) {
            chargerStatusInfo.setChStartDate(chargerStatusInfo.getInfoCollDate());
        }
        if (request.getTimestamp().contains(StringConstants.DOT)) {
            chargerStatusInfo.setInfoCollDate(
                    DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS));
        } else {
            chargerStatusInfo.setInfoCollDate(
                    DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
        }

        // derive authType from request before clearing
        String authType = request.getIdToken() != null && request.getIdToken().getType() != null
                ? request.getIdToken().getType().name()
                : null;
        chargerStatusInfo.setRechargingId(null);
        chargerStatusInfo.setEventCode(EVT0E6);
        chargerStatusInfo.setCutCardNo(null);
        chargerStatusInfo.setCsStatCode(CHRS09);

        String endTime = request.getTimestamp();
        double meterEnd = 0;
        double meterEndExport = 0;
        boolean v2xBidirectional = station != null && "V2XT02".equals(station.getV2xType());
        if (request.getMeterValue() != null && request.getMeterValue().size() != 0) {
            meterEnd = getMeterValue(request.getMeterValue(), MeasurandEnumType.Energy_Active_Import_Register);
            if (v2xBidirectional) {
                meterEndExport = getMeterValue(request.getMeterValue(),
                        MeasurandEnumType.Energy_Active_Export_Register);
            }
        }

        recharging.setEndCaEleEnerge(new BigDecimal(meterEnd).divide(new BigDecimal(1000))); // 시작 시 전력량 (Wh -> kWh)
        recharging.setChUseAmount(recharging.getEndCaEleEnerge().subtract(recharging.getStartCaEleEnerge()));

        // 방전 거래 종료 처리 (V2X 양방향 만)
        Discharging discharging = v2xBidirectional && dischargingService != null
                ? dischargingService.retrieveDischargingById(recharingId)
                : null;
        BigDecimal dchAccum = BigDecimal.ZERO;
        if (discharging != null) {
            discharging.setEndDaEleEnerge(BigDecimal.valueOf(meterEndExport).divide(BigDecimal.valueOf(1000)));
            BigDecimal startDa = discharging.getStartDaEleEnerge() != null
                    ? discharging.getStartDaEleEnerge()
                    : BigDecimal.ZERO;
            dchAccum = discharging.getEndDaEleEnerge().subtract(startDa);
            discharging.setDchUseAmount(dchAccum);
        }

        BigDecimal fUseAmount = recharging.getChUseAmount();
        chargerStatusInfo.setInstChAmont(fUseAmount.subtract(chargerStatusInfo.getCuEleEnerge()));// 순간 충전량
        chargerStatusInfo.setCuEleEnerge(fUseAmount);// 충전사용전력량

        BigDecimal prevDchCuEle = chargerStatusInfo.getCuDaEleEnerge() != null
                ? chargerStatusInfo.getCuDaEleEnerge()
                : BigDecimal.ZERO;
        chargerStatusInfo.setInstDchAmont(dchAccum.subtract(prevDchCuEle));
        chargerStatusInfo.setCuDaEleEnerge(dchAccum);

        if (chargerStatusInfo.getChStartDate() == null) {
            if (endTime.contains(StringConstants.DOT)) {
                chargerStatusInfo.setChStartDate(
                        DateUtils.stringToDate(endTime, DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS));
            } else {
                chargerStatusInfo
                        .setChStartDate(DateUtils.stringToDate(endTime, DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
            }
        }
        // 요금 계산 — 충전 / 방전 분리
        calculateFee(chargerStatusInfo, station);
        if (v2xBidirectional) {
            calculateDischargeFee(chargerStatusInfo, station);
        }

        chargerStatusInfo.setCaEleEnerge(new BigDecimal(meterEnd));
        chargerStatusInfo.setDaEleEnerge(BigDecimal.valueOf(meterEndExport));
        chargerStatusInfo.setChSum(chargerStatusInfo.getChSum().add(chargerStatusInfo.getInstChSum()));// 충전요금
        chargerStatusInfo.setDchSum(chargerStatusInfo.getDchSum().add(chargerStatusInfo.getInstDchSum()));// 방전금액 누적
        chargerStatusInfo.setChEndDate(chargerStatusInfo.getInfoCollDate());
        chargerStatusInfo.setUpdateDate(new Date());
        chargerStatusService.modifyChargerStatus(chargerStatusInfo);

        // 충전정보 변경.
        recharging.setChUseAmount(chargerStatusInfo.getCuEleEnerge());
        recharging.setChUseUnitCost(chargerStatusInfo.getInstChCost());
        recharging
                .setChUseCost(chargerStatusInfo.getChSum().compareTo(BigDecimal.ZERO) > 0 ? chargerStatusInfo.getChSum()
                        : BigDecimal.ONE);
        recharging.setChStatCode(RechargingStatus.RECS03.getCode());
        // V2X Net-off 정책: paySum = max(0, chSum − dchSum)
        recharging.setPaySum(netOffPaySum(chargerStatusInfo));
        recharging.setChStartDate(chargerStatusInfo.getChStartDate());// 충전 시작 시간
        recharging.setChEndDate(chargerStatusInfo.getChEndDate());// 충전 종료 시간
        applyTriggerReasonTime(recharging, request);
        rechargingService.modifyRecharging(recharging);

        // 방전 거래 종료 + 누적 보상금 적립 (정책: 누적 적립)
        if (discharging != null) {
            discharging.setDchUseUnitCost(chargerStatusInfo.getInstDchCost());
            discharging.setDchUseCost(chargerStatusInfo.getDchSum());
            discharging.setDchStatCode("DCSS03");
            discharging.setDchEndDate(chargerStatusInfo.getChEndDate());
            dischargingService.modifyDischarging(discharging);

            // 누적 보상 적립 — idTagType == EVCCID 이고 evccId/dchUseCost 가 유효할 때만.
            // eMAID 인증 + EVCCID 미동봉 케이스는 차량 미식별이므로 스킵.
            boolean evccIdAuth = IdTokenEnumType.EVCCID.name().equals(discharging.getIdTagType());
            if (customerVehicleService != null
                    && evccIdAuth
                    && discharging.getEvccId() != null && !discharging.getEvccId().isEmpty()
                    && discharging.getDchUseCost() != null
                    && discharging.getDchUseCost().compareTo(BigDecimal.ZERO) > 0) {
                try {
                    int affected = customerVehicleService.accumulateReward(discharging.getEvccId(),
                            discharging.getDchUseCost(), "ocpp20-daemon");
                    if (affected == 0) {
                        LOGGER.warn("V2G reward skipped — EVCCID 미등록 차량 (evccId={}, dchSum={})",
                                discharging.getEvccId(), discharging.getDchUseCost());
                    }
                } catch (Exception ex) {
                    LOGGER.warn("V2G reward accumulation failed (evccId={}, dchSum={}): {}",
                            discharging.getEvccId(), discharging.getDchUseCost(), ex.getMessage());
                }
            } else if (discharging.getEvccId() == null) {
                LOGGER.info("V2G reward skipped — EVCCID 미동봉 (idTagType={}, txId={})",
                        discharging.getIdTagType(), discharging.getDcId());
            }
        }

        kr.co.kevit.ocpp201.response.TransactionEvent response = new kr.co.kevit.ocpp201.response.TransactionEvent();
        IdTokenInfoType idTokenInfo = new IdTokenInfoType();
        idTokenInfo.setLanguage1("ko-KR");
        idTokenInfo.setLanguage2("en-US");
        idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
        response.setIdTokenInfo(idTokenInfo);
        response.setChargingPriority(1);
        // E16.FR.16 — CS 가 local 비용 계산(costDetails 동봉) 중이면 totalCost 응답 금지
        if (request.getCostDetails() == null) {
            response.setTotalCost(recharging.getPaySum() != null ? recharging.getPaySum().doubleValue() : 0.0);
        }
        // 카드 식별이 이번(Ended) 이벤트에서 처음 이뤄진 극히 드문 경우까지 대비 — 비어 있는 필드만 채움
        if (recharging.getCutCardNo() != null) {
            applyCustomerMgtLimitFallback(recharging, getCustomerMgtByCardNo(recharging.getCutCardNo()), request);
        }
        // CSMS override/CustomerMgt 기본값 또는 CS-set 한도 echo (E16.FR.02 / E16.FR.07 / E16.FR.08)
        applyTransactionLimitToResponse(response, recharging, request);

        if (!StringConstants.GUEST0_ID.equals(recharging.getCutCardNo())
                && !StringConstants.GUEST1_ID.equals(recharging.getCutCardNo())
                && !StringConstants.FREEGUEST_ID.equals(recharging.getCutCardNo())
                && !IdTokenEnumType.NoAuthorization.name().equals(authType)) {
            Customer customer = customerService.retrieveCustomerByCustomerCardNo(recharging.getCutCardNo());
            if (customer == null) {
                idTokenInfo.setStatus(AuthorizationStatusEnumType.Invalid);
                idTokenInfo.setCacheExpiryDateTime(
                        DateUtils.dateToString(new Date(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
                return objectMapper.valueToTree(response);
            }
        }
        // 사용자의 그룹 ID(부모 카드 = 그룹) 가 존재하면 응답 idTokenInfo 에 채운다.
        if (!IdTokenEnumType.NoAuthorization.name().equals(authType) && recharging.getCutCardNo() != null) {
            CustomerMgt customerMgt = getCustomerMgtByCardNo(recharging.getCutCardNo());
            if (customerMgt != null && StringUtils.isNotEmpty(customerMgt.getParentCardNo())) {
                // Ended 이벤트엔 idToken 이 없을 수 있어 authType 이 null 이다.
                // 이 경우 거래 시작 시 저장한 idTagType(인증 토큰 종류)을 사용한다. (IdTokenType.type 은 필수)
                String groupType = authType != null ? authType : recharging.getIdTagType();
                if (groupType != null && !IdTokenEnumType.NoAuthorization.name().equals(groupType)) {
                    IdTokenType groupIdToken = new IdTokenType();
                    groupIdToken.setIdToken(customerMgt.getParentCardNo());
                    groupIdToken.setType(IdTokenEnumType.valueOf(groupType));
                    idTokenInfo.setGroupIdToken(groupIdToken);
                }
            }
        }

        Date curDate = DateUtils.changeDateWithDayLevel(new Date(), 7);
        idTokenInfo.setCacheExpiryDateTime(DateUtils.dateToString(curDate, DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
        idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
        return objectMapper.valueToTree(response);
    }

    /**
     * Helper: get CustomerMgt by card number (2-step chain)
     */
    private CustomerMgt getCustomerMgtByCardNo(String cardNo) {
        Customer customer = customerService.retrieveCustomerByCustomerCardNo(cardNo);
        if (customer != null) {
            return customerService.retrieveCustomerMgtByCustomerId(customer.getCustomerId());
        }
        return null;
    }

    /**
     * triggerReason에 따라 주차/출차/케이블 시간 설정
     */
    private void applyTriggerReasonTime(Recharging recharging, kr.co.kevit.ocpp201.request.TransactionEvent request) {
        if (request.getTriggerReason() == null)
            return;
        Date eventTime = parseTimestamp(request.getTimestamp());
        switch (request.getTriggerReason()) {
            case EVDetected:
                recharging.setPkStartDate(eventTime);
                break;
            case EVDeparted:
                recharging.setPkEndDate(eventTime);
                recharging.setCableEndDate(eventTime);
                break;
            case CablePluggedIn:
                recharging.setCableStartDate(eventTime);
                break;
            default:
                break;
        }
    }

    private Date parseTimestamp(String timestamp) {
        if (timestamp == null)
            return new Date();
        if (timestamp.contains(StringConstants.DOT)) {
            return DateUtils.stringToDate(timestamp, DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS);
        }
        return DateUtils.stringToDate(timestamp, DateUtils.RFC3339_DEFAULT_DATE_FORMAT);
    }

    /**
     * Helper: calculate fee and set on chargerStatusInfo
     */
    private void calculateFee(ChargerStatusInfo chargerStatusInfo, ChargingStation station) {
        try {
            ProductPrice productPrice = productPriceService.retrieveLiveProductPriceByType(station.getProdType(),
                    new Date());
            if (productPrice != null) {
                Map<String, BigDecimal> priceMap = FeeCalculator.getInstance().calculate(productPrice,
                        chargerStatusInfo.getInstChAmont());
                chargerStatusInfo.setInstChCost(priceMap.get(StringConstants.UNIT_PRICE));// 순간충전단가
                chargerStatusInfo.setInstChSum(priceMap.get(StringConstants.PRICE));// 순간충전금액
                return;
            }
        } catch (Exception e) {
            LOGGER.warn("Fee calculation failed: {}", e.getMessage());
        }
        chargerStatusInfo.setInstChCost(BigDecimal.ZERO);
        chargerStatusInfo.setInstChSum(BigDecimal.ZERO);
    }

    /**
     * Net-off 정책 결제 금액 계산: paySum = max(0, floor(chSum − dchSum)).
     *
     * <p>
     * 방전 보상금({@code dchSum})이 충전 비용({@code chSum})을 상쇄. 음수는 0 으로 절삭한다.
     * V2XT01 (충전만) CS 는 dchSum=0 이므로 기존 paySum=chSum 동작과 동일.
     * </p>
     */
    private int netOffPaySum(ChargerStatusInfo chargerStatusInfo) {
        BigDecimal chSum = chargerStatusInfo.getChSum() != null ? chargerStatusInfo.getChSum() : BigDecimal.ZERO;
        BigDecimal dchSum = chargerStatusInfo.getDchSum() != null ? chargerStatusInfo.getDchSum() : BigDecimal.ZERO;
        int net = chSum.subtract(dchSum).setScale(0, RoundingMode.FLOOR).intValue();
        return Math.max(0, net);
    }

    /**
     * Helper: V2X 방전 단가 기반 순간 방전 금액 산정. {@link ProductPrice#getDischargeFee()} 가 0
     * 이면 0 반환.
     */
    private void calculateDischargeFee(ChargerStatusInfo chargerStatusInfo, ChargingStation station) {
        try {
            ProductPrice productPrice = productPriceService.retrieveLiveProductPriceByType(station.getProdType(),
                    new Date());
            if (productPrice != null) {
                BigDecimal instDch = chargerStatusInfo.getInstDchAmont() != null
                        ? chargerStatusInfo.getInstDchAmont()
                        : BigDecimal.ZERO;
                Map<String, BigDecimal> priceMap = FeeCalculator.getInstance().calculateDischarge(productPrice,
                        instDch);
                chargerStatusInfo.setInstDchCost(priceMap.get(StringConstants.UNIT_PRICE));
                chargerStatusInfo.setInstDchSum(priceMap.get(StringConstants.PRICE));
                return;
            }
        } catch (Exception e) {
            LOGGER.warn("Discharge fee calculation failed: {}", e.getMessage());
        }
        chargerStatusInfo.setInstDchCost(BigDecimal.ZERO);
        chargerStatusInfo.setInstDchSum(BigDecimal.ZERO);
    }

    /**
     * 요청 페이로드의 {@code transactionInfo.transactionLimit.maxEnergy} 추출. 없으면 null.
     */
    private Double extractMaxEnergy(kr.co.kevit.ocpp201.request.TransactionEvent request) {
        if (request == null || request.getTransactionInfo() == null)
            return null;
        TransactionLimitType limit = request.getTransactionInfo().getTransactionLimit();
        return limit == null ? null : limit.getMaxEnergy();
    }

    /**
     * 요청 페이로드의 {@code transactionInfo.transactionLimit} 전체 추출. 없으면 null.
     */
    private TransactionLimitType extractRequestedLimit(kr.co.kevit.ocpp201.request.TransactionEvent request) {
        if (request == null || request.getTransactionInfo() == null)
            return null;
        return request.getTransactionInfo().getTransactionLimit();
    }

    /**
     * CustomerMgt(회원 기본 충전 한도) → Recharging fallback.
     *
     * <p>
     * 우선순위: CS 자체 보고값(request.transactionInfo.transactionLimit) &gt; CustomerMgt 회원
     * 기본값. CS 가 이번 요청에 해당 필드를 직접 보고했으면 그 필드는 건드리지 않고 CS 설정을 그대로
     * 따르며, 보고하지 않았을 때만 CustomerMgt 값을 recharging 에 채워 세션 내내 유지·echo 한다.
     * </p>
     * <p>
     * Started 시점엔 idToken 이 아직 없을 수 있다(EV 케이블만 먼저 연결되고 인증은 이후 Updated
     * 이벤트에서 이뤄지는 흐름 — CablePluggedIn → Authorize → ChargingStateChanged). 그래서 이
     * 메서드는 Started/Updated/Ended 어디서 호출되든 안전하도록, recharging 에 이미 값이 채워져
     * 있는 필드(= 이전 이벤트에서 이미 확정됐거나 PSP 등이 override 한 값)는 절대 덮어쓰지 않고
     * 비어 있는 필드만 채운다. 즉 카드 식별이 어느 이벤트에서 처음 이뤄지든 그 시점에 한 번만 적용된다.
     * </p>
     */
    private void applyCustomerMgtLimitFallback(Recharging recharging, CustomerMgt customerMgt,
            kr.co.kevit.ocpp201.request.TransactionEvent request) {
        if (recharging == null || customerMgt == null)
            return;
        TransactionLimitType reqLimit = extractRequestedLimit(request);
        if ((recharging.getMaxCost() == null || recharging.getMaxCost() <= 0)
                && (reqLimit == null || reqLimit.getMaxCost() == null)
                && customerMgt.getMaxCost() != null && customerMgt.getMaxCost() > 0) {
            recharging.setMaxCost(customerMgt.getMaxCost());
        }
        if ((recharging.getMaxEnergy() == null || recharging.getMaxEnergy() <= 0)
                && (reqLimit == null || reqLimit.getMaxEnergy() == null)
                && customerMgt.getMaxEnergy() != null && customerMgt.getMaxEnergy() > 0) {
            recharging.setMaxEnergy(customerMgt.getMaxEnergy());
        }
        if ((recharging.getMaxTime() == null || recharging.getMaxTime() <= 0)
                && (reqLimit == null || reqLimit.getMaxTime() == null)
                && customerMgt.getMaxTime() != null && customerMgt.getMaxTime() > 0) {
            recharging.setMaxTime(customerMgt.getMaxTime());
        }
        if ((recharging.getMaxSoC() == null || recharging.getMaxSoC() <= 0)
                && (reqLimit == null || reqLimit.getMaxSoC() == null)
                && customerMgt.getMaxSoC() != null && customerMgt.getMaxSoC() > 0) {
            recharging.setMaxSoC(customerMgt.getMaxSoC());
        }
    }

    /**
     * Recharging 에 저장된 한도(maxCost/maxEnergy/maxTime/maxSoC)를 응답의 transactionLimit 에 echo.
     *
     * <ul>
     * <li>maxEnergy — 기존 정책 유지: Recharging.maxEnergy 가 0 보다 크면 동봉하되, E16.FR.08(CS
     * 보고값이 이미 저장값 이하이면 echo 금지)을 적용한다.</li>
     * <li>maxCost/maxTime/maxSoC — 이번 이벤트에서 CS 가 직접 보고한 필드는 건드리지 않고(CS 설정
     * 유지), 응답에 이미 값이 채워져 있으면(예: 선불카드 잔액, PSP 결제 한도) 덮어쓰지 않는다.</li>
     * </ul>
     */
    private void applyTransactionLimitToResponse(kr.co.kevit.ocpp201.response.TransactionEvent response,
            Recharging recharging,
            kr.co.kevit.ocpp201.request.TransactionEvent request) {
        if (response == null || recharging == null)
            return;
        TransactionLimitType reqLimit = extractRequestedLimit(request);
        TransactionLimitType tlt = response.getTransactionLimit();

        // maxEnergy : 기존 E16.FR.02/07/08 정책 그대로
        Double maxE = recharging.getMaxEnergy();
        if (maxE != null && maxE > 0) {
            Double reqE = reqLimit != null ? reqLimit.getMaxEnergy() : null;
            if (reqE == null || reqE > maxE) {
                if (tlt == null)
                    tlt = new TransactionLimitType();
                tlt.setMaxEnergy(maxE);
            }
        }
        // maxCost / maxTime / maxSoC : CS 미보고 + 응답에 아직 값이 없을 때만 echo
        if (recharging.getMaxCost() != null && recharging.getMaxCost() > 0
                && (reqLimit == null || reqLimit.getMaxCost() == null)) {
            if (tlt == null)
                tlt = new TransactionLimitType();
            if (tlt.getMaxCost() == null)
                tlt.setMaxCost(recharging.getMaxCost());
        }
        if (recharging.getMaxTime() != null && recharging.getMaxTime() > 0
                && (reqLimit == null || reqLimit.getMaxTime() == null)) {
            if (tlt == null)
                tlt = new TransactionLimitType();
            if (tlt.getMaxTime() == null)
                tlt.setMaxTime(recharging.getMaxTime());
        }
        if (recharging.getMaxSoC() != null && recharging.getMaxSoC() > 0
                && (reqLimit == null || reqLimit.getMaxSoC() == null)) {
            if (tlt == null)
                tlt = new TransactionLimitType();
            if (tlt.getMaxSoC() == null)
                tlt.setMaxSoC(recharging.getMaxSoC());
        }
        if (tlt != null) {
            response.setTransactionLimit(tlt);
        }
    }
}

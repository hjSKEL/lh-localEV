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
import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCard;
import kr.co.kevit.localcsms.payment.entity.domain.PspPayment;
import kr.co.kevit.localcsms.payment.process.PrepaidCardService;
import kr.co.kevit.localcsms.payment.process.PspPaymentService;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.process.ProductPriceService;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.fee.FeeCalculator;
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

        switch (request.getEventType()) {
            case Started:
                return start(request, csIds);
            case Ended:
                return end(request, csIds);
            case Updated:
                return update(request, csIds);
        }
        return objectMapper.createObjectNode();
    }

    private ObjectNode start(kr.co.kevit.ocpp201.request.TransactionEvent request, String[] csIds) {
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
        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0],
                csIds[1]);
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
        double meterStart = getMeterValue(request.getMeterValue());

        ChargingStation station = chargingStationService.retrieveChargingStationByCpIdNCsId(chargerStatusInfo.getCpId(),
                chargerStatusInfo.getCsId());
        Recharging recharging = makeNewRecharging(chargerStatusInfo, customerMgt,
                request.getTransactionInfo().getTransactionId(), station);
        recharging.setStartCaEleEnerge(new BigDecimal(meterStart).divide(new BigDecimal(1000))); // 시작 시 전력량 (Wh -> kWh)
        recharging.setEndCaEleEnerge(BigDecimal.ZERO); // 시작 시 전력량
        // CS-set maxEnergy 가 요청에 포함되어 있으면 저장 (E16.FR.01)
        Double reqMaxEnergy = extractMaxEnergy(request);
        if (reqMaxEnergy != null) {
            recharging.setMaxEnergy(reqMaxEnergy);
        }
        applyTriggerReasonTime(recharging, request);
        rechargingService.registerRecharging(recharging);

        // CSMS override 또는 CS-set maxEnergy echo (E16.FR.02 / E16.FR.07 / E16.FR.08)
        applyMaxEnergyToResponse(response, recharging, request);

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
                if (tlt == null) tlt = new TransactionLimitType();
                tlt.setMaxCost(pspPayment.getMaxCost().doubleValue());
            }
            if (pspPayment.getMaxEnergy() != null) {
                if (tlt == null) tlt = new TransactionLimitType();
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

    private double getMeterValue(List<MeterValueType> meterValues) {
        if (meterValues == null) {
            return 0;
        }
        for (MeterValueType meterValue : meterValues) {
            if (meterValue.getSampledValue() != null) {
                for (SampledValueType sampledValue : meterValue.getSampledValue()) {
                    if (sampledValue.getMeasurand() == null) {
                        sampledValue.setMeasurand(MeasurandEnumType.Energy_Active_Import_Register);
                    }
                    if (sampledValue.getMeasurand() == MeasurandEnumType.Energy_Active_Import_Register) {
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

    private ObjectNode update(kr.co.kevit.ocpp201.request.TransactionEvent request, String[] csIds) {
        //
        int evseId = request.getEvse() != null ? request.getEvse().getId() : 1;
        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0],
                csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.stream().filter(s -> s.getEvseId() == evseId)
                .findFirst().orElse(null);
        if (chargerStatusInfo == null) {
            return objectMapper.createObjectNode();
        }
        ChargingStation station = chargingStationService.retrieveChargingStationByCpIdNCsId(chargerStatusInfo.getCpId(),
                chargerStatusInfo.getCsId());
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
        double meterEnd = getMeterValue(request.getMeterValue());

        recharging.setEndCaEleEnerge(new BigDecimal(meterEnd).divide(new BigDecimal(1000))); // 시작 시 전력량 (Wh -> kWh)
        recharging.setChUseAmount(recharging.getEndCaEleEnerge().subtract(recharging.getStartCaEleEnerge()));

        BigDecimal fUseAmount = recharging.getChUseAmount();
        chargerStatusInfo.setInstChAmont(fUseAmount.subtract(chargerStatusInfo.getCuEleEnerge()));// 순간 충전량
        chargerStatusInfo.setCuEleEnerge(fUseAmount);// 충전사용전력량

        if (chargerStatusInfo.getChStartDate() == null) {
            if (endTime.contains(StringConstants.DOT)) {
                chargerStatusInfo.setChStartDate(
                        DateUtils.stringToDate(endTime, DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS));
            } else {
                chargerStatusInfo
                        .setChStartDate(DateUtils.stringToDate(endTime, DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
            }
        }
        // 요금 계산
        calculateFee(chargerStatusInfo, station);

        chargerStatusInfo.setEventCode(StringConstants.BLANK);
        chargerStatusInfo.setCaEleEnerge(new BigDecimal(meterEnd));
        chargerStatusInfo.setChSum(chargerStatusInfo.getChSum().add(chargerStatusInfo.getInstChSum()));// 충전요금
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
        recharging.setPaySum(chargerStatusInfo.getChSum().setScale(0, RoundingMode.FLOOR).intValue());
        recharging.setChStartDate(chargerStatusInfo.getChStartDate());// 충전 시작 시간
        recharging.setChEndDate(chargerStatusInfo.getChEndDate());// 충전 종료 시간
        applyTriggerReasonTime(recharging, request);
        rechargingService.modifyRecharging(recharging);

        // CSMS override 또는 CS-set maxEnergy echo (E16.FR.02 / E16.FR.07 / E16.FR.08)
        applyMaxEnergyToResponse(response, recharging, request);

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

    private ObjectNode end(kr.co.kevit.ocpp201.request.TransactionEvent request, String[] csIds) {
        //
        int evseId = request.getEvse() != null ? request.getEvse().getId() : 1;
        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0],
                csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.stream().filter(s -> s.getEvseId() == evseId)
                .findFirst().orElse(null);
        if (chargerStatusInfo == null) {
            return objectMapper.createObjectNode();
        }
        ChargingStation station = chargingStationService.retrieveChargingStationByCpIdNCsId(chargerStatusInfo.getCpId(),
                chargerStatusInfo.getCsId());
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
        if (request.getMeterValue() != null && request.getMeterValue().size() != 0) {
            meterEnd = getMeterValue(request.getMeterValue());
        }

        recharging.setEndCaEleEnerge(new BigDecimal(meterEnd).divide(new BigDecimal(1000))); // 시작 시 전력량 (Wh -> kWh)
        recharging.setChUseAmount(recharging.getEndCaEleEnerge().subtract(recharging.getStartCaEleEnerge()));

        BigDecimal fUseAmount = recharging.getChUseAmount();
        chargerStatusInfo.setInstChAmont(fUseAmount.subtract(chargerStatusInfo.getCuEleEnerge()));// 순간 충전량
        chargerStatusInfo.setCuEleEnerge(fUseAmount);// 충전사용전력량

        if (chargerStatusInfo.getChStartDate() == null) {
            if (endTime.contains(StringConstants.DOT)) {
                chargerStatusInfo.setChStartDate(
                        DateUtils.stringToDate(endTime, DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS));
            } else {
                chargerStatusInfo
                        .setChStartDate(DateUtils.stringToDate(endTime, DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
            }
        }
        // 요금 계산
        calculateFee(chargerStatusInfo, station);

        chargerStatusInfo.setCaEleEnerge(new BigDecimal(meterEnd));
        chargerStatusInfo.setChSum(chargerStatusInfo.getChSum().add(chargerStatusInfo.getInstChSum()));// 충전요금
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
        recharging.setPaySum(chargerStatusInfo.getChSum().setScale(0, RoundingMode.FLOOR).intValue() > 0
                ? chargerStatusInfo.getChSum().setScale(0, RoundingMode.FLOOR).intValue()
                : 0);
        recharging.setChStartDate(chargerStatusInfo.getChStartDate());// 충전 시작 시간
        recharging.setChEndDate(chargerStatusInfo.getChEndDate());// 충전 종료 시간
        applyTriggerReasonTime(recharging, request);
        rechargingService.modifyRecharging(recharging);

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
     * 요청 페이로드의 {@code transactionInfo.transactionLimit.maxEnergy} 추출. 없으면 null.
     */
    private Double extractMaxEnergy(kr.co.kevit.ocpp201.request.TransactionEvent request) {
        if (request == null || request.getTransactionInfo() == null) return null;
        TransactionLimitType limit = request.getTransactionInfo().getTransactionLimit();
        return limit == null ? null : limit.getMaxEnergy();
    }

    /**
     * Recharging.maxEnergy 가 0 보다 크면 응답의 transactionLimit.maxEnergy 에 동봉 (E16.FR.02 / E16.FR.07).
     *
     * <p>E16.FR.08 — 요청에 포함된 CS-reported limit 이 CSMS 요구 limit 이하이면 echo 금지.</p>
     */
    private void applyMaxEnergyToResponse(kr.co.kevit.ocpp201.response.TransactionEvent response,
                                          Recharging recharging,
                                          kr.co.kevit.ocpp201.request.TransactionEvent request) {
        if (response == null || recharging == null) return;
        Double max = recharging.getMaxEnergy();
        if (max == null || max <= 0) return;

        // E16.FR.08 : CS-reported limit ≤ CSMS-required limit → echo 금지
        Double reqMax = extractMaxEnergy(request);
        if (reqMax != null && reqMax <= max) return;

        TransactionLimitType tlt = response.getTransactionLimit();
        if (tlt == null) tlt = new TransactionLimitType();
        tlt.setMaxEnergy(max);
        response.setTransactionLimit(tlt);
    }
}

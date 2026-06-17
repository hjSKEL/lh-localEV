/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation.
 * All rights reserved. This software is the proprietary information of
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.ocpp20.caller.OcspCaller;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerCert;
import kr.co.kevit.localcsms.customer.process.CustomerCertService;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerVehicle;
import kr.co.kevit.localcsms.customer.process.CustomerService;
import kr.co.kevit.localcsms.customer.process.CustomerVehicleService;
import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCard;
import kr.co.kevit.localcsms.payment.entity.domain.Tariff;
import kr.co.kevit.localcsms.payment.process.PrepaidCardService;
import kr.co.kevit.localcsms.payment.process.TariffService;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.process.RechargingService;
import kr.co.kevit.ocpp201.domain.IdTokenInfoType;
import kr.co.kevit.ocpp201.domain.IdTokenType;
import kr.co.kevit.ocpp201.domain.TariffType;
import kr.co.kevit.ocpp201.enumtype.EnergyTransferModeEnumType;

import java.util.ArrayList;
import kr.co.kevit.ocpp201.enumtype.AuthorizationStatusEnumType;
import kr.co.kevit.ocpp201.enumtype.AuthorizeCertificateStatusEnumType;
import kr.co.kevit.ocpp201.enumtype.IdTokenEnumType;
import kr.co.kevit.ocpp201.exception.OCPPErrorCode;
import kr.co.kevit.ocpp201.exception.OCPPException;
import kr.co.kevit.ocpp201.util.OCPPStringConstraints;

/**
 *
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 3. 11.
 */
@Component("Authorize")
public class AuthorizeBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizeBean.class);
    @Autowired
    private ObjectMapper objectMapper;
    private static final DateTimeFormatter ISO_SECONDS_UTC = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .withZone(ZoneOffset.UTC);

    @Autowired(required = false)
    private ChargerStatusService chargerStatusService;

    @Autowired(required = false)
    private CustomerService customerService;

    @Autowired(required = false)
    private CustomerVehicleService customerVehicleService;

    @Autowired(required = false)
    private CustomerCertService custCertService;

    @Autowired(required = false)
    private RechargingService rechargingService;

    @Autowired(required = false)
    private PrepaidCardService prepaidCardService;

    @Autowired(required = false)
    private TariffService tariffService;

    private final String EVT0J2 = "EVT0J2";

    /**
     * Accepted 응답에 driver tariff + V2X allowedEnergyTransfer 동봉 후 직렬화.
     * driver tariff/V2X 는 Customer.customerId 기반 — eMAID/카드 인증 모두에서 일관 동작.
     *
     * <p>
     * eMAID 인증(Contract Cert) 의 경우 groupIdToken 은 응답에 동봉하지 않는다.
     * 그룹/마스터 카드 개념은 RFID 등 카드 기반 인증에서만 의미가 있으며, eMAID 는
     * ISO 15118 Contract 자체가 식별자라 그룹 체이닝이 부적합하다.
     * </p>
     *
     * @param customerId  Customer.customerId — control() 에서 미리 resolve
     * @param requestType 요청 idToken type — eMAID 면 groupIdToken strip
     */
    private ObjectNode toTree(kr.co.kevit.ocpp201.response.Authorize response, String customerId,
            IdTokenEnumType requestType) {
        attachDriverTariff(response, customerId);
        attachAllowedEnergyTransfer(response, customerId);
        if (requestType == IdTokenEnumType.eMAID && response.getIdTokenInfo() != null) {
            response.getIdTokenInfo().setGroupIdToken(null);
        }
        ObjectNode out = objectMapper.valueToTree(response);
        return out;
    }

    private void attachDriverTariff(kr.co.kevit.ocpp201.response.Authorize response, String customerId) {
        if (response == null || response.getIdTokenInfo() == null)
            return;
        if (!AuthorizationStatusEnumType.Accepted.equals(response.getIdTokenInfo().getStatus()))
            return;
        if (tariffService == null || customerId == null || customerId.isEmpty())
            return;
        try {
            Tariff t = tariffService.retrieveActiveDriverTariff(customerId);
            if (t == null || t.getTariffJson() == null)
                return;
            TariffType ocppTariff = objectMapper.readValue(t.getTariffJson(), TariffType.class);
            ocppTariff.setTariffId(t.getTariffId());
            ocppTariff.setCurrency(t.getCurrency());
            if (t.getValidFrom() != null) {
                ocppTariff.setValidFrom(ISO_SECONDS_UTC.format(t.getValidFrom().toInstant()));
            }
            response.setTariff(ocppTariff);
        } catch (Exception ex) {
            LOGGER.warn("Driver tariff attach failed for customerId={}: {}", customerId, ex.getMessage());
        }
    }

    /**
     * OCPP 2.1 AuthorizeResponse.allowedEnergyTransfer 동봉. (V2X 정책 announce)
     * Customer.V2X_CONTRACT_YN=Y 이고 ALLOWED_ENERGY_TRANSFER CSV 가 있는 경우에만 적용.
     * omit 시 CS 측 default = "charging only" (단방향).
     *
     * <p>
     * customerId 기반 lookup — eMAID 인증의 경우 idTag(eMAID) 는 카드번호가 아니므로
     * {@code retrieveCustomerByCustomerCardNo} 로 못 찾음. control() 에서 미리 resolve 된
     * customerId 사용.
     * </p>
     */
    private void attachAllowedEnergyTransfer(kr.co.kevit.ocpp201.response.Authorize response, String customerId) {
        if (response == null || response.getIdTokenInfo() == null)
            return;
        if (!AuthorizationStatusEnumType.Accepted.equals(response.getIdTokenInfo().getStatus()))
            return;
        if (customerService == null || customerId == null || customerId.isEmpty())
            return;
        try {
            Customer customer = customerService.retrieveCustomerByUserId(customerId);
            if (customer == null)
                return;
            if (!"Y".equalsIgnoreCase(customer.getV2xContractYn()))
                return;
            String csv = customer.getAllowedEnergyTransfer();
            if (csv == null || csv.trim().isEmpty())
                return;
            List<EnergyTransferModeEnumType> modes = new ArrayList<>();
            for (String s : csv.split(",")) {
                String t = s.trim();
                if (t.isEmpty())
                    continue;
                try {
                    modes.add(EnergyTransferModeEnumType.valueOf(t));
                } catch (IllegalArgumentException e) {
                    LOGGER.warn("Unknown EnergyTransferMode '{}' for customerId={}", t, customerId);
                }
            }
            if (!modes.isEmpty()) {
                response.setAllowedEnergyTransfer(modes);
            }
        } catch (Exception ex) {
            LOGGER.warn("allowedEnergyTransfer attach failed for customerId={}: {}", customerId, ex.getMessage());
        }
    }

    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        LOGGER.debug("Auth CSID : {}", cpCsId);
        String[] csIds = cpCsId.split(StringConstants.DASH);

        String text = msg.getPayload().toString();
        kr.co.kevit.ocpp201.request.Authorize request = objectMapper.readValue(text,
                kr.co.kevit.ocpp201.request.Authorize.class);
        kr.co.kevit.ocpp201.response.Authorize response = new kr.co.kevit.ocpp201.response.Authorize();
        IdTokenEnumType requestType = request.getIdToken().getType();
        String customerId = null;
        String idTag = request.getIdToken().getIdToken();
        CustomerMgt customerMgt = null;
        Customer customer = null;
        PrepaidCard prepaidCard = null;
        switch (request.getIdToken().getType()) {
            case KeyCode:// Key Input
            case ISO15693:// RFID
            case Central:
            case Local:
                customer = customerService.retrieveCustomerByCustomerCardNo(idTag);
                if (customer != null) {
                    customerMgt = customerService.retrieveCustomerMgtByCustomerId(customer.getCustomerId());
                }
                break;
            case ISO14443:// NFC
                customer = customerService.retrieveCustomerByCustomerCardNo(idTag);
                if (customer != null) {
                    customerMgt = customerService.retrieveCustomerMgtByCustomerId(customer.getCustomerId());
                    prepaidCard = prepaidCardService.retrievePrepaidCard(idTag);
                }
                break;
            case eMAID:
                CustomerCert custCert = custCertService.retrieveCustomerCertByEmaid(idTag);
                if (custCert == null) {
                    response.setCertificateStatus(AuthorizeCertificateStatusEnumType.NoCertificateAvailable);
                    return toTree(response, null, requestType);
                }
                boolean isValidCer = OcspCaller.validateCert(request);
                if (isValidCer) {
                    response.setCertificateStatus(AuthorizeCertificateStatusEnumType.Accepted);
                } else {
                    response.setCertificateStatus(AuthorizeCertificateStatusEnumType.CertificateRevoked);
                }
                customerId = custCert.getCustomerId();
                break;
            case NoAuthorization:
                idTag = StringConstants.BLANK;
                break;
            case EVCCID:
                // V2X: idTag = EVCCID (차량 식별). TB_CUEV001 의 차량 소유자로 customer 매핑.
                if (customerVehicleService != null) {
                    CustomerVehicle vehicle = customerVehicleService.retrieveVehicle(idTag);
                    if (vehicle != null && vehicle.getCustomerId() != null) {
                        customer = customerService.retrieveCustomerByUserId(vehicle.getCustomerId());
                        if (customer != null) {
                            customerMgt = customerService.retrieveCustomerMgtByCustomerId(customer.getCustomerId());
                        }
                    } else {
                        LOGGER.warn("EVCCID 미등록 차량으로 Authorize: {}", idTag);
                    }
                }
                break;
            case VIN:
            case MacAddress:
                // 비주요 차량 식별 — 정책에 따라 거부하거나 카드 매핑으로 fallback.
                LOGGER.info("Authorize via {} : {} — no mapping defined", requestType, idTag);
                break;
            default:
                throw new OCPPException(OCPPErrorCode.MessageTypeNotSupported);
        }
        // 카드 인증(KeyCode/ISO15693/ISO14443/Central/Local) 의 경우 customerId 가 switch 안에서
        // 채워지지 않으므로 customer 에서 추출. attachAllowedEnergyTransfer(customerId) 가 필요.
        if (customerId == null && customer != null) {
            customerId = customer.getCustomerId();
        }
        if (customerId != null) {
            customerMgt = customerService.retrieveCustomerMgtByCustomerId(customerId);
            idTag = customerMgt.getCutCardNo();
        }
        // 사용자인증 이벤트 저장
        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0],
                csIds[1]);
        for (ChargerStatusInfo chargerStatusInfo : chargerStatusInfos) {
            chargerStatusInfo.setInfoCollDate(new Date());
            chargerStatusInfo.setEventCode(EVT0J2);
            chargerStatusInfo.setCutCardNo(idTag);
            chargerStatusInfo.setUpdateDate(new Date());
            chargerStatusService.modifyChargerStatus(chargerStatusInfo);
        }

        IdTokenInfoType idTokenInfo = new IdTokenInfoType();
        idTokenInfo.setLanguage1(OCPPStringConstraints.ko_KR);

        idTokenInfo.setLanguage2(OCPPStringConstraints.en_US);

        // 선불카드(NFC) 활성·잔액>0 인 경우: expireDate 를 현재시각으로 갱신·저장하고
        // 동일 시각을 응답의 cacheExpiryDateTime 에 반영
        if (prepaidCard != null && "PPCS01".equals(prepaidCard.getCardStatCode())) {
            // 밀리초 절단(.000) — DB DATETIME 정밀도와 일치시키고 ISO 문자열에 ".SSS" 가 표시되지 않도록 함
            Date now = new Date(System.currentTimeMillis() / 1000 * 1000);
            idTokenInfo.setCacheExpiryDateTime(ISO_SECONDS_UTC.format(now.toInstant()));
            if (prepaidCard.getBalance() == null || prepaidCard.getBalance() <= 0L) {
                idTokenInfo.setStatus(AuthorizationStatusEnumType.NoCredit);
                response.setIdTokenInfo(idTokenInfo);
                return toTree(response, customerId, requestType);
            }
        }

        if (request.getIdToken().getType().equals(IdTokenEnumType.NoAuthorization)) {
            idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
            response.setIdTokenInfo(idTokenInfo);
            return toTree(response, customerId, requestType);
        }
        // 존재하지 않는 회원 카드번호
        if (customerMgt == null || StringConstants.Y.equals(customerMgt.getDeleteYn())) {
            idTokenInfo.setStatus(AuthorizationStatusEnumType.Invalid);
            if (customerMgt != null && !StringUtils.isEmpty(customerMgt.getParentCardNo())) {
                IdTokenType groupIdTokenType = new IdTokenType();
                groupIdTokenType.setIdToken(customerMgt.getParentCardNo());
                groupIdTokenType.setType(request.getIdToken().getType());
                idTokenInfo.setGroupIdToken(groupIdTokenType);
            }
            response.setIdTokenInfo(idTokenInfo);
            return toTree(response, customerId, requestType);
        }

        // 정지된 회원인 경우
        if (StringConstants.Y.equals(customerMgt.getStopYn())) {
            idTokenInfo.setStatus(AuthorizationStatusEnumType.Expired);
            response.setIdTokenInfo(idTokenInfo);
            return toTree(response, customerId, requestType);
        }

        // 준회원인 경우
        if (!StringConstants.MEMB01.equals(customerMgt.getCutGrdCode())) {
            idTokenInfo.setStatus(AuthorizationStatusEnumType.Blocked);
            response.setIdTokenInfo(idTokenInfo);
            return toTree(response, customerId, requestType);
        }
        // 충전 중이 아닌 경우 승인
        IdTokenType groupIdTokenType = new IdTokenType();
        for (ChargerStatusInfo chargerStatusInfo : chargerStatusInfos) {
            if (StringUtils.isEmpty(chargerStatusInfo.getRechargingId())) {
                // 마스터카드는 무조건 충전시작 X
                if ("MEMK06".equals(customerMgt.getCutManageCode())) {
                    idTokenInfo.setStatus(AuthorizationStatusEnumType.Invalid);
                }
                if ("MEMK05".equals(customerMgt.getCutManageCode())) {
                    idTokenInfo.setStatus(AuthorizationStatusEnumType.Invalid);
                } else {
                    idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
                }
                if (!StringUtils.isEmpty(customerMgt.getParentCardNo())) {
                    groupIdTokenType.setIdToken(customerMgt.getParentCardNo());
                    groupIdTokenType.setType(request.getIdToken().getType());
                    idTokenInfo.setGroupIdToken(groupIdTokenType);
                }
                response.setIdTokenInfo(idTokenInfo);
                return toTree(response, customerId, requestType);
            } else {
                Recharging recharging = rechargingService
                        .retrieveRecharging4IfById(chargerStatusInfo.getRechargingId());
                // 인증전문 없이 충전시작인 경우 충전중이라도 요청전문의 idTag값이 유효하면 Accepted 전송
                if (StringConstants.TEMP_ID.equals(recharging.getCutCardNo())) {
                    if (!StringUtils.isEmpty(customerMgt.getParentCardNo())) {
                        groupIdTokenType.setIdToken(customerMgt.getParentCardNo());
                        groupIdTokenType.setType(request.getIdToken().getType());
                        idTokenInfo.setGroupIdToken(groupIdTokenType);
                    }
                    idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
                    response.setIdTokenInfo(idTokenInfo);
                    return toTree(response, customerId, requestType);
                }

                if (recharging.getCutCardNo().equals(idTag)) {
                    idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
                    if (!StringUtils.isEmpty(customerMgt.getParentCardNo())) {
                        groupIdTokenType.setIdToken(customerMgt.getParentCardNo());
                        groupIdTokenType.setType(request.getIdToken().getType());
                        idTokenInfo.setGroupIdToken(groupIdTokenType);
                    }
                    response.setIdTokenInfo(idTokenInfo);
                    return toTree(response, customerId, requestType);
                }
                // 마스터카드는 무조건 충전종료 O
                if ("MEMK06".equals(customerMgt.getCutManageCode())) {
                    if (!StringUtils.isEmpty(customerMgt.getParentCardNo())) {
                        groupIdTokenType.setIdToken(customerMgt.getParentCardNo());
                        groupIdTokenType.setType(request.getIdToken().getType());
                        idTokenInfo.setGroupIdToken(groupIdTokenType);
                    }
                    idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
                    response.setIdTokenInfo(idTokenInfo);
                    return toTree(response, customerId, requestType);
                }

                Customer ingCustomer = customerService.retrieveCustomerByCustomerCardNo(recharging.getCutCardNo());
                CustomerMgt ingCustomerMgt = ingCustomer != null
                        ? customerService.retrieveCustomerMgtByCustomerId(ingCustomer.getCustomerId())
                        : null;
                if (ingCustomerMgt != null && customerMgt.getParentCardNo() != null
                        && customerMgt.getParentCardNo().equals(ingCustomerMgt.getParentCardNo())) {
                    idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
                    groupIdTokenType.setIdToken(customerMgt.getParentCardNo());
                    groupIdTokenType.setType(request.getIdToken().getType());
                    idTokenInfo.setGroupIdToken(groupIdTokenType);
                    response.setIdTokenInfo(idTokenInfo);
                    return toTree(response, customerId, requestType);
                }
            }
        }

        idTokenInfo.setStatus(AuthorizationStatusEnumType.Invalid);
        response.setIdTokenInfo(idTokenInfo);
        return toTree(response, customerId, requestType);
    }

}

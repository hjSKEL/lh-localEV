/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation.
 * All rights reserved. This software is the proprietary information of
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.time.Instant;
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
import kr.co.kevit.localcsms.certificate.entity.domain.CustomerCert;
import kr.co.kevit.localcsms.certificate.process.CustomerCertService;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.process.CustomerService;
import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCard;
import kr.co.kevit.localcsms.payment.process.PrepaidCardService;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.process.RechargingService;
import kr.co.kevit.ocpp201.domain.IdTokenInfoType;
import kr.co.kevit.ocpp201.domain.IdTokenType;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired(required = false)
    private ChargerStatusService chargerStatusService;

    @Autowired(required = false)
    private CustomerService customerService;

    @Autowired(required = false)
    private CustomerCertService custCertService;

    @Autowired(required = false)
    private RechargingService rechargingService;

    @Autowired(required = false)
    private PrepaidCardService prepaidCardService;

    private final String EVT0J2 = "EVT0J2";

    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        LOGGER.debug("Auth CSID : {}", cpCsId);
        String[] csIds = cpCsId.split(StringConstants.DASH);

        String text = msg.getPayload().toString();
        kr.co.kevit.ocpp201.request.Authorize request = objectMapper.readValue(text,
                kr.co.kevit.ocpp201.request.Authorize.class);
        kr.co.kevit.ocpp201.response.Authorize response = new kr.co.kevit.ocpp201.response.Authorize();
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
                    return objectMapper.valueToTree(response);
                }
                boolean isValidCer = OcspCaller.validateCert(request);
                if (isValidCer) {
                    response.setCertificateStatus(AuthorizeCertificateStatusEnumType.Accepted);
                } else {
                    response.setCertificateStatus(AuthorizeCertificateStatusEnumType.Accepted);
                }
                customerId = custCert.getCustomerId();
                break;
            case NoAuthorization:
                idTag = StringConstants.BLANK;
                break;
            default:
                throw new OCPPException(OCPPErrorCode.MessageTypeNotSupported);
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

        // 선불카드(NFC) 유효 상태(활성·미만료·잔액>0) 시 캐시 만료시각을 현재시각으로 지정
        // → 캐시를 즉시 무효화하여 매 인증마다 잔액 재확인을 유도
        if (prepaidCard != null
                && "PPCS01".equals(prepaidCard.getCardStatCode())
                && prepaidCard.getExpireDate() != null && prepaidCard.getExpireDate().after(new Date())
                && prepaidCard.getBalance() != null && prepaidCard.getBalance() > 0L) {
            idTokenInfo.setCacheExpiryDateTime(Instant.now().toString());
        }

        if (request.getIdToken().getType().equals(IdTokenEnumType.NoAuthorization)) {
            idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
            response.setIdTokenInfo(idTokenInfo);
            return objectMapper.valueToTree(response);
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
            return objectMapper.valueToTree(response);
        }

        // 정지된 회원인 경우
        if (StringConstants.Y.equals(customerMgt.getStopYn())) {
            idTokenInfo.setStatus(AuthorizationStatusEnumType.Expired);
            response.setIdTokenInfo(idTokenInfo);
            return objectMapper.valueToTree(response);
        }

        // 준회원인 경우
        if (!StringConstants.MEMB01.equals(customerMgt.getCutGrdCode())) {
            idTokenInfo.setStatus(AuthorizationStatusEnumType.Blocked);
            response.setIdTokenInfo(idTokenInfo);
            return objectMapper.valueToTree(response);
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
                return objectMapper.valueToTree(response);
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
                    return objectMapper.valueToTree(response);
                }

                if (recharging.getCutCardNo().equals(idTag)) {
                    idTokenInfo.setStatus(AuthorizationStatusEnumType.Accepted);
                    if (!StringUtils.isEmpty(customerMgt.getParentCardNo())) {
                        groupIdTokenType.setIdToken(customerMgt.getParentCardNo());
                        groupIdTokenType.setType(request.getIdToken().getType());
                        idTokenInfo.setGroupIdToken(groupIdTokenType);
                    }
                    response.setIdTokenInfo(idTokenInfo);
                    return objectMapper.valueToTree(response);
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
                    return objectMapper.valueToTree(response);
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
                    return objectMapper.valueToTree(response);
                }
            }
        }

        idTokenInfo.setStatus(AuthorizationStatusEnumType.Invalid);
        response.setIdTokenInfo(idTokenInfo);
        return objectMapper.valueToTree(response);
    }

}

/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation.
 * All rights reserved. This software is the proprietary information of
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.client.Get15118EvCertEaiClient;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.ocpp201.domain.StatusInfoType;
import kr.co.kevit.ocpp201.enumtype.Iso15118EVCertificateStatusEnumType;

/**
 * Get15118EVCertificate 처리. daemon 은 비즈니스 로직 없이 OCPP 2.1 요청 객체를 api-eai 로
 * 그대로 전달하고, api-eai 응답 객체를 그대로 반환한다.
 * (단, api-eai 로 보내기 전에 필수 항목 정합성만 검증한다.)
 *
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2021. 3. 28.
 */
@Component("Get15118EVCertificate")
public class Get15118EVCertificateBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(Get15118EVCertificateBean.class);

    @Autowired
    private ObjectMapper objectMapper;

    /** Get15118EVCertificate 요청/응답을 api-eai 로 그대로 위임하는 클라이언트 */
    @Autowired
    private Get15118EvCertEaiClient get15118EvCertEaiClient;

    /**
     *
     * {@inheritDoc}0
     */
    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String text = msg.getPayload().toString();
        kr.co.kevit.ocpp201.request.Get15118EVCertificate request = objectMapper.readValue(text,
                kr.co.kevit.ocpp201.request.Get15118EVCertificate.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Get15118EVCertificateBean.control : {}", text);
        }

        // 필수 항목 정합성 검증. 누락 시 api-eai 로 전달하지 않고 Failed 반환.
        String missing = validateRequired(request);
        if (missing != null) {
            LOGGER.warn("Get15118EVCertificateBean: 필수 항목 누락/유효하지 않음 [{}]. cpCsId={}", missing, cpCsId);
            return failed("Missing/invalid required field: " + missing);
        }

        // 요청 객체를 api-eai 로 그대로 전달하고 응답 객체를 그대로 받아온다. (URL 에 충전기 ID 포함)
        kr.co.kevit.ocpp201.response.Get15118EVCertificate response = get15118EvCertEaiClient.getEVCertificate(cpCsId,
                request);

        // api-eai 호출 실패(통신 오류 등) 시에만 Failed 로 응답
        if (response == null) {
            LOGGER.warn("Get15118EVCertificateBean: api-eai 응답 없음(호출 실패). cpCsId={}", cpCsId);
            return failed("api-eai unreachable");
        }

        return objectMapper.valueToTree(response);
    }

    /**
     * OCPP 2.1 Get15118EVCertificateRequest 필수 항목 검증.
     * <ul>
     * <li>iso15118SchemaVersion (required)</li>
     * <li>action (required) - Install/Update</li>
     * <li>exiRequest (required)</li>
     * <li>maximumContractCertificateChains - ISO 15118-20 세션에서는 required</li>
     * </ul>
     *
     * @return 누락/유효하지 않은 필드명. 모두 유효하면 null
     */
    private String validateRequired(kr.co.kevit.ocpp201.request.Get15118EVCertificate request) {
        if (StringUtils.isEmpty(request.getIso15118SchemaVersion())) {
            return "iso15118SchemaVersion";
        }
        if (request.getAction() == null) {
            return "action";
        }
        if (StringUtils.isEmpty(request.getExiRequest())) {
            return "exiRequest";
        }
        // ISO 15118-20 세션에서는 maximumContractCertificateChains 가 필수 (스키마 정의)
        if (isIso15118v20(request.getIso15118SchemaVersion())
                && request.getMaximumContractCertificateChains() == null) {
            return "maximumContractCertificateChains";
        }
        return null;
    }

    /** iso15118SchemaVersion 이 ISO 15118-20 네임스페이스인지 판별(-20 / "-20" 포함 여부) */
    private boolean isIso15118v20(String schemaVersion) {
        return schemaVersion != null && schemaVersion.contains("-20");
    }

    /** 검증/호출 실패 시 Failed 응답(JSON) 생성. statusInfo 에 사유를 담는다. */
    private ObjectNode failed(String reason) {
        kr.co.kevit.ocpp201.response.Get15118EVCertificate resp = new kr.co.kevit.ocpp201.response.Get15118EVCertificate();
        resp.setStatus(Iso15118EVCertificateStatusEnumType.Failed);
        StatusInfoType statusInfo = new StatusInfoType();
        statusInfo.setReasonCode("InvalidRequest");
        statusInfo.setAdditionalInfo(reason);
        resp.setStatusInfo(statusInfo);
        return objectMapper.valueToTree(resp);
    }
}

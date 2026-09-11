/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import kr.co.kevit.localcsms.common.domain.CodeVal;
import kr.co.kevit.localcsms.common.process.CodeValService;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.client.ApiEaiInboundClient;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.ocpp201.enumtype.NotifyEVChargingNeedsStatusEnumType;

/**
 * NotifyEVChargingNeeds (ISO 15118-20 K16/K17/K19/K20) — thin OCPP transport.
 *
 * <p>
 * 본 빈은 CS 로부터 받은 메시지를 즉시 {@code Processing} 으로 응답하고 api-eai 의
 * inbound 콜백으로 forward 하는 것만 담당한다. 협상 판단(어떤 SetChargingProfile 을 보낼지),
 * EMS/외부 정책 연동, DB 영속화 등의 모든 비즈니스 결정은 api-eai 의
 * {@code Ocpp2xInboundController} 에서 수행된다.
 * </p>
 *
 * <p>
 * api-eai 가 결정 후 {@code Daemon2xClient → /ocpp2x/command/{cpCsId}} 로 다시 호출하면
 * {@code Ocpp20WebSocketHandler.sendCommand} 가 CS 로 SetChargingProfile 을 송신한다.
 * </p>
 *
 * @author bckim
 */
@Component("NotifyEVChargingNeeds")
public class NotifyEVChargingNeedsBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyEVChargingNeedsBean.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private ApiEaiInboundClient apiEaiInboundClient;

    @Autowired(required = false)
    private CodeValService codeValService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        kr.co.kevit.ocpp201.response.NotifyEVChargingNeeds response = new kr.co.kevit.ocpp201.response.NotifyEVChargingNeeds();
        response.setStatus(NotifyEVChargingNeedsStatusEnumType.Rejected);

        // api-eai 비동기 forward — 결정은 api-eai 에서. fire-and-forget.

        CodeVal codeVal = codeValService.retrieveCodeValByCode("OCPP04");
        if (codeVal != null){
            switch(codeVal.getCodeValue()){
                case "Accepted":
                    /* 임시 주석.
                    if (apiEaiInboundClient != null) {
                        try {
                            apiEaiInboundClient.forward(cpCsId, "NotifyEVChargingNeeds", msg.getPayload());
                        } catch (Exception e) {
                            LOGGER.warn("api-eai inbound forward 호출 실패 cpCsId={}: {}", cpCsId,
                                    e.getMessage());
                        }
                    } else {
                        LOGGER.warn("ApiEaiInboundClient 미주입 — NotifyEVChargingNeeds 후속 처리 누락 cpCsId={}", cpCsId);
                    }*/
                    response.setStatus(NotifyEVChargingNeedsStatusEnumType.Accepted);
                    break;
                case "Processing":
                    response.setStatus(NotifyEVChargingNeedsStatusEnumType.Processing);
                    break;
                case "NoChargingProfile":
                    response.setStatus(NotifyEVChargingNeedsStatusEnumType.NoChargingProfile);
                    break;
            }
        }
        return objectMapper.valueToTree(response);
    }
}

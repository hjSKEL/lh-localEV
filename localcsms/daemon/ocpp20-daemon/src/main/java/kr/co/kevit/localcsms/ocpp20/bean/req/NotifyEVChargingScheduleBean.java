/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;


import java.util.List;

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
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.smartcharging.process.NegotiationCoordinator;
import kr.co.kevit.localcsms.common.domain.CodeVal;
import kr.co.kevit.localcsms.common.process.CodeValService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.ocpp201.enumtype.GenericStatusEnumType;

/**
 * NotifyEVChargingSchedule (K16/K17/K20) — EV 가 협상한 충전 스케줄을 CS 가 CSMS 로 통보.
 *
 * <p>스마트차징 기능(OCPP02) 이 켜져 있고, 해당 evseId 에 진행 중 충전이 있으며,
 * chargingSchedule 이 동봉된 경우 Accepted. 그 외 Rejected.</p>
 *
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2022. 9. 8.
 */
@Component("NotifyEVChargingSchedule")
public class NotifyEVChargingScheduleBean implements ControlerBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyEVChargingScheduleBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired(required = false)
    private CodeValService codeValService;

    @Autowired(required = false)
    private ChargerStatusService chargerStatusService;

    @Autowired(required = false)
    private NegotiationCoordinator negotiationCoordinator;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String[] csIds = cpCsId.split(StringConstants.DASH);

        String text = msg.getPayload().toString();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("NotifyEVChargingScheduleBean text : {}", text);
        }

        kr.co.kevit.ocpp201.request.NotifyEVChargingSchedule obj =
                objectMapper.readValue(text, kr.co.kevit.ocpp201.request.NotifyEVChargingSchedule.class);
        kr.co.kevit.ocpp201.response.NotifyEVChargingSchedule response =
                new kr.co.kevit.ocpp201.response.NotifyEVChargingSchedule();

        // 스마트차징 기능 마스터 토글
        CodeVal codeVal = codeValService.retrieveCodeValByCode("OCPP02");
        if (codeVal == null || !"true".equals(codeVal.getCodeValue())) {
            LOGGER.info("NotifyEVChargingSchedule: smart charging disabled (OCPP02) → Rejected");
            response.setStatus(GenericStatusEnumType.Rejected);
            return objectMapper.valueToTree(response);
        }

        // chargingSchedule 누락 시 거부
        if (obj.getChargingSchedule() == null) {
            LOGGER.info("NotifyEVChargingSchedule: chargingSchedule 누락 → Rejected");
            response.setStatus(GenericStatusEnumType.Rejected);
            return objectMapper.valueToTree(response);
        }

        // 해당 evseId 에 진행 중 충전 존재 확인
        List<ChargerStatusInfo> chargerStatusInfos =
                chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.stream()
                .filter(s -> s.getEvseId() == obj.getEvseId()).findFirst().orElse(null);
        if (chargerStatusInfo == null || StringUtils.isEmpty(chargerStatusInfo.getRechargingId())) {
            LOGGER.info("NotifyEVChargingSchedule: evseId={} 진행 중 충전 없음 → Rejected", obj.getEvseId());
            response.setStatus(GenericStatusEnumType.Rejected);
            return objectMapper.valueToTree(response);
        }

        // 협상 상태 SCHEDULE_CONFIRMED 전이
        if (negotiationCoordinator != null) {
            try {
                negotiationCoordinator.confirmSchedule(csIds[0], csIds[1], obj.getEvseId());
            } catch (Exception ex) {
                LOGGER.warn("confirmSchedule 실패 cpCsId={} evseId={}: {}", cpCsId, obj.getEvseId(), ex.getMessage());
            }
        }

        response.setStatus(GenericStatusEnumType.Accepted);
        return objectMapper.valueToTree(response);
    }

}

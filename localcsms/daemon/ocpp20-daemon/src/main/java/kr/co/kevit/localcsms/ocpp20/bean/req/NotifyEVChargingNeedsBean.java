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

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.smartcharging.process.NegotiationCoordinator;
import kr.co.kevit.localcsms.smartcharging.process.NegotiationResult;
import kr.co.kevit.localcsms.common.domain.CodeVal;
import kr.co.kevit.localcsms.common.process.CodeValService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.bean.FollowUpCapable;
import kr.co.kevit.localcsms.ocpp20.bean.OutboundCall;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.ocpp201.domain.ChargingProfileType;
import kr.co.kevit.ocpp201.request.SetChargingProfile;

/**
 * NotifyEVChargingNeeds (ISO 15118-20 K16/K17/K19/K20) — thin adapter.
 *
 * <p>요청 파싱·CS 상태 조회만 담당하고 협상 판단은 {@link NegotiationCoordinator} 에 위임.
 * 응답(NeedsResponse) 송신 직후 {@link FollowUpCapable} 훅으로 SetChargingProfile 을 push.</p>
 *
 * @author bckim
 */
@Component("NotifyEVChargingNeeds")
public class NotifyEVChargingNeedsBean implements ControlerBean, FollowUpCapable {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyEVChargingNeedsBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /** control() → followUp() 간 push 대상 전달 (dispatchCall 동일 스레드 순차 실행) */
    private final ThreadLocal<ChargingProfileType> pendingPush = new ThreadLocal<>();

    @Autowired(required = false)
    private ChargerStatusService chargerStatusService;

    @Autowired(required = false)
    private CodeValService codeValService;

    @Autowired(required = false)
    private NegotiationCoordinator negotiationCoordinator;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        pendingPush.remove();
        String[] csIds = cpCsId.split(StringConstants.DASH);

        kr.co.kevit.ocpp201.request.NotifyEVChargingNeeds req =
                objectMapper.readValue(msg.getPayload().toString(), kr.co.kevit.ocpp201.request.NotifyEVChargingNeeds.class);
        kr.co.kevit.ocpp201.response.NotifyEVChargingNeeds response =
                new kr.co.kevit.ocpp201.response.NotifyEVChargingNeeds();

        int evseId = req.getEvseId() != null ? req.getEvseId() : 0;
        List<ChargerStatusInfo> infos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        ChargerStatusInfo info = infos.stream().filter(s -> s.getEvseId() == evseId).findFirst().orElse(null);
        boolean hasActiveCharging = info != null && !StringUtils.isEmpty(info.getRechargingId());
        String rechargingId = info != null ? info.getRechargingId() : null;

        NegotiationResult result = negotiationCoordinator.handleNeeds(
                csIds[0], csIds[1], rechargingId, req, hasActiveCharging, isNoProfilePolicy());

        response.setStatus(result.getStatus());
        if (result.hasProfileToPush()) {
            pendingPush.set(result.getProfileToPush());
        }
        return objectMapper.valueToTree(response);
    }

    @Override
    public OutboundCall followUp(String cpCsId, OcppMessage msg, ObjectNode response) throws Exception {
        ChargingProfileType profile = pendingPush.get();
        pendingPush.remove();
        if (profile == null) {
            return null;
        }
        kr.co.kevit.ocpp201.request.NotifyEVChargingNeeds req =
                objectMapper.readValue(msg.getPayload().toString(), kr.co.kevit.ocpp201.request.NotifyEVChargingNeeds.class);
        SetChargingProfile scp = new SetChargingProfile();
        scp.setEvseId(req.getEvseId() != null ? req.getEvseId() : 0);
        scp.setChargingProfile(profile);
        ObjectNode payload = objectMapper.valueToTree(scp);
        LOGGER.info("협상 push: SetChargingProfile cpCsId={} profileId={}", cpCsId, profile.getId());
        return new OutboundCall("SetChargingProfile", payload);
    }

    private boolean isNoProfilePolicy() {
        if (codeValService == null) {
            return false;
        }
        CodeVal codeVal = codeValService.retrieveCodeValByCode("OCPP03");
        return codeVal != null && "true".equals(codeVal.getCodeValue());
    }
}

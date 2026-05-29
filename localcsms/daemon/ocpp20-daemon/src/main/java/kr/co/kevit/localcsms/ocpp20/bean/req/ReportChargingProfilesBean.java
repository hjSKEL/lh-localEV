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
import kr.co.kevit.localcsms.smartcharging.process.SmartChargingService;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * ReportChargingProfiles (CS→CSMS, K) — GetChargingProfiles 응답으로 CS 보유 프로파일 보고.
 *
 * <p>보고된 프로파일을 CSMS 저장소에 동기화(upsert)하여 CSMS 뷰를 CS 실상태와 일치시킴.
 * requestId 는 선행 GetChargingProfiles 와의 correlation 키.</p>
 *
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2022. 9. 8.
 */
@Component("ReportChargingProfiles")
public class ReportChargingProfilesBean implements ControlerBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportChargingProfilesBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired(required = false)
    private SmartChargingService smartChargingService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String[] csIds = cpCsId.split(StringConstants.DASH);

        String text = msg.getPayload().toString();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ReportChargingProfilesBean text : {}", text);
        }
        kr.co.kevit.ocpp201.request.ReportChargingProfiles obj =
                objectMapper.readValue(text, kr.co.kevit.ocpp201.request.ReportChargingProfiles.class);

        if (smartChargingService != null && obj.getChargingProfile() != null) {
            try {
                int synced = smartChargingService.syncReportedProfiles(
                        csIds[0], csIds[1], obj.getEvseId(), obj.getChargingProfile());
                LOGGER.info("ReportChargingProfiles cpCsId={} requestId={} tbc={} synced={}",
                        cpCsId, obj.getRequestId(), obj.isTbc(), synced);
            } catch (Exception ex) {
                LOGGER.warn("ReportChargingProfiles 동기화 실패 cpCsId={}: {}", cpCsId, ex.getMessage());
            }
        }

        kr.co.kevit.ocpp201.response.ReportChargingProfiles response = new kr.co.kevit.ocpp201.response.ReportChargingProfiles();
        return objectMapper.valueToTree(response);
    }

}

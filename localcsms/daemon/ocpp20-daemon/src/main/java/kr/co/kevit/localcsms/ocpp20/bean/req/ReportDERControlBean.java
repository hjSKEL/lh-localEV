/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.derctrl.process.DerControlService;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;

/**
 * CS → CSMS: ReportDERControlRequest 수신.
 * payload 내 enterService / freqDroop / curve / fixedPF / fixedVar / gradient / limitMaxDischarge
 * 키별로 분리해 TB_DRCTL01 (ORIGIN=CS_REPORT) 다중 INSERT.
 */
@Component("ReportDERControl")
public class ReportDERControlBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportDERControlBean.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired(required = false)
    private DerControlService derControlService;

    @SuppressWarnings("unchecked")
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String[] csIds = cpCsId.split(StringConstants.DASH);
        String cpId = csIds[0];
        String csId = csIds.length > 1 ? csIds[1] : "";

        String text = msg.getPayload().toString();
        LOGGER.info("[OCPP20] ReportDERControlRequest cpCsId={} payload={}", cpCsId, text);

        if (derControlService != null) {
            try {
                Map<String, Object> payloadMap = objectMapper.readValue(text, Map.class);
                derControlService.recordReport(cpId, csId, payloadMap, "ocpp20-daemon");
            } catch (Exception ex) {
                LOGGER.warn("[OCPP20] ReportDERControl recordReport failed: {}", ex.getMessage());
            }
        }
        return objectMapper.createObjectNode();
    }
}

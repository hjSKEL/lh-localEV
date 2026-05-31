/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerControl;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerStartStop;
import kr.co.kevit.localcsms.smartcharging.process.DerControlService;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;

@Component("NotifyDERStartStop")
public class NotifyDERStartStopBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyDERStartStopBean.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private DerControlService derControlService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String[] csIds = cpCsId.split(StringConstants.DASH);
        String cpId = csIds[0];
        String csId = csIds.length > 1 ? csIds[1] : "";

        String text = msg.getPayload().toString();
        LOGGER.info("[OCPP20] NotifyDERStartStopRequest cpCsId={} payload={}", cpCsId, text);

        kr.co.kevit.ocpp201.request.NotifyDERStartStop req =
                objectMapper.readValue(text, kr.co.kevit.ocpp201.request.NotifyDERStartStop.class);

        if (derControlService != null) {
            try {
                DerStartStop event = new DerStartStop();
                event.setCpId(cpId);
                event.setCsId(csId);
                event.setControlId(req.getControlId());
                event.setStartedYn(req.isStarted() ? DerControl.YES : DerControl.NO);
                if (req.getTimestamp() != null) {
                    event.setTimestampDt(parseIso(req.getTimestamp()));
                } else {
                    event.setTimestampDt(new Date());
                }
                event.setReceivedDate(new Date());
                derControlService.recordStartStop(event);
            } catch (Exception ex) {
                LOGGER.warn("[OCPP20] NotifyDERStartStop recordStartStop failed: {}", ex.getMessage());
            }
        }

        kr.co.kevit.ocpp201.response.NotifyDERStartStop response = new kr.co.kevit.ocpp201.response.NotifyDERStartStop();
        return objectMapper.valueToTree(response);
    }

    private Date parseIso(String s) {
        try {
            if (s.contains(StringConstants.DOT)) {
                return DateUtils.stringToDate(s, DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS);
            }
            return DateUtils.stringToDate(s, DateUtils.RFC3339_DEFAULT_DATE_FORMAT);
        } catch (Exception e) {
            return new Date();
        }
    }
}

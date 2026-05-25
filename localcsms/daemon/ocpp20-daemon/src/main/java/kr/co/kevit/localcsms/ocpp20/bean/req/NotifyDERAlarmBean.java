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
import com.google.gson.Gson;

import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.derctrl.entity.domain.DerAlarm;
import kr.co.kevit.localcsms.derctrl.entity.domain.DerControl;
import kr.co.kevit.localcsms.derctrl.process.DerControlService;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;

@Component("NotifyDERAlarm")
public class NotifyDERAlarmBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyDERAlarmBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Gson gson = new Gson();

    @Autowired(required = false)
    private DerControlService derControlService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String[] csIds = cpCsId.split(StringConstants.DASH);
        String cpId = csIds[0];
        String csId = csIds.length > 1 ? csIds[1] : "";

        String text = msg.getPayload().toString();
        LOGGER.info("[OCPP20] NotifyDERAlarmRequest cpCsId={} payload={}", cpCsId, text);

        kr.co.kevit.ocpp201.request.NotifyDERAlarm req =
                objectMapper.readValue(text, kr.co.kevit.ocpp201.request.NotifyDERAlarm.class);

        if (derControlService != null) {
            try {
                DerAlarm alarm = new DerAlarm();
                alarm.setCpId(cpId);
                alarm.setCsId(csId);
                alarm.setControlType(req.getControlType() != null ? req.getControlType().toString() : "Unknown");
                alarm.setGridEventFault(req.getGridEventFault() != null ? req.getGridEventFault().toString() : null);
                alarm.setAlarmEnded(req.isAlarmEnded() ? DerControl.YES : DerControl.NO);
                if (req.getTimestamp() != null) {
                    alarm.setTimestampDt(parseIso(req.getTimestamp()));
                } else {
                    alarm.setTimestampDt(new Date());
                }
                alarm.setExtraInfo(req.getExtraInfo() != null ? gson.toJson(req.getExtraInfo()) : null);
                alarm.setReceivedDate(new Date());
                derControlService.recordAlarm(alarm);
            } catch (Exception ex) {
                LOGGER.warn("[OCPP20] NotifyDERAlarm recordAlarm failed: {}", ex.getMessage());
            }
        }

        kr.co.kevit.ocpp201.response.NotifyDERAlarm response = new kr.co.kevit.ocpp201.response.NotifyDERAlarm();
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

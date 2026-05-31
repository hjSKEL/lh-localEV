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
import kr.co.kevit.localcsms.charger.entity.domain.CsFirmware;
import kr.co.kevit.localcsms.charger.process.CsFirmwareService;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;

@Component("FirmwareStatusNotification")
public class FirmwareStatusNotificationBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirmwareStatusNotificationBean.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private CsFirmwareService csFirmwareService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String text = msg.getPayload().toString();
        LOGGER.debug("FirmwareStatusNotificationBean.control : {}", text);

        kr.co.kevit.ocpp201.request.FirmwareStatusNotification request =
                objectMapper.readValue(text, kr.co.kevit.ocpp201.request.FirmwareStatusNotification.class);
        LOGGER.debug("getRequestId : {}, getStatus : {}", request.getRequestId(), request.getStatus());

        String csfw = toFirmwareStatusCode(request.getStatus() != null ? request.getStatus().name() : null);
        if (csfw != null) {
            updateFirmwareStatus(cpCsId, csfw);
        }

        kr.co.kevit.ocpp201.response.FirmwareStatusNotification response = new kr.co.kevit.ocpp201.response.FirmwareStatusNotification();
        return objectMapper.valueToTree(response);
    }

    private void updateFirmwareStatus(String cpCsId, String statusCode) {
        try {
            int idx = cpCsId.lastIndexOf('-');
            if (idx < 0) return;
            String cpId = cpCsId.substring(0, idx);
            String csId = cpCsId.substring(idx + 1);

            CsFirmware fw = new CsFirmware();
            fw.setCpId(cpId);
            fw.setCsId(csId);
            fw.setStatus(statusCode);
            fw.setRequestEmployeeId(kr.co.kevit.localcsms.common.util.string.StringConstants.SYSTEM_EMPLOYEE);
            fw.setUpdateDate(new java.util.Date());
            csFirmwareService.modifyFirmwareStatus(fw);
        } catch (Exception e) {
            LOGGER.warn("CsFirmware 상태 업데이트 실패: {}", e.getMessage(), e);
        }
    }

    /** OCPP 2.0.1 FirmwareStatusEnum → CSFW 코드 매핑 */
    private String toFirmwareStatusCode(String ocppStatus) {
        if (ocppStatus == null) return null;
        switch (ocppStatus) {
            case "Downloading":             return "CSFW02";
            case "Downloaded":              return "CSFW03";
            case "DownloadFailed":          return "CSFW04";
            case "Installing":              return "CSFW05";
            case "Installed":               return "CSFW06";
            case "InstallationFailed":      return "CSFW07";
            case "InstallRebooting":        return "CSFW05";
            case "InstallVerification":     return "CSFW05";
            case "InvalidSignature":        return "CSFW04";
            case "SignatureVerified":       return "CSFW03";
            default:                        return null; // Idle, DownloadScheduled, DownloadPaused 등
        }
    }
}
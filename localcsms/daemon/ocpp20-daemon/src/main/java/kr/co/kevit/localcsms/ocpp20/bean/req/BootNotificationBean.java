/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.math.BigDecimal;
import java.util.Date;
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
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStationCsm;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.domain.CodeVal;
import kr.co.kevit.localcsms.common.process.CodeValService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.ocpp201.domain.ChargingStationType;
import kr.co.kevit.ocpp201.enumtype.RegistrationStatusEnumType;

/**
 *
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 3. 8.
 */
@Component("BootNotification")
public class BootNotificationBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootNotificationBean.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private ChargerStatusService chargerStatusService;

    @Autowired(required = false)
    private ChargingStationService chargingStationService;

    @Autowired(required = false)
    private CodeValService codeValService;

    private final String EVENTCODE = "EVT0J4";

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String[] csIds = cpCsId.split(StringConstants.DASH);

        String text = msg.getPayload().toString();
        kr.co.kevit.ocpp201.request.BootNotification obj = objectMapper.readValue(text,
                kr.co.kevit.ocpp201.request.BootNotification.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("BootNotificationBean.control : {}", text);
        }

        kr.co.kevit.ocpp201.response.BootNotification response = new kr.co.kevit.ocpp201.response.BootNotification();
        response.setCurrentTime(DateUtils.dateToString(DateUtils.changeDateWithHourLevel(new Date(), -9),
                DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS));
        CodeVal codeVal = codeValService.retrieveCodeValByCode("OCPP01");
        response.setInterval(Integer.parseInt(codeVal.getCodeValue())); // 300 seconds

        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0],
                csIds[1]);
        if (chargerStatusInfos.isEmpty()) {
            response.setStatus(RegistrationStatusEnumType.Rejected);

            return objectMapper.valueToTree(response);
        }

        String eventCode = EVENTCODE;
        ChargingStation chargingStation = chargingStationService.retrieveChargingStationByCpIdNCsId(csIds[0], csIds[1]);
        if (StringConstants.Y.equals(chargingStation.getUseYn())) {
            response.setStatus(RegistrationStatusEnumType.Accepted);
        } else {
            response.setStatus(RegistrationStatusEnumType.Pending);
        }

        for (ChargerStatusInfo chargerStatusInfo : chargerStatusInfos) {
            chargerStatusInfo.setInfoCollDate(new Date());
            chargerStatusInfo.setEventCode(eventCode);
            chargerStatusInfo.setCuEleEnerge(BigDecimal.ZERO);
            chargerStatusInfo.setCutCardNo(null);
            chargerStatusInfo.setRechargingId(null);
            chargerStatusInfo.setInstChSum(BigDecimal.ZERO);// 순간충전금액
            chargerStatusInfo.setInstChAmont(BigDecimal.ZERO);// 순간 충전량
            chargerStatusInfo.setInstChCost(BigDecimal.ZERO);// 순간충전단가
            chargerStatusInfo.setChSum(BigDecimal.ZERO);// 충전금액
            chargerStatusInfo.setChStartDate(null);// 충전시작시간
            chargerStatusInfo.setChEndDate(null);// 충전종료시간
            chargerStatusInfo.setUpdateDate(new Date());
            chargerStatusService.modifyChargerStatus(chargerStatusInfo);
        }

        // 부팅 정보(시리얼번호/모델명/펌웨어버전) 저장
        ChargingStationType csInfo = obj.getChargingStation();
        if (csInfo != null) {
            ChargingStationCsm csm = new ChargingStationCsm();
            csm.setCpId(csIds[0]);
            csm.setCsId(csIds[1]);
            csm.setSerialNumber(csInfo.getSerialNumber());
            csm.setModelName(csInfo.getModel());
            csm.setFwVer(csInfo.getFirmwareVersion());
            csm.setLastBootDate(new Date());
            chargingStationService.modifyChargingStationCsm(csm);
        }

        return objectMapper.valueToTree(response);
    }
}
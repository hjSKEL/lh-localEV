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
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.ocpp201.domain.MeterValueType;
import kr.co.kevit.ocpp201.domain.SampledValueType;
import kr.co.kevit.ocpp201.domain.UnitOfMeasureType;
import kr.co.kevit.ocpp201.enumtype.MeasurandEnumType;


/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 3. 11.
 */
@Component("MeterValues")
public class MeterValuesBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeterValuesBean.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private ChargerStatusService chargerStatusService;
    
        
    /**
     * 
     * {@inheritDoc}
     */
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String[] csIds = cpCsId.split(StringConstants.DASH);
        
        String text = msg.getPayload().toString();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("MeterValuesBean.control : {}", text);
        }
        kr.co.kevit.ocpp201.request.MeterValues request = objectMapper.readValue(text,kr.co.kevit.ocpp201.request.MeterValues.class);
        if(request.getEvseId() == 0) {
            return objectMapper.valueToTree(new kr.co.kevit.ocpp201.response.MeterValues());
        }
        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.stream().filter(s -> s.getEvseId() == request.getEvseId()).findFirst().orElse(null);
        if (chargerStatusInfo == null) {
            return objectMapper.valueToTree(new kr.co.kevit.ocpp201.response.MeterValues());
        }
        
        for (MeterValueType meterValue : request.getMeterValue()) {
            if(meterValue.getSampledValue() != null) {
                BigDecimal caEleEnerge = getkWh(meterValue.getSampledValue());
                if(caEleEnerge != null) {
                    if(meterValue.getTimestamp().contains(StringConstants.DOT)) {
                        chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(meterValue.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS));
                    }else {
                        chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(meterValue.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
                    }
                    chargerStatusInfo.setCaEleEnerge(caEleEnerge);
                    chargerStatusInfo.setUpdateDate(new Date());
                    chargerStatusService.modifyChargerStatus(chargerStatusInfo);
                }
            }
        }
        return objectMapper.valueToTree(new kr.co.kevit.ocpp201.response.MeterValues());
    }
    
    private BigDecimal getkWh(List<SampledValueType> sampledList) {
        if(sampledList != null) {
            for (SampledValueType sampledValue : sampledList) {
                if(sampledValue.getMeasurand() == null) {
                    sampledValue.setMeasurand(MeasurandEnumType.Energy_Active_Import_Register);
                }
                if(sampledValue.getUnitOfMeasure() == null) {
                    UnitOfMeasureType unitOfMeasure = new UnitOfMeasureType();
                    unitOfMeasure.setUnit("Wh");
                    sampledValue.setUnitOfMeasure(unitOfMeasure);
                }
                if(sampledValue.getMeasurand() == MeasurandEnumType.Energy_Active_Import_Register) {
                    BigDecimal caEleEnerge = new BigDecimal(sampledValue.getValue());
                    if(sampledValue.getUnitOfMeasure().getUnit().equals("Wh")) {
                        caEleEnerge = caEleEnerge.divide(new BigDecimal(1000));
                    }
                    return caEleEnerge;
                }
            }
        }
        return null;
    }

}
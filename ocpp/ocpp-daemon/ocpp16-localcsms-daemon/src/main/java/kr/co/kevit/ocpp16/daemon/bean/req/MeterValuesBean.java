/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.process.ProductPriceService;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.fee.FeeCalculator;
import kr.co.kevit.localcsms.recharger.process.RechargingService;
import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;
import kr.co.kevit.ocpp16.daemon.store.DaemonStore;
import kr.co.kevit.ocpp16.domain.MeterValue;
import kr.co.kevit.ocpp16.domain.SampledValue;
import kr.co.kevit.ocpp16.enumtype.MeasurandTypeEnum;
import kr.co.kevit.ocpp16.enumtype.UnitEnum;


/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 3. 11.
 */
public class MeterValuesBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeterValuesBean.class);
    
    @Autowired
    private ChargerStatusService chargerStatusService;
    
    @Autowired
    private RechargingService rechargingService;

    @Autowired
    private ProductPriceService priceService;
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Object control(String csId, List<Object> reqs) {
        //
        String[] csIds = csId.split(StringConstants.DASH);
        
        Object object = reqs.get(3);
        Gson gson = new GsonBuilder().create();
        String text = gson.toJson(object);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("MeterValuesBean.control : {}", text);
        }
        kr.co.kevit.ocpp16.request.MeterValues request = gson.fromJson(text,kr.co.kevit.ocpp16.request.MeterValues.class);
        if(0 == request.getTransactionId() || request.getMeterValue() == null || request.getMeterValue().isEmpty()) {
            return new Object();
        }

        String connectorId = Integer.toString(request.getConnectorId());
        Date lastDate = DaemonStore.MeterValuesMap.get(csId.concat(StringConstants.UNDER_LINE).concat(connectorId));
        Date curDate = new Date();
        if(lastDate != null && (curDate.getTime() - lastDate.getTime() < 150000)){
            LOGGER.debug("MeterValuesBean Cache Response : {}", text);
            return new Object();
        }
        DaemonStore.MeterValuesMap.put(csId.concat(StringConstants.UNDER_LINE).concat(connectorId), curDate);

        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.get(0);
        if(chargerStatusInfos.size() > 1) {
            for(ChargerStatusInfo datum : chargerStatusInfos) {
                if(datum.getEvseId() == request.getConnectorId()) {
                    chargerStatusInfo = datum;
                }else {
                    datum.setInfoCollDate(new Date());
                    datum.setUpdateDate(new Date());
                    chargerStatusService.modifyChargerStatusWithoutHis(datum);
                }
            }
        }
        Recharging recharging = null;
        if(!StringUtils.isEmpty(chargerStatusInfo.getRechargingId())) {
            recharging = rechargingService.retrieveRecharging4IfById(chargerStatusInfo.getRechargingId());
        }

        if(recharging == null || !recharging.getRechargingId().endsWith(request.getTransactionId().toString())) {
            return new Object();
        }
        
        MeterValue meterValue = request.getMeterValue().get(0);
        if(meterValue.getTimestamp() == null) {
            return new Object();
        }
        
        SampledValue sampledValue = getSampledValue(meterValue.getSampledValue(), MeasurandTypeEnum.Energy_Active_Import_Register);
        if (sampledValue == null) {
            sampledValue = new SampledValue();
            sampledValue.setValue(StringConstants.ZERO);
            sampledValue.setUnit(UnitEnum.Wh);
        }
        BigDecimal cuEleEnerge = new BigDecimal(sampledValue.getValue());
        if(sampledValue.getUnit() == UnitEnum.Wh) {
            cuEleEnerge = cuEleEnerge.divide(new BigDecimal(1000));
        }
        cuEleEnerge = cuEleEnerge.subtract(recharging.getStartCaEleEnerge());
        if(meterValue.getTimestamp().contains(StringConstants.DOT)) {
            chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(meterValue.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS, DateUtils.UTC));
        }else {
            chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(meterValue.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));
        }
        chargerStatusInfo.setCutCardNo(null);
        chargerStatusInfo.setInstChAmont(cuEleEnerge.subtract(recharging.getChUseAmount()));// 순간 충전량
        chargerStatusInfo.setCuEleEnerge(cuEleEnerge);
        if (chargerStatusInfo.getChStartDate() == null) {
            chargerStatusInfo.setChStartDate(recharging.getChStartDate());
        }
        chargerStatusInfo.setChEndDate(chargerStatusInfo.getInfoCollDate());//충전종료시간
        ProductPrice productPrice = priceService.retrieveProductPriceInCache(recharging.getProductId());
        Map<String, BigDecimal> priceMap = FeeCalculator.getInstance().calculate(productPrice, chargerStatusInfo.getInstChAmont());
        chargerStatusInfo.setInstChCost(priceMap.get(StringConstants.UNIT_PRICE));// 순간충전단가
        chargerStatusInfo.setInstChSum(priceMap.get(StringConstants.PRICE));// 순간충전금액
        chargerStatusInfo.setCaEleEnerge(recharging.getStartCaEleEnerge().add(cuEleEnerge));
        chargerStatusInfo.setChSum(recharging.getChUseCost().add(chargerStatusInfo.getInstChSum()));// 충전요금
        chargerStatusInfo.setUpdateDate(new Date());
        
        recharging.setChUseCost(chargerStatusInfo.getChSum());
        recharging.setChUseAmount(cuEleEnerge);
        recharging.setChEndDate(chargerStatusInfo.getInfoCollDate());
        rechargingService.modifyRecharging(recharging);
        chargerStatusInfo.setEventCode(StringConstants.BLANK);
        chargerStatusService.modifyChargerStatus(chargerStatusInfo);
        
        return new Object();
    }
    
    private SampledValue getSampledValue(List<SampledValue> data, MeasurandTypeEnum measurandType) {
        //
        for (SampledValue sampledValue : data) {
            if(sampledValue.getMeasurand() == null){
                sampledValue.setMeasurand(MeasurandTypeEnum.Energy_Active_Import_Register);
            }
            if(sampledValue.getUnit() == null){
                sampledValue.setUnit(UnitEnum.Wh);
            }
            if(sampledValue.getMeasurand() == measurandType) {
                return sampledValue;
            }
        }
        return null;
    }

}
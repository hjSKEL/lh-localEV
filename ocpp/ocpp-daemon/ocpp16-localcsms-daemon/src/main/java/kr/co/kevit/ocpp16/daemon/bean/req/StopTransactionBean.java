/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.req;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargeStatusType;
import kr.co.kevit.localcsms.common.util.enumtype.charger.RechargingStatus;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.process.ProductPriceService;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.fee.FeeCalculator;
import kr.co.kevit.localcsms.recharger.process.RechargingService;
import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;
import kr.co.kevit.ocpp16.domain.IdTagInfo;
import kr.co.kevit.ocpp16.enumtype.IdTagInfoStatus;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 26.
 */
public class StopTransactionBean  implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopTransactionBean.class);
    
    @Autowired
    private ChargerStatusService chargerStatusService;

    @Autowired
    private RechargingService rechargingService;

    @Autowired
    private ProductPriceService priceService;

    private final String EVT0J7 = "EVT0J7";
    private final String CHRS09 = "CHRS09";

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Object control(String csId, List<Object> reqs) {
        //
        String[] csIds = csId.split(StringConstants.DASH);
        
        Object object = reqs.get(3);
        Gson gson = new Gson();
        String text = gson.toJson(object);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("StopTransactionBean.control : {}", text);
        }
        
        kr.co.kevit.ocpp16.request.StopTransaction request = gson.fromJson(text,kr.co.kevit.ocpp16.request.StopTransaction.class);
        if(request.getTransactionId() == 0) {
            return new kr.co.kevit.ocpp16.response.StopTransaction();
        }
        
        String transactionId = Integer.toString(request.getTransactionId());
        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        ChargerStatusInfo chargerStatusInfo = null;
        for(ChargerStatusInfo datum : chargerStatusInfos) {
            if(!StringUtils.isEmpty(datum.getRechargingId()) && datum.getRechargingId().endsWith(transactionId)) {
                chargerStatusInfo = datum;
                break;
            }
        }
        if(chargerStatusInfo == null) {
            processWithoutStatus(csIds[0], csIds[1], request);
            return new kr.co.kevit.ocpp16.response.StopTransaction();
        }
        
        String recharingId = chargerStatusInfo.getRechargingId();
        Recharging recharging = null;
        if(!StringUtils.isEmpty(chargerStatusInfo.getRechargingId())) {
            recharging = rechargingService.retrieveRecharging4IfById(recharingId);
        }
        if(recharging == null || request.getTimestamp() == null) {
            return new kr.co.kevit.ocpp16.response.StopTransaction();
        }
        
        // 충전종료 이벤트 저장
        if (chargerStatusInfo.getChStartDate() == null) {
            chargerStatusInfo.setChStartDate(recharging.getChStartDate());
        }

        if(request.getTimestamp().contains(StringConstants.DOT)) {
            chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS, DateUtils.UTC));
        }else {
            chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));
        }
        chargerStatusInfo.setRechargingId(recharingId);
        chargerStatusInfo.setEventCode(EVT0J7);
        chargerStatusInfo.setCutCardNo(null);
        chargerStatusInfo.setCsStatCode(CHRS09);
        recharging.setEndCaEleEnerge(new BigDecimal(request.getMeterStop()).divide(new BigDecimal(1000))); //시작 시 전력량 (Wh -> kWh)
        BigDecimal fUseAmount = recharging.getEndCaEleEnerge().subtract(recharging.getStartCaEleEnerge());
        
        chargerStatusInfo.setInstChAmont(fUseAmount.subtract(recharging.getChUseAmount()));// 순간 충전량
        recharging.setChUseAmount(fUseAmount);
        chargerStatusInfo.setCuEleEnerge(fUseAmount);// 충전사용전력량
        chargerStatusInfo.setChEndDate(chargerStatusInfo.getInfoCollDate());

        ProductPrice productPrice = priceService.retrieveProductPriceInCache(recharging.getProductId());
        Map<String, BigDecimal> priceMap = FeeCalculator.getInstance().calculate(productPrice,chargerStatusInfo.getInstChAmont());
        chargerStatusInfo.setInstChCost(priceMap.get(StringConstants.UNIT_PRICE));// 순간충전단가
        chargerStatusInfo.setInstChSum(priceMap.get(StringConstants.PRICE));// 순간충전금액
        chargerStatusInfo.setCaEleEnerge(recharging.getEndCaEleEnerge());
        chargerStatusInfo.setChSum(chargerStatusInfo.getChSum().add(chargerStatusInfo.getInstChSum()));// 충전요금
        chargerStatusInfo.setUpdateDate(new Date());
        chargerStatusService.modifyChargerStatus(chargerStatusInfo);

        //충전정보 변경.
        recharging.setChUseUnitCost(chargerStatusInfo.getInstChCost());
        recharging.setChUseCost(chargerStatusInfo.getChSum());
        recharging.setChStatCode(RechargingStatus.RECS03.getCode());
        recharging.setPaySum(chargerStatusInfo.getChSum().setScale(0, RoundingMode.FLOOR).intValue());
        recharging.setChEndDate(chargerStatusInfo.getInfoCollDate());// 충전 종료 시간
        rechargingService.modifyRecharging(recharging);
        
        //충전기 상태 초기화
        chargerStatusInfo.setCsStatCode(ChargeStatusType.COMPLETE.getCode());
        chargerStatusInfo.setRechargingId(null);
        chargerStatusInfo.setInstChSum(BigDecimal.ZERO);// 순간충전금액
        chargerStatusInfo.setInstChAmont(BigDecimal.ZERO);// 순간 충전량
        chargerStatusInfo.setInstChCost(BigDecimal.ZERO);// 순간충전단가
        chargerStatusInfo.setCuEleEnerge(BigDecimal.ZERO);//상용전력량
        chargerStatusInfo.setChSum(BigDecimal.ZERO);// 충전금액
        chargerStatusInfo.setChStartDate(null);// 충전시작시간
        chargerStatusInfo.setChEndDate(null);//충전종료시간
        chargerStatusInfo.setCutCardNo(null);
        chargerStatusService.modifyChargerStatusWithoutHis(chargerStatusInfo);

        kr.co.kevit.ocpp16.response.StopTransaction response = new kr.co.kevit.ocpp16.response.StopTransaction();
        IdTagInfo idTagInfo = new IdTagInfo();
        Date curDate = DateUtils.changeDateWithDayLevel(new Date(), 7);
        idTagInfo.setExpiryDate(DateUtils.dateToString(curDate, DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
        idTagInfo.setStatus(IdTagInfoStatus.Accepted);
        response.setIdTagInfo(idTagInfo);
        return response;
    }
    
    private void processWithoutStatus(String cpId, String csId, kr.co.kevit.ocpp16.request.StopTransaction request) {
        String curDt = DateUtils.getCurrentDateAsString(DateUtils.DATE_FORMAT_WITHOUT_DASH);
        
        StringBuilder buffer = new StringBuilder(24);
        buffer.append(cpId);
        buffer.append(csId);
        buffer.append(curDt.substring(0, 4));
        buffer.append(StringUtils.leftPadding(Integer.toString(request.getTransactionId()), StringConstants.ZERO_CHAR, 9));
        String recharingId = buffer.toString();
        Recharging recharging = rechargingService.retrieveRecharging4IfById(recharingId);
        if(recharging == null || !RechargingStatus.RECS02.getCode().equals(recharging.getChStatCode())) {
            return ;
        }
        if(request.getTimestamp() == null) {
            return ;
        }
        Date endDate = null;
        if(request.getTimestamp().contains(StringConstants.DOT)) {
            endDate = DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS, DateUtils.UTC);
        }else {
            endDate = DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC);
        }
        
        recharging.setEndCaEleEnerge(new BigDecimal(request.getMeterStop()).divide(new BigDecimal(1000))); //시작 시 전력량 (Wh -> kWh)
        BigDecimal fUseAmount = recharging.getEndCaEleEnerge().subtract(recharging.getStartCaEleEnerge());
        recharging.setChUseAmount(fUseAmount);
        
        BigDecimal unitCost = null;
        BigDecimal price = null;

        ProductPrice productPrice = priceService.retrieveProductPriceInCache(recharging.getProductId());
        Map<String, BigDecimal> priceMap = FeeCalculator.getInstance().calculate(productPrice,fUseAmount);
        unitCost = priceMap.get(StringConstants.UNIT_PRICE);// 순간충전단가
        price = priceMap.get(StringConstants.PRICE);// 순간충전금액

        //충전정보 변경.
        recharging.setChUseUnitCost(unitCost);
        recharging.setChUseCost(price);
        recharging.setChStatCode(RechargingStatus.RECS03.getCode());
        recharging.setPaySum(price.setScale(0, RoundingMode.FLOOR).intValue());
        recharging.setChEndDate(endDate);// 충전 종료 시간
        rechargingService.modifyRecharging(recharging);
    }
}
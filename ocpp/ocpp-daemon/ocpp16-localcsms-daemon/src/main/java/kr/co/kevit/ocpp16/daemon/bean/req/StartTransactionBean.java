/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargeStatusType;
import kr.co.kevit.localcsms.common.util.enumtype.charger.RechargingStatus;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.process.CustomerMgtService;
import kr.co.kevit.localcsms.customer.process.CustomerService;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.process.ProductPriceService;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.process.RechargingService;
import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;
import kr.co.kevit.ocpp16.domain.IdTagInfo;
import kr.co.kevit.ocpp16.enumtype.IdTagInfoStatus;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 6. 26.
 */
public class StartTransactionBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartTransactionBean.class);
    
    @Autowired
    private ChargerStatusService chargerStatusService;
    
    @Autowired
    private ChargingStationService chargingStationService;

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private CustomerMgtService custMgtService;

    @Autowired
    private RechargingService rechargingService;
    
    @Autowired
    private ProductPriceService priceService;
    
    private final String EVT0J3 = "EVT0J3";


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
        
        kr.co.kevit.ocpp16.request.StartTransaction request = gson.fromJson(text,kr.co.kevit.ocpp16.request.StartTransaction.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("StartTransactionBean.control : {}", text);
        }

        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.get(0);
        if(chargerStatusInfos.size() > 1) {
            for(ChargerStatusInfo datum : chargerStatusInfos) {
                if(datum.getEvseId() == request.getConnectorId()) {
                    chargerStatusInfo = datum;
                    break;
                }
            }
        }
        
        kr.co.kevit.ocpp16.response.StartTransaction response = new kr.co.kevit.ocpp16.response.StartTransaction();
        IdTagInfo idTagInfo = new IdTagInfo();
        idTagInfo.setParentIdTag(StringConstants.BLANK);
        response.setIdTagInfo(idTagInfo);
        
        String custCardNo = request.getIdTag();
        Date curDt = new Date();
        CustomerMgt customerMgt = custMgtService.retrieveCustomerMgtByCustomerCardNo(custCardNo);
        if (customerMgt == null) {            
            idTagInfo.setStatus(IdTagInfoStatus.Invalid);
            idTagInfo.setExpiryDate(null);
            idTagInfo.setParentIdTag(null);
            return response;
        }
        
        idTagInfo.setStatus(IdTagInfoStatus.Accepted);
        curDt = DateUtils.changeDateWithDayLevel(curDt, 7);
        idTagInfo.setExpiryDate(DateUtils.dateToString(curDt, DateUtils.RFC3339_DEFAULT_DATE_FORMAT));

        Recharging recharging = makeNewRecharging(request, chargerStatusInfo, customerMgt);
        recharging.setStartCaEleEnerge(new BigDecimal(request.getMeterStart()).divide(new BigDecimal(1000))); //시작 시 전력량 (Wh -> kWh)
        recharging.setEndCaEleEnerge(BigDecimal.ZERO); //시작 시 전력량
        rechargingService.registerRecharging(recharging);
        
        chargerStatusInfo.setInfoCollDate(new Date());
        chargerStatusInfo.setCsCableStatus(StringConstants.ONE);
        chargerStatusInfo.setCsStatCode(ChargeStatusType.CHARGING.getCode());
        chargerStatusInfo.setCaEleEnerge(recharging.getStartCaEleEnerge()); //시작 시 전력량 (Wh -> kWh)
        chargerStatusInfo.setCuEleEnerge(BigDecimal.ZERO);
        chargerStatusInfo.setCutCardNo(request.getIdTag());
        chargerStatusInfo.setRechargingId(recharging.getRechargingId());
        chargerStatusInfo.setEventCode(EVT0J3);
        chargerStatusInfo.setInstChSum(BigDecimal.ZERO);// 순간충전금액
        chargerStatusInfo.setInstChAmont(BigDecimal.ZERO);// 순간 충전량
        chargerStatusInfo.setInstChCost(BigDecimal.ZERO);// 순간충전단가
        chargerStatusInfo.setChSum(BigDecimal.ZERO);// 충전금액
        chargerStatusInfo.setChStartDate(recharging.getChStartDate());// 충전시작시간
        chargerStatusInfo.setChEndDate(chargerStatusInfo.getChStartDate());//충전종료시간
        chargerStatusInfo.setUpdateDate(new Date());
        chargerStatusService.modifyChargerStatus(chargerStatusInfo);

        //int c = 042600001;//9
        response.setTransactionId(Integer.parseInt(recharging.getRechargingId().substring(12)));
        return response;
    }
    
    private Recharging makeNewRecharging(kr.co.kevit.ocpp16.request.StartTransaction request, ChargerStatusInfo chargerStatusInfo, CustomerMgt customerMgt) {
        //
        ChargingStation station = chargingStationService.retrieveChargingStationByCpIdNCsId(chargerStatusInfo.getCpId(), chargerStatusInfo.getCsId());

        Recharging recharging = new Recharging();
        recharging.setChStatCode(RechargingStatus.RECS02.getCode());
        recharging.setCpId(station.getCpId());
        recharging.setCsId(station.getCsId());
        recharging.setEvseId(chargerStatusInfo.getEvseId());
        if(request.getTimestamp() == null) {
            request.setTimestamp(DateUtils.dateToString(new Date(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS, DateUtils.UTC));
        }
        if(request.getTimestamp().contains(StringConstants.DOT)) {
            recharging.setChStartDate(DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS, DateUtils.UTC));
        }else {
            recharging.setChStartDate(DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));
        }
        ProductPrice product = priceService.retrieveLiveProductPriceByType(station.getProdType(), recharging.getChStartDate());
        recharging.setChEndDate(recharging.getChStartDate());
        recharging.setChUseAmount(BigDecimal.ZERO);
        recharging.setChUseCost(BigDecimal.ZERO);
        recharging.setChUseUnitCost(BigDecimal.valueOf(product.getFee()));
        recharging.setCustomerId(customerMgt.getCustomerId());
        recharging.setCutCardNo(request.getIdTag());
        recharging.setFinalPaySum(0);
        recharging.setPaySum(0);
        recharging.setProductId(product.getId());
        return recharging;
    }
}
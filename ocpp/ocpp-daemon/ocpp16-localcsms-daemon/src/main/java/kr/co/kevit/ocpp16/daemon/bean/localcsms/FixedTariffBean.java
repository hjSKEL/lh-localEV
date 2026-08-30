/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.localcsms;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.product.entity.domain.ProductPrice;
import kr.co.kevit.localcsms.product.process.ProductPriceService;
import kr.co.kevit.ocpp16.daemon.bean.DataTransferControlerBean;
import kr.co.kevit.ocpp16.request.DataTransfer;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 26.
 */
public class FixedTariffBean implements DataTransferControlerBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FixedTariffBean.class);
    
    @Autowired
    private ChargingStationService chargingStationService;
    
    @Autowired
    private ProductPriceService priceService;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object control(String csId, DataTransfer req) {
        //
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("DataTransferBean.data : {}", req.getData());
        }
        String csIds[] = csId.split("-");
        kr.co.kevit.ocpp16.localcsms.request.FixedTariff request = gson.fromJson(req.getData(),kr.co.kevit.ocpp16.localcsms.request.FixedTariff.class);
        LOGGER.debug("request.TariffBean start ... ");
        Date startDate = null;
        if(request.getTimestamp().contains(StringConstants.DOT)) {
            startDate = DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS, DateUtils.UTC);
        }else {
            startDate = DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC);
        }
        ChargingStation station = chargingStationService.retrieveChargingStationByCpIdNCsId(csIds[0], csIds[1]);
        ProductPrice price = priceService.retrieveLiveProductPriceByType(station.getProdType(), startDate);
        kr.co.kevit.ocpp16.localcsms.response.FixedTariff response = new kr.co.kevit.ocpp16.localcsms.response.FixedTariff();
        response.setConnectorId(request.getConnectorId());
        response.setIdTag(request.getIdTag());
        response.setTimestamp(request.getTimestamp());
        response.setPrice(price.getFee());
        return response;
    }

}
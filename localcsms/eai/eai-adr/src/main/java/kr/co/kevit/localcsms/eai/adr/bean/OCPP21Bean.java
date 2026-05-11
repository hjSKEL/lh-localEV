/*******************************************************************************
 * Copyright(c) 2024 IIAC All rights reserved.
 * This software is the proprietary information of IIAC.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.adr.bean;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.kevit.ocpp201.request.NotifyEVChargingNeeds;
import kr.co.kevit.localcsms.eai.vo.AdrParamVo;

/**
 * 
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 24.
 */
public class OCPP21Bean {

    private static final Logger LOGGER = LoggerFactory.getLogger(OCPP21Bean.class);

    public OCPP21Bean() {
        //
    }

    public void execute(Exchange exchange) {
        //
        LOGGER.info("OCPP21Bean Start Timer");
        
        String message = exchange.getIn().getBody(String.class);
        LOGGER.info("OCPP21Bean PARAM : {}", message);
        try {
            AdrParamVo pramVo = new Gson().fromJson(message, AdrParamVo.class);
            LOGGER.info("param1 ( CSID     ) : {}", pramVo.getParam1()); // csId
            LOGGER.info("param2 ( MSG NAME ) : {}", pramVo.getParam2()); // messageName
            LOGGER.info("param3 ( MSG      ) : {}", pramVo.getParam3()); // message
            LOGGER.info("param4 ( TX ID    ) : {}", pramVo.getParam4()); // transactionEventID
            
            //NotifyEVChargingNeedsRequest with departureTime and v2xChargingParameters
            if("NotifyEVChargingNeeds".equals(pramVo.getParam2())) {
                NotifyEVChargingNeeds evChargingNeed = new Gson().fromJson(pramVo.getParam3(), NotifyEVChargingNeeds.class);
                LOGGER.info("evseId : {} ", evChargingNeed.getEvseId());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        exchange.getMessage().setBody("{}");
        LOGGER.info("OCPP21Bean END Timer");
    }

}

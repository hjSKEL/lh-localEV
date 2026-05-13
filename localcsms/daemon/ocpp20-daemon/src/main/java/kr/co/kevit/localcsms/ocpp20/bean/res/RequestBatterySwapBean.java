/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.res;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.kevit.localcsms.ocpp20.bean.ResponderBean;

/**
 * OCPP 2.1 RequestBatterySwap (CSMS→CS) 에 대한 CALLRESULT 처리 Bean.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Component("RequestBatterySwap")
public class RequestBatterySwapBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(RequestBatterySwapBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        kr.co.kevit.ocpp201.response.RequestBatterySwap response = objectMapper.treeToValue(payload,
                kr.co.kevit.ocpp201.response.RequestBatterySwap.class);
        log.debug("RequestBatterySwapBean cpCsId={} status={}", cpCsId, response.getStatus());
    }
}

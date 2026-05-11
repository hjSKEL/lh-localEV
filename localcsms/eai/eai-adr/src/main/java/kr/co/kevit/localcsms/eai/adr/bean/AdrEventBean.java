/*******************************************************************************
 * Copyright(c) 2024 IIAC All rights reserved.
 * This software is the proprietary information of IIAC.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.adr.bean;

import org.apache.camel.Exchange;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.kevit.ocpp201.domain.ChargingScheduleUpdateType;
import kr.co.kevit.ocpp201.request.UpdateDynamicSchedule;
import kr.co.kevit.localcsms.eai.vo.adr.Event;

/**
 * 
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 24.
 */
public class AdrEventBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdrEventBean.class);

    public AdrEventBean() {
        //
        /*
{
    "id": "f81d4fae-7dec11d0-a765-00a0c91e6bf6",
    "createdDateTime": "2025-01-11T09:30:00.000Z",
    "modificationDateTime": "2025-01-11T09:30:00.000Z",
    "objectType": "EVENT",
    "programID": "program-1",
    "eventName": " program-1 2025-01-11",
    "priority": 200,
    "targets": [{
        "type": "RESOURCE_NAME",
        "values": ["412200001-03"]
    }],
    "reportDescriptors": null,
    "payloadDescriptors": [{
        "payloadType": " EXPORT_CAPACITY_LIMIT",
        "units": "AMPS"
    }],
    "intervalPeriod": {
        "start": "2025-01-11T09:30:00.000Z",
        "duration": "PT30M"
    },
    "intervals": [
        {
            "id": 0,
            "intervalPeriod": {
                "start": "2025-01-11T09:30:00.000Z",
                "duration": "PT15M"
            },
            "payloads": [
                {"type": "EXPORT_CAPACITY_LIMIT","values": [10]}
            ]
        },
        {
            "id": 1,
            "intervalPeriod": {
                "start": "2025-01-11T09:45:00.000Z",
                "duration": "PT15M"
            },
            "payloads": [
                {"type": "EXPORT_CAPACITY_LIMIT","values": [16]}
            ]
        }
    ]
}
         */
    }

    public void execute(Exchange exchange) {
        //
        LOGGER.info("AdrEventBean Start Timer");
        String authorization = (String) exchange.getIn().getHeader("Authorization");
        authorization = authorization.replace("Bearer ", "");
        LOGGER.info("Authorization : Bearer {}", authorization);
        
        String message = exchange.getIn().getBody(String.class);
        LOGGER.info("AdrEventBean PARAM : {}", message);
        try {
            Event event = new Gson().fromJson(message, Event.class);
            System.out.println(event.getId());
            
            // OCPP 2.1 UpdateDynamicScheduleRequest
//            UpdateDynamicSchedule request = new UpdateDynamicSchedule();
//            request.setChargingProfileId(1);
//            ChargingScheduleUpdateType scheduleUpdate = new ChargingScheduleUpdateType();
//            scheduleUpdate.setDischargeLimit(dischargeLimit);
//            scheduleUpdate.setDischargeLimit_L2(dischargeLimit_L2);
//            scheduleUpdate.setDischargeLimit_L3(dischargeLimit_L3);
//            scheduleUpdate.setLimit(limit);
//            scheduleUpdate.setLimit_L2(limit_L2);
//            scheduleUpdate.setLimit_L3(limit_L3);
//            scheduleUpdate.setSetpoint(setpoint);
//            scheduleUpdate.setSetpoint_L2(setpoint_L2);
//            scheduleUpdate.setSetpoint_L3(setpoint_L3);
//            scheduleUpdate.setSetPointReactive(setPointReactive);
//            scheduleUpdate.setSetPointReactive_L2(setPointReactive_L2);
//            scheduleUpdate.setSetPointReactive_L3(setPointReactive_L3);
//            request.setScheduleUpdate(scheduleUpdate);
            
//            HttpPost post = new HttpPost("http://test.kevit.co.kr:8300/ws/cmd/remoteMergeHttp");
//            post.setEntity(new StringEntity(new Gson().toJson(request), "UTF-8"));
//            post.addHeader("Content-Type", "application/json");
//            post.addHeader("Accept", "application/json");
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            CloseableHttpResponse response = httpClient.execute(post);
//            String resultStr = EntityUtils.toString(response.getEntity());
//            LOGGER.info(resultStr);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        exchange.getMessage().setBody("{}");
        LOGGER.info("AdrEventBean END Timer");
    }

}

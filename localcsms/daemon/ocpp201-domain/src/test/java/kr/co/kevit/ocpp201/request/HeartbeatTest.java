/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import org.junit.Test;

import com.google.gson.Gson;

/**
 * 
 * @author KEVIT <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 3. 14.
 */
public class HeartbeatTest {

    @Test
    public void test() {
        String json = "{}";
        Heartbeat hb = new Gson().fromJson(json, Heartbeat.class);
        System.out.println(hb);
        System.out.println(hb.getCustomData());
    }
    
    @Test
    public void test1() {
        String json = "{\"customData\":{\"vendorId\":\"KEVIT\",\"evseId\":1}}";
        Heartbeat hb = new Gson().fromJson(json, Heartbeat.class);
        System.out.println(hb);
        System.out.println(hb.getCustomData());
        System.out.println(hb.getCustomData().get("vendorId"));
        System.out.println(((Double)hb.getCustomData().get("evseId")).intValue());
    }

}

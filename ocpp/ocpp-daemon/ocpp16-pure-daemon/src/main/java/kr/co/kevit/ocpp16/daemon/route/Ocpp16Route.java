/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import kr.co.kevit.ocpp16.license.Vendor;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 4. 15.
 */
@Component
public class Ocpp16Route extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //
        from("websocket://0.0.0.0:" + Vendor.getInstance().ocppPort() + "/daemon/endpoint/*?maxIdleTime=600000" + (Vendor.getInstance().isTLS() ? "&sslContextParameters=#sslContextParameters" : ""))
        .bean("ocpp16Bean", "execute");
    }

}
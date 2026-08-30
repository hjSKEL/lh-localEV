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
public class RemoteRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //
        from("netty:tcp://0.0.0.0:" + Vendor.getInstance().remotePort() + "?sync=true&allowDefaultCodec=false&encoders=#kevitEncoders,#stringEncoder&decoders=#kevitDecoders,#stringDecoder&receiveBufferSize=512&sendBufferSize=512&disconnect=true&requestTimeout=10000&keepAlive=false&maximumPoolSize=10")
        .to("bean:remoteBean");
    }

}
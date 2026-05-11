/*******************************************************************************
 * Copyright(c) 2024 IIAC All rights reserved.
 * This software is the proprietary information of IIAC.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.adr.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 4. 24.
 */
@Component
public class ADRRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //
//        from("jetty://https://0.0.0.0:22300/adr/program?sessionSupport=false&sendServerVersion=false&sslContextParameters=#sslContextParameters").bean("adrProgramBean", "execute");
//        from("jetty://https://0.0.0.0:22300/adr/event?sessionSupport=false&sendServerVersion=false&sslContextParameters=#sslContextParameters").bean("adrEventBean", "execute");
        
        from("jetty://http://0.0.0.0:22300/adr/program?sessionSupport=false&sendServerVersion=false").bean("adrProgramBean", "execute");
        from("jetty://http://0.0.0.0:22300/adr/event?sessionSupport=false&sendServerVersion=false")  .bean("adrEventBean", "execute");
        
        from("jetty://http://0.0.0.0:22300/ieee/note?sessionSupport=false&sendServerVersion=false")  .bean("notificationBean", "execute");
        
        from("jetty://http://0.0.0.0:22300/ocpp21/msg?sessionSupport=false&sendServerVersion=false").bean("ocpp21Bean", "execute");
    }

}

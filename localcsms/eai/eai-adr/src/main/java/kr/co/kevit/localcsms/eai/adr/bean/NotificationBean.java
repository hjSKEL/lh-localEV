/*******************************************************************************
 * Copyright(c) 2024 IIAC All rights reserved.
 * This software is the proprietary information of IIAC.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.adr.bean;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.eai.adr.task.NotificationRunner;
import kr.co.kevit.localcsms.eai.adr.util.EXIUtils;
import kr.co.kevit.localcsms.eai.vo.ieee.DERCurveList;
import kr.co.kevit.localcsms.eai.vo.ieee.Notification;

/**
 * 
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 24.
 */
public class NotificationBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationBean.class);

    public NotificationBean() {
        //
        /*
         */
    }

    public void execute(Exchange exchange) {
        //
        LOGGER.info("NotificationBean Start Timer");
        try {
            String contentType = (String)exchange.getIn().getHeader(Exchange.CONTENT_TYPE);
            String method = (String)exchange.getIn().getHeader(Exchange.HTTP_METHOD);
            if(!"POST".equals(method)) {
                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
                return ;
            }
            Notification notification = null;
            boolean isXml = contentType.equals("application/sep+xml");
            if(isXml) {
                notification = processXML(exchange);
            }
            boolean isExi = contentType.equals("application/sep-exi");
            if(isExi) {
                notification = processEXI(exchange);
            }
            if(!isXml && !isExi) {
                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
                return ;
            }else {
                // When
                LOGGER.info(new Gson().toJson(notification));
                new NotificationRunner().start();
                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 201);
                exchange.getMessage().setHeader("Location", "/edev/0/sub/1");
            }
        }catch(Exception ex){
            LOGGER.error(ex.getMessage(), ex);
        }
        LOGGER.info("NotificationBean END Timer");
    }
    
    private Notification processXML(Exchange exchange) throws Exception{
        //
        String message = exchange.getIn().getBody(String.class);
        LOGGER.info("DERCurveListBean PARAM : {}", message);
        JAXBContext jaxbContext = JAXBContext.newInstance(DERCurveList.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        
        return (Notification) unmarshaller.unmarshal(new ByteArrayInputStream(message.getBytes()));
    }
    
    private Notification processEXI(Exchange exchange) throws Exception {
        //
        byte[] exiData = exchange.getIn().getBody(byte[].class);
        
        String decodedXml = EXIUtils.decodeFromEXI(exiData);
        System.out.println("Decoded XML:\n" + decodedXml);
        
        JAXBContext jaxbContext = JAXBContext.newInstance(DERCurveList.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        
        return (Notification) unmarshaller.unmarshal(new ByteArrayInputStream(decodedXml.getBytes()));
    }
}

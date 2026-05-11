/*******************************************************************************
 * Copyright(c) 2024 IIAC All rights reserved.
 * This software is the proprietary information of IIAC.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.adr.bean;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.eai.vo.adr.Program;

/**
 * 
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 24.
 */
public class AdrProgramBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdrProgramBean.class);

    public AdrProgramBean() {
        //
    }

    public void execute(Exchange exchange) {
        //
        LOGGER.info("AdrProgramBean Start Timer");
        String authorization = (String) exchange.getIn().getHeader("Authorization");
        authorization = authorization.replace("Bearer ", "");
        LOGGER.info("Authorization : Bearer {}", authorization);

        String message = exchange.getIn().getBody(String.class);
        LOGGER.info("AdrProgramBean PARAM : {}", message);
        try {
            Program program = new Gson().fromJson(message, Program.class);
            System.out.println(program.getId());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        exchange.getMessage().setBody("{}");
        LOGGER.info("AdrProgramBean END Timer");
    }

}

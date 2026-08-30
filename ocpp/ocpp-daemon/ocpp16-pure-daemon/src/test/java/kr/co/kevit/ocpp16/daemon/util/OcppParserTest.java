/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.util;

import java.util.List;

import org.junit.Test;

import kr.co.kevit.ocpp16.exception.OCPPException;

/**
 * 
 * @author KEVIT <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2024. 7. 31.
 */
public class OcppParserTest {

    @Test
    public void testRequest() throws OCPPException {
        String jsonTxt = "[2,\"20240731120809123\",\"StartTransaction\",{\"connectorId\":1,\"idTag\":\"1234567890\",\"meterStart\":300000,\"timestamp\":\"2024-07-31T03:09:00Z\"}]";
        System.out.println(jsonTxt);
        List<String> stringList = OCPPParser.parseRequest(jsonTxt);
        System.out.println(stringList.get(0));
        System.out.println(stringList.get(1));
        System.out.println(stringList.get(2));
        System.out.println(stringList.get(3));
    }
    
    @Test
    public void testResponse() throws OCPPException {
        String jsonTxt = "[3,\"20240731120809123\",{\"status\":\"Accepted\",\"transactionId\":123456789}]";
        System.out.println(jsonTxt);
        List<String> stringList = OCPPParser.parseResponse(jsonTxt);
        System.out.println(stringList.get(0));
        System.out.println(stringList.get(1));
        System.out.println(stringList.get(2));
    }

}

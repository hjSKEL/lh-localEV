/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.util;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.ocpp16.exception.OCPPErrorCode;
import kr.co.kevit.ocpp16.exception.OCPPException;

/**
 * 
 * @author KEVIT <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2024. 7. 31.
 */
public class OCPPParser {
    
    public static List<String> parseRequest(String jsonTxt) throws OCPPException{
        /**
         * [2,"104e04f59b0a422c9750d1cd61454cb6","StatusNotification",{"connectorId":1,"errorCode":"NoError","info":"","status":"Available","vendorId":"kr.co.kevit","timestamp":"2024-07-11T04:06:40Z"}]
         */
        List<String> ocppList = new ArrayList<>(4);
        try {            
            if( !jsonTxt.startsWith(StringConstants.BRACKET_D_OPEN) || !jsonTxt.endsWith(StringConstants.BRACKET_D_CLOSE)) {
                throw new OCPPException(OCPPErrorCode.FormatViolation, "Not OCPP JSON");
            }
            String reqResTag = jsonTxt.substring(1, 2);
            if(!StringConstants.TWO.equals(reqResTag)) {
                throw new OCPPException(OCPPErrorCode.FormatViolation, "Not OCPP JSON");
            }
            ocppList.add(reqResTag);
            int index0 = jsonTxt.indexOf(StringConstants.QUOTE) + 1;
            int index1 = jsonTxt.indexOf(StringConstants.QUOTE, index0);
            ocppList.add(jsonTxt.substring(index0, index1));
            index0 = jsonTxt.indexOf(StringConstants.QUOTE) + index1;
            index1 = jsonTxt.indexOf(StringConstants.QUOTE, index0);
            ocppList.add(jsonTxt.substring(index0, index1));
            index0 = jsonTxt.indexOf(StringConstants.BRACKET_M_OPEN);
            index1 = jsonTxt.lastIndexOf(StringConstants.BRACKET_M_CLOSE) + 1;
            ocppList.add(jsonTxt.substring(index0, index1));
        }catch(Exception ex) {
            throw new OCPPException(OCPPErrorCode.FormatViolation, "Not OCPP JSON");
        }
        return ocppList;
    }
    
    public static List<String> parseResponse(String jsonTxt) throws OCPPException{
        /**
         * [3,"104e04f59b0a422c9750d1cd61454cb6",{}]
         * [4,"104e04f59b0a422c9750d1cd61454cb6","Internal Error Message"]
         */
        
        List<String> ocppList = new ArrayList<>(3);
        try {            
            if( !jsonTxt.startsWith(StringConstants.BRACKET_D_OPEN) || !jsonTxt.endsWith(StringConstants.BRACKET_D_CLOSE)) {
                throw new OCPPException(OCPPErrorCode.FormatViolation, "Not OCPP JSON");
            }
            String reqResTag = jsonTxt.substring(1, 2);
            if(!(StringConstants.THREE.equals(reqResTag) || StringConstants.FOUR.equals(reqResTag))) {
                throw new OCPPException(OCPPErrorCode.FormatViolation, "Not OCPP JSON");
            }
            ocppList.add(reqResTag);
            int index0 = jsonTxt.indexOf(StringConstants.QUOTE) + 1;
            int index1 = jsonTxt.indexOf(StringConstants.QUOTE, index0);
            ocppList.add(jsonTxt.substring(index0, index1));
            if(StringConstants.THREE.equals(reqResTag)) {                
                index0 = jsonTxt.indexOf(StringConstants.BRACKET_M_OPEN);
                index1 = jsonTxt.lastIndexOf(StringConstants.BRACKET_M_CLOSE) + 1;
                ocppList.add(jsonTxt.substring(index0, index1));
            }else {
                index0 = jsonTxt.indexOf(StringConstants.QUOTE);
                index1 = jsonTxt.lastIndexOf(StringConstants.QUOTE) + 1;
                ocppList.add(jsonTxt.substring(index0, index1));
            }
        }catch(Exception ex) {
            throw new OCPPException(OCPPErrorCode.FormatViolation, "Not OCPP JSON");
        }
        return ocppList;
    }

}

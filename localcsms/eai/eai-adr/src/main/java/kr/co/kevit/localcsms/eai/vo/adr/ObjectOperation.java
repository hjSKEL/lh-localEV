/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.adr;

import java.util.List;

/**
 * 
"callbackUrl": "https://myserver.com/program_callbacks",
            "operations": ["POST","PUT"],
            "objects": ["PROGRAM"]
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
public class ObjectOperation {
    
    /**
     * 
    ": "https://myserver.com/program_callbacks",
     */
    private String callbackUrl;
    
    /**
     * 
    ": ["POST","PUT"],
     */
    private List<String> operations;
    
    /**
    ": ["PROGRAM"]
     */
    private List<String> objects;
    
    
    /**
     * 
    : User provided token.
     */
    private String bearerToken;


    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public List<String> getObjects() {
        return objects;
    }

    public void setObjects(List<String> objects) {
        this.objects = objects;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }
    
}

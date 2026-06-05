/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.api.client.vo;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2024. 4. 19.
 */
public class TokenReq {
    
    private String client_id = "tgJ3RLKf7LTWtGk8BYCuIY8P16Dp0ktC";
    
    private String client_secret = "SJIkYtLVRM6DmlsmCyUS4luOYjiFtolRbQY4MGqdbkvNiALQApLD4EgQQTPEftE1";
    
    private String audience = "https://us.plugncharge.hubject.com";
    
    private String grant_type = "client_credentials";

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }
}

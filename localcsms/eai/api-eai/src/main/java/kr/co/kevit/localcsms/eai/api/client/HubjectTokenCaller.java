/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.api.client;

import java.util.Date;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.eai.api.client.vo.TokenReq;
import kr.co.kevit.localcsms.eai.api.client.vo.TokenResp;
import kr.co.kevit.localcsms.common.util.date.DateUtils;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2024. 3. 15.
 */
public class HubjectTokenCaller {

    private static final Logger LOGGER = LoggerFactory.getLogger(HubjectTokenCaller.class);
    
    private static String URL_HEAD = "https://auth.us.plugncharge.hubject.com/";
    
    private static String TOKEN = null;
    
    private static Date expiredDate = null;
    
    public static String getToken() {
        //
        if(expiredDate == null) {            
            getLiveToken();
            return TOKEN;
        }
        if(expiredDate.compareTo(new Date()) > 0) {
            return TOKEN;
        }
        getLiveToken();
        return TOKEN;
    }
    
    public static void getLiveToken() {
        String URL = URL_HEAD + "oauth/token";
        HttpPost put = new HttpPost(URL);
        try {
            // send a JSON data
            put.addHeader("Content-Type", "application/json;charset=UTF-8");
            put.addHeader("Accept", "application/json");
            String paramJson = new Gson().toJson(new TokenReq());
            put.setEntity(new StringEntity(paramJson, "UTF-8"));
            
            CloseableHttpClient httpClient = getHttpClient(3000);
            CloseableHttpResponse response = httpClient.execute(put);
            LOGGER.info("Status Code : {} ==  200 성공.", response.getStatusLine().getStatusCode());
            String resultStr = EntityUtils.toString(response.getEntity());
            LOGGER.info("RESULT : {}", resultStr);
            TokenResp tokenResp = new Gson().fromJson(resultStr, TokenResp.class);
            if(tokenResp != null && !StringUtils.isEmpty(tokenResp.getAccess_token())) {
                TOKEN = "Bearer " + tokenResp.getAccess_token();
                expiredDate = DateUtils.changeDateWithMinuteLevel(new Date(),tokenResp.getExpires_in() / 60);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    private static CloseableHttpClient getHttpClient(int timeout) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).setConnectTimeout(timeout).build();
        HttpClientBuilder httpClientBuilder = HttpClients.custom().setDefaultRequestConfig(requestConfig);
        return httpClientBuilder.build();
    }
}

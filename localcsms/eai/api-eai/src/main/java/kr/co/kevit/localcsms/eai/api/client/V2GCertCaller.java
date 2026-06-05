/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.api.client;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2024. 3. 15.
 */
public class V2GCertCaller {

    private static final Logger LOGGER = LoggerFactory.getLogger(V2GCertCaller.class);
    
    private static String URL_HEAD = "https://us.plugncharge.hubject.com/";
    
    public static String getV2GCSData(String csr) {
        String URL = URL_HEAD + "well-known/cpo/simpleenroll";
        HttpPost put = new HttpPost(URL);
        try {
            // send a JSON data
            put.addHeader("Content-Type", "application/json;charset=UTF-8");
            put.addHeader("Accept", "application/json");
            put.addHeader("Authorization", HubjectTokenCaller.getToken());
            put.setEntity(new StringEntity(csr, "UTF-8"));
            
            CloseableHttpClient httpClient = getHttpClient(3000);
            CloseableHttpResponse response = httpClient.execute(put);
            LOGGER.info("Status Code : {} ==  200 성공.", response.getStatusLine().getStatusCode());
            String resultStr = EntityUtils.toString(response.getEntity());
            LOGGER.info("RESULT : {}", resultStr);
            return resultStr;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
    
    public static String getV2GCACert() {
        String URL = URL_HEAD + "well-known/cpo/cacerts";
        HttpGet get = new HttpGet(URL);
        try {
            get.addHeader("Authorization", HubjectTokenCaller.getToken());
            
            CloseableHttpClient httpClient = getHttpClient(3000);
            CloseableHttpResponse response = httpClient.execute(get);
            LOGGER.info("Status Code : {} ==  200 성공.", response.getStatusLine().getStatusCode());
            String resultStr = EntityUtils.toString(response.getEntity());
            LOGGER.info("RESULT : {}", resultStr);
            return resultStr;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
    
    private static CloseableHttpClient getHttpClient(int timeout) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).setConnectTimeout(timeout).build();
        HttpClientBuilder httpClientBuilder = HttpClients.custom().setDefaultRequestConfig(requestConfig);
        return httpClientBuilder.build();
    }
}

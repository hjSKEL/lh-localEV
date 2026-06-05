/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.api.client;

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

import com.google.gson.Gson;

import kr.co.kevit.localcsms.eai.api.client.vo.ContractCertReq;
import kr.co.kevit.localcsms.eai.api.client.vo.ContractCertRes;
import kr.co.kevit.localcsms.eai.api.client.vo.SignContractCert;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2024. 3. 15.
 */
public class ContractCertCaller {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContractCertCaller.class);
    
    private static String URL_HEAD = "https://us.plugncharge.hubject.com/";
    
    public static SignContractCert getSignedContractData(ContractCertReq data) {
        String URL = URL_HEAD + "v1/ccp/signedContractData";
        HttpPost put = new HttpPost(URL);
        try {
            // send a JSON data
            put.addHeader("Content-Type", "application/json;charset=UTF-8");
            put.addHeader("Accept", "application/json");
            put.addHeader("Authorization", HubjectTokenCaller.getToken());
            put.setEntity(new StringEntity(new Gson().toJson(data), "UTF-8"));
            
            CloseableHttpClient httpClient = getHttpClient(3000);
            CloseableHttpResponse response = httpClient.execute(put);
            LOGGER.info("Status Code : {} ==  200 성공.", response.getStatusLine().getStatusCode());
            String resultStr = EntityUtils.toString(response.getEntity());
            LOGGER.info("RESULT : {}", resultStr);
            return new Gson().fromJson(resultStr, ContractCertRes.class).getCCPResponse();
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

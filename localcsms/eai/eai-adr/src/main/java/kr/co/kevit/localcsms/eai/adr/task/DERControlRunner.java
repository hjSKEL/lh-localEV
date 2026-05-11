/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.adr.task;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.eai.adr.store.DERStore;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.ocpp201.request.SetDERControl;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2025. 1. 12.
 */
public class DERControlRunner extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(DERControlRunner.class);

    @Override
    public void run() {
        LOGGER.info("DERControlBean START");
        waiting(5000);
        do {
            if("START".equals(DERStore.der_status)) {
                sendMessage();
            }
        } while (waiting(10 * 1000));
        LOGGER.info("DERControlBean END");
    }

    private void sendMessage() {
        //
        DERStore.der_status = "ING";
        
        try {
            LOGGER.info("DERControlBean sendMessage");
            // OCPP 2.1 SetDERControl
            SetDERControl request = DERStore.getInstance().getSetDERControl();
            StringBuilder builder = new StringBuilder(255);
            builder.append("412200001-03");
            builder.append(StringConstants.SEMICOLON);
            builder.append("SetDERControl");
            builder.append(StringConstants.SEMICOLON);
            builder.append(new Gson().toJson(request));
            
            HttpPost post = new HttpPost("http://test.kevit.co.kr:8300/ws/cmd/remoteMergeHttp");
            post.setEntity(new StringEntity(builder.toString(), "UTF-8"));
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Accept", "application/json");
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);
            String resultStr = EntityUtils.toString(response.getEntity());
            LOGGER.info(resultStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean waiting(long millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}

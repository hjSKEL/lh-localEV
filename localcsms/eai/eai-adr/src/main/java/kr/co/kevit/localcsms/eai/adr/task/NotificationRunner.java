/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.adr.task;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.eai.vo.ieee.DERControl;
import kr.co.kevit.localcsms.eai.vo.ieee.DERControlList;
import kr.co.kevit.localcsms.eai.vo.ieee.DERCurveList;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2025. 1. 12.
 */
public class NotificationRunner extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationRunner.class);

    @Override
    public void run() {
        LOGGER.info("NotificationRunner START");
        waiting(5000);
        String href = "/derp/0/derc?s=0&l=1";
        DERControlList derControlList = getDERControlList(href);
        LOGGER.info(new Gson().toJson(derControlList));
        DERControl derControl = derControlList.getDerControls().get(0);
        String derCurveHref = derControl.getDerControlBase().getOpModVoltWatt().getHref();
        waiting(5000);
        DERCurveList derCurveList = getDERCurveList(derCurveHref);
        LOGGER.info(new Gson().toJson(derCurveList));
        LOGGER.info("NotificationRunner END");
    }

    private DERControlList getDERControlList(String href) {
        //
        try {
            LOGGER.info("getDERControlList");
            
            HttpGet post = new HttpGet("https://test" + href);
            post.addHeader("Accept", "application/sep+xml; level=+S1");
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);
            String resultStr = EntityUtils.toString(response.getEntity());
            LOGGER.info(resultStr);
            
            JAXBContext jaxbContext = JAXBContext.newInstance(DERControlList.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            // When
            return (DERControlList) unmarshaller.unmarshal(new ByteArrayInputStream(resultStr.getBytes()));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    private DERCurveList getDERCurveList(String derCurveHref) {
        //
        try {
            LOGGER.info("getDERCurveList");
            
            HttpGet post = new HttpGet("https://test" + derCurveHref);
            post.addHeader("Accept", "application/sep+xml; level=+S1");
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);
            String resultStr = EntityUtils.toString(response.getEntity());
            LOGGER.info(resultStr);
            
            JAXBContext jaxbContext = JAXBContext.newInstance(DERControlList.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            // When
            return (DERCurveList) unmarshaller.unmarshal(new ByteArrayInputStream(resultStr.getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
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

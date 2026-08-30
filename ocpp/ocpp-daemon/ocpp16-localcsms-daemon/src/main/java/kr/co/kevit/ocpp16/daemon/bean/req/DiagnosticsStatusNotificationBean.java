/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.req;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 6. 28.
 */
public class DiagnosticsStatusNotificationBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosticsStatusNotificationBean.class);

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Object control(String csId, List<Object> reqs) {
        //
        // String[] csIds = csId.split(StringConstants.DASH);

        if (LOGGER.isDebugEnabled()) {
            Object object = reqs.get(3);
            Gson gson = new Gson();
            String text = gson.toJson(object);
            kr.co.kevit.ocpp16.request.DiagnosticsStatusNotification request = gson.fromJson(text,kr.co.kevit.ocpp16.request.DiagnosticsStatusNotification.class);
            LOGGER.debug("DiagnosticsStatusNotificationBean text : {}", text);
            LOGGER.debug("getStatus : {}", request.getStatus());
        }
        return new Object();
    }
}
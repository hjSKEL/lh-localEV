/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package org.apache.camel.component.websocket;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.kevit.ocpp16.daemon.BeanStore;
import kr.co.kevit.ocpp16.daemon.SecurityProfile;
import kr.co.kevit.ocpp16.daemon.bean.BasicAuthBean;
import kr.co.kevit.ocpp16.daemon.util.StringConstants;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 4. 15.
 */
public class DefaultWebsocketFactory implements WebSocketFactory {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebsocketFactory.class);

    @Override
    public WebSocket newInstance(HttpServletRequest request, String protocol, NodeSynchronization sync, WebsocketConsumer consumer) {
        String subprotocol = request.getHeader("Sec-WebSocket-Protocol");
        LOGGER.debug("subprotocol : {}", subprotocol);
        if(!"ocpp1.6".equals(subprotocol)) {
            LOGGER.error("Not registered id : {}", subprotocol);
            return null;
        }
        String authorization = request.getHeader("Authorization");
        LOGGER.info("Authorization : {}", authorization);
        if(StringConstants.ONE.equals(SecurityProfile.LEVEL) || StringConstants.TWO.equals(SecurityProfile.LEVEL)) {            
//            if(!StringUtils.isEmpty(authorization)) {
//                byte [] authBt = Base64.decodeBase64(authorization.substring(6).getBytes());
//                BasicAuthBean authBean = (BasicAuthBean)BeanStore.getInstance().getBean("BasicAuthBean");
//                if(!authBean.isSuccessed(new String(authBt))) {
//                    return null;
//                }
//            }else {
//                return null;
//            }
        }
        LOGGER.debug("DONE");
        String url = request.getPathInfo();
        String csUniqId = url.substring(1);
        LOGGER.info("[ACCESS_INFO] CS_UNIQ_ID : {}, ACCESS_IP : {}", csUniqId, getRemoteAddr(request));
        return new Ocpp16DefaultWebsocket(csUniqId, sync, consumer);
    }
    private String getRemoteAddr(HttpServletRequest request){
        return (null != request.getHeader("X-FORWARDED-FOR")) ? request.getHeader("X-FORWARDED-FOR") : request.getRemoteAddr();
    }
}
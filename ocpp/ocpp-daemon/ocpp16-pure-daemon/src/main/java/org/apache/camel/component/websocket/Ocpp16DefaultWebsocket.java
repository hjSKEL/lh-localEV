/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package org.apache.camel.component.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.kevit.ocpp16.daemon.BeanStore;
import kr.co.kevit.ocpp16.daemon.bean.base.AccessBean;
import kr.co.kevit.ocpp16.daemon.store.SessionStore;
import kr.co.kevit.ocpp16.license.Vendor;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 4. 15.
 */
public class Ocpp16DefaultWebsocket extends DefaultWebsocket {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(Ocpp16DefaultWebsocket.class);

    private final WebsocketConsumer consumer;
    private final NodeSynchronization sync;
    private Connection connection;
    private String connectionKey;
    
    private String csUniqId;

    public Ocpp16DefaultWebsocket(String csUniqId, NodeSynchronization sync, WebsocketConsumer consumer) {
        super(sync, consumer);
        this.csUniqId = csUniqId;
        this.sync = sync;
        this.consumer = consumer;
    }

    @Override
    public void onClose(int closeCode, String message) {
        LOG.trace("onClose {} {}", closeCode, message);
        LOG.error("[TXT][{}] : onClose 1 {} {}",this.connectionKey, closeCode, message);
        sync.removeSocket(this);
    }

    @Override
    public void onOpen(Connection connection) {
        LOG.trace("onOpen {}", connection);
        LOG.info("CSID : {} , Protocol : {}, onOpen : {}",csUniqId, connection.getProtocol(), connection);
//        Connection oldconn = (Connection)SessionStore.getInstance().getSession(csUniqId);
//        if(oldconn != null) {
//            try {oldconn.close();}catch(Exception ex) {}
//        }
        SessionStore.getInstance().setSession(csUniqId, connection);
        this.connection = connection;
        this.connectionKey = csUniqId;
        sync.addSocket(this);
        AccessBean accessBean = (AccessBean) BeanStore.getInstance().getBean(AccessBean.class.getSimpleName());
        accessBean.registerAccessInfo(csUniqId, Vendor.getInstance().getIp(), Vendor.getInstance().remotePort());
    }

    @Override
    public void onMessage(String message) {
        LOG.debug("onMessage: {}", message);
        if (this.consumer != null) {
            this.consumer.sendMessage(this.connectionKey, message);
        } else {
            LOG.debug("No consumer to handle message received: {}", message);
        }
    }


    @Override
    public void onMessage(byte[] data, int offset, int length) {
        LOG.debug("onMessage: byte[]");
        if (this.consumer != null) {
            byte[] message = new byte[length];
            System.arraycopy(data, offset, message, 0, length);
            this.consumer.sendMessage(this.connectionKey, message);
        } else {
            LOG.debug("No consumer to handle message received: byte[]");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getConnectionKey() {
        return connectionKey;
    }

    public void setConnectionKey(String connectionKey) {
        this.connectionKey = connectionKey;
    }
}
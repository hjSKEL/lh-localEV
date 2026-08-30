/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.base;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 7. 31.
 */
public interface OcppLogBean {
    
    void requestLog(String id, String actionName, String ids, String connectorId, String text);
    
    void responseLog(String id, String messageTypeId, String actionName, String ids, String connectorId, String text);

}

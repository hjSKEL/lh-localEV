/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.base;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2022. 12. 7.
 */
public interface AccessBean{
    
    /**
     * 
     * @param csUniqId
     * @param ip
     * @param remotePort
     */
    void registerAccessInfo(String csUniqId, String ip, String remotePort);

}
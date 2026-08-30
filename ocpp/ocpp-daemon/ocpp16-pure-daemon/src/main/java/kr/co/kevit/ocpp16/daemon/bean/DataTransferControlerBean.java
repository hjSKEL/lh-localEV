/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean;

import kr.co.kevit.ocpp16.request.DataTransfer;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 26.
 */
public interface DataTransferControlerBean {
    
    Object control(String csId, DataTransfer req);

}

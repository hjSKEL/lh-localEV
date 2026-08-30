/*******************************************************************************
 * Copyright(c) 2018 Charge All rights reserved.
 * This software is the proprietary information of Charge.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean;

import java.util.List;

/**
 * 
 * @author blue7928 <a href="mailto:blue7928@gmail.com">blue7928@gmail.com</a> 
 * @since 2019. 2. 9.
 */
public interface ControlerBean {
    
    Object control(String csId, List<Object> reqs);

}

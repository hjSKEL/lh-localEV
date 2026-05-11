/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

import java.io.Serializable;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 10.
 */
public class CarModelSearchCond extends PageCriteria implements Serializable{

    /**  */
    private static final long serialVersionUID = -9127168428907444145L;
    
    /**
     * 차량이름.
     * CAR_NM     VARCHAR(100 BYTE)                 NOT NULL,
     * 
     */
    private String carName;
    
    /**
     * 연결코드
     * LINK_CD      CHAR(6 BYTE)                   NOT NULL,
     */
    private String linkCode;

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }
    
}

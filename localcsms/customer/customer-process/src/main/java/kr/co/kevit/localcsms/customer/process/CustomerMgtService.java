/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.process;

import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 8. 20.
 */
public interface CustomerMgtService {
    
    void modifyCustomerMgt(CustomerMgt customerMgt);

    /**
     * 고객등급(CUT_GRD_CD)만 변경한다. 다른 고객관리 정보는 보존된다.
     */
    void modifyCustomerGrade(String customerId, String cutGrdCode);

    /**
     * 정지여부(STOP_YN)만 변경한다. 정지(Y)면 정지일을 현재시각으로, 해제(N)면 정지일을 비운다.
     */
    void modifyCustomerStopYn(String customerId, String stopYn);

    CustomerMgt retrieveCustomerMgtByCustomerCardNo(String customerCardNo);
}

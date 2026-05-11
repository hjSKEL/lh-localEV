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

    CustomerMgt retrieveCustomerMgtByCustomerCardNo(String customerCardNo);
}

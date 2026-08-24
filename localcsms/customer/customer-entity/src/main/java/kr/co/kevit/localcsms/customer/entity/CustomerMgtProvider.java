/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity;

import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 8. 20.
 */
public interface CustomerMgtProvider {
    
    
    /**
     * for IF에서 조회.
     * @param customerCardNo
     * @return
     */
    CustomerMgt retrieveCustomerMgtByCustomerCardNo(String customerCardNo);

    CustomerMgt retrieveCustomerMgtByCustomerId(String customerId);

    boolean modifyCustomerMgt(CustomerMgt customerMgt);

    void registerCustomerMgt(CustomerMgt customerMgt);

}

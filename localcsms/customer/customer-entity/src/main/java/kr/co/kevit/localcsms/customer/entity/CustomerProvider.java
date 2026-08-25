/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerSearchCond;

/**
 * 
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */
public interface CustomerProvider {

    void registerCustomer(Customer customer);

    void modifyCustomer(Customer customer);

    CustomerDto retrieveCustomer(String customerId);
    
    Customer retrieveCustomerByCustomerId(String customerId);

    Customer retrieveCustomerByCustomerCardNo(String customerCardNo);

    Page<CustomerDto> retrieveCustomerDtoByCustomerSearchCond(CustomerSearchCond searchCond);

    CustomerMgt retrieveCustomerMgtByCustomerId(String customerId);

    void removeCustomer(String customerId);
}

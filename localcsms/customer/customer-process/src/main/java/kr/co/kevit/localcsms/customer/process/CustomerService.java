/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.process;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerSearchCond;

/**
 * 
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 11. 13.
 */
public interface CustomerService {
    
    /**
     * for IF에서 조회.
     * @param userId
     * @return
     */
    Customer retrieveCustomerByUserId(String userId);
    
    void registerCustomer(CustomerDto customerDto);
    
    void modifyCustomer(Customer customer);
    
    CustomerDto retrieveCustomer(String userId);
    
    Customer retrieveCustomerByCustomerCardNo(String customerCardNo);
    
    CustomerMgt retrieveCustomerMgtByCustomerId(String customerId);
    
    Page<CustomerDto> retrieveCustomerDtoByCustomerSearchCond(CustomerSearchCond searchCond);

    boolean isReadyCardMappingCustomer(String customerId);

    void modifyCustomerAndCard(CustomerDto customer);

    CustomerDto retrieveCustomerWithCard(String customerId);

    Page<CustomerDto> retrieveCustomerBySearchCond(CustomerSearchCond searchCond);

    void removeCustomer(String customerId);

    CustomerDto retrieveCustomerByCustomer(CustomerDto customer);
}

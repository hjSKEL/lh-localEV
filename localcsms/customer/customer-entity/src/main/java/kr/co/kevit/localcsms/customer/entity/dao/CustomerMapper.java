/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.dao;

import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerSearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @sinc               e 2018. 8. 31.
 */
@Repository
public interface CustomerMapper {

    int insertCustomer(@Param("customer") Customer customer);

    int updateCustomer(@Param("customer") Customer customer);

    CustomerDto selectCustomer(@Param("customerId") String customerId);

    Customer selectCustomerByCustomerId(@Param("customerId") String customerId);

    Customer selectCustomerByCustomerCardNo(@Param("customerCardNo") String customerCardNo);

    int countCustomerDtoByCustomerSearchCond(@Param("searchCond") CustomerSearchCond searchCond);

    List<CustomerDto> selectCustomerDtoByCustomerSearchCond(@Param("searchCond") CustomerSearchCond searchCond);

    String selectMaxCustomerId();

    int deleteCustomer(@Param("customerId") String customerId);
}

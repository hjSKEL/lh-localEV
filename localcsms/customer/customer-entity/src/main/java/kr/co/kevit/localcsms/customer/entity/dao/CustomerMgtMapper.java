/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2020. 8. 20.
 */
@Repository
public interface CustomerMgtMapper {

    int insertCustomerMgt(@Param("customerMgt") CustomerMgt customerMgt);

    int updateCustomerMgt(@Param("customerMgt") CustomerMgt customerMgt);

    CustomerMgt selectCustomerMgtByCustomerCardNo(@Param("customerCardNo") String customerCardNo);

    CustomerMgt selectCustomerMgtByCustomerId(@Param("customerId") String customerId);

    int deleteCustomerMgt(@Param("customerId") String customerId);
}

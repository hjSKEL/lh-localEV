/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.logic;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.customer.entity.CustomerMgtProvider;
import kr.co.kevit.localcsms.customer.entity.dao.CustomerMgtMapper;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 8. 20.
 */
@Component
public class CustomerMgtProviderImpl implements CustomerMgtProvider{
    
    @Autowired
    private CustomerMgtMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean modifyCustomerMgt(CustomerMgt customerMgt) {
        // 
        customerMgt.setUpdateDate(new Date());
        int result = mapper.updateCustomerMgt(customerMgt);
        return result == 1;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CustomerMgt retrieveCustomerMgtByCustomerCardNo(String customerCardNo) {
        //
        return mapper.selectCustomerMgtByCustomerCardNo(customerCardNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomerMgt retrieveCustomerMgtByCustomerId(String customerId) {
        //
        return mapper.selectCustomerMgtByCustomerId(customerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCustomerMgt(CustomerMgt customerMgt) {
        //
        mapper.insertCustomerMgt(customerMgt);
    }

}

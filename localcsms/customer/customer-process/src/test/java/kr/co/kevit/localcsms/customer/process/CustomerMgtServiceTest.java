/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.process;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 13.
 */
public class CustomerMgtServiceTest extends AbstractTestCase {
    
    @Autowired
    private CustomerMgtService service;

    @Test
    public void testModifyCustomerMgt() {
        CustomerMgt customerMgt = new CustomerMgt();
        customerMgt.setCustomerId("C00000001");
        customerMgt.setCutCardNo("1234123412341234");
        customerMgt.setStopDate(new Date());
        customerMgt.setStopYn(StringConstants.Y);
        customerMgt.setRegCertDate(new Date());
        customerMgt.setUpdateDate(new Date());
        service.modifyCustomerMgt(customerMgt);
    }
    
    @Test
    public void testRetrieveCustomerMgtByCustomerCardNo() {
        //
        String customerCardNo = "1234123412341234";
        CustomerMgt custmerMgt = service.retrieveCustomerMgtByCustomerCardNo(customerCardNo);
        assertNotNull(custmerMgt);
    }
    
}

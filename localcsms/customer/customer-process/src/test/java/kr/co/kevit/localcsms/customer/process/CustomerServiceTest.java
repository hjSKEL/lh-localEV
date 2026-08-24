/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.process;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 13.
 */
public class CustomerServiceTest extends AbstractTestCase {
    
    @Autowired
    private CustomerService service;

    private CustomerDto registerCustomer() {
        //
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId("C00000002");
        customerDto.setCustName("홍길동");
        customerDto.setCompanyId("CO0000000");
        customerDto.setMblPhoneNo("01012341234");
        //customerDto.setEmail(email);
        Writer writer = new Writer("E00000001");
        customerDto.setWriter(writer);
        service.registerCustomer(customerDto);
        return customerDto;
    }
    
    @Test
    public void testRegisterCustomer() {
        //
        CustomerDto customerDto = registerCustomer();
        assertNotNull(customerDto);
    }
    
    @Test
    public void testModifyCustomer() {
        //
        CustomerDto customerDto = registerCustomer();
        service.modifyCustomer(customerDto);
    }
    
    @Test
    public void testRetrieveCustomer() {
        //
        CustomerDto old = registerCustomer();
        CustomerDto newCustomer = service.retrieveCustomer(old.getCustomerId());
        assertNotNull(newCustomer);
    }
    
    @Test
    public void testRetrieveCustomerByUserId() {
        //
        CustomerDto customerDto = registerCustomer();
        CustomerMgt newCustomer = service.retrieveCustomerMgtByCustomerId(customerDto.getCustomerId());
        assertNotNull(newCustomer);
    }
    
    @Test
    public void testRetrieveCustomerByUserId2() {
        //
        CustomerDto customerDto = registerCustomer();
        Customer newCustomer = service.retrieveCustomerByUserId(customerDto.getCustomerId());
        assertNotNull(newCustomer);
    }
    
    @Test
    public void testRetrieveCustomerByCustomerCardNo() {
        //
        String cardNo = "1234123412341234";
        Customer newCustomer = service.retrieveCustomerByCustomerCardNo(cardNo);
        assertNotNull(newCustomer);
    }
    
    @Test
    public void testRetrieveCustomerMgtByCustomerId() {
        //
        CustomerDto customerDto = registerCustomer();
        CustomerMgt newCustomer = service.retrieveCustomerMgtByCustomerId(customerDto.getCustomerId());
        assertNotNull(newCustomer);
    }
    
    @Test
    public void testRetrieveCustomerDtoByCustomerSearchCond() {
        //
        CustomerDto customerDto = registerCustomer();
        CustomerSearchCond searchCond = new CustomerSearchCond();
        Page<CustomerDto> resultSet = service.retrieveCustomerDtoByCustomerSearchCond(searchCond);
        assertTrue(resultSet.getCriteria().getTotalItemCount() > 0);
    }
    
    @Test
    public void testRetrieveCustomerBySearchCond() {
        //
        CustomerDto customerDto = registerCustomer();
        CustomerSearchCond searchCond = new CustomerSearchCond();
        Page<CustomerDto> resultSet = service.retrieveCustomerBySearchCond(searchCond);
        assertTrue(resultSet.getCriteria().getTotalItemCount() > 0);
    }
    
    @Test
    public void testRemoveCustomer() {
        //
        CustomerDto customerDto = registerCustomer();
        service.removeCustomer(customerDto.getCustomerId());
    }
    
    @Test
    public void testIsReadyCardMappingCustomer() {
        //
        CustomerDto customerDto = registerCustomer();
        boolean result = service.isReadyCardMappingCustomer(customerDto.getCustomerId());
        assertTrue(result);
    }
    
    @Test
    public void testModifyCustomerAndCard() {
        //
        CustomerDto customerDto = registerCustomer();
        CustomerDto customer = new CustomerDto();
        service.modifyCustomerAndCard(customer);
    }
    
    @Test
    public void testRetrieveCustomerWithCard() {
        //
        CustomerDto customerDto = registerCustomer();
        CustomerDto newDto = service.retrieveCustomerWithCard(customerDto.getCustomerId());
        assertNotNull(newDto);
    }

}

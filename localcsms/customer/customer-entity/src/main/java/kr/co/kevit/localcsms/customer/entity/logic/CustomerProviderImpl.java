/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.customer.entity.CustomerProvider;
import kr.co.kevit.localcsms.customer.entity.dao.CustomerCardMapper;
import kr.co.kevit.localcsms.customer.entity.dao.CustomerMapper;
import kr.co.kevit.localcsms.customer.entity.dao.CustomerMgtMapper;
import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerCard;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerSearchCond;

/**
 * 
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */
@Component
public class CustomerProviderImpl implements CustomerProvider {

    @Autowired
    private CustomerMapper mapper;

    @Autowired
    private CustomerMgtMapper cuMgtMapper;
    
    @Autowired
    private CustomerCardMapper cardMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCustomer(Customer customer) {
        //
        String maxCustomerId = mapper.selectMaxCustomerId();
        customer.makeCustomerId(maxCustomerId);
        mapper.insertCustomer(customer);
        CustomerMgt customerMgt = customer.getCustomerMgt();
        customerMgt.setCustomerId(customer.getCustomerId());
        customerMgt.setRegistrationDate(customer.getWriter().getRegistrationDate());
        customerMgt.setUpdateDate(customerMgt.getRegistrationDate());
        customerMgt.setRegCertDate(customerMgt.getRegistrationDate());
        customerMgt.setStopYn(StringConstants.N);
        cuMgtMapper.insertCustomerMgt(customerMgt);
        CustomerCard memberCard = new CustomerCard();
        memberCard.setCustomerId(customer.getCustomerId());
        memberCard.setCutCardNo(customerMgt.getCutCardNo());
        memberCard.setCustStatCode("MEML01");
        memberCard.setWriter(customer.getWriter());
        cardMapper.insertMemberCard(memberCard);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyCustomer(Customer customer) {
        //
        mapper.updateCustomer(customer);
        CustomerMgt oldCustomerMgt = cuMgtMapper.selectCustomerMgtByCustomerId(customer.getCustomerId());
        CustomerMgt customerMgt = customer.getCustomerMgt();
        if(!oldCustomerMgt.getCutCardNo().equals(customerMgt.getCutCardNo())) {
            CustomerCard oldCard = cardMapper.selectMemberCard(oldCustomerMgt.getCutCardNo());
            if(oldCard != null && "MEML01".equals(oldCard.getCustStatCode())) {
                oldCard.setCustStatCode("MEML03");
                oldCard.setDeleteId(customer.getWriter().getUpdUserId());
                oldCard.setDelDate(customer.getWriter().getUpdateDate());
                oldCard.setWriter(customer.getWriter());
                cardMapper.updateMemberCard(oldCard);
            }
            cuMgtMapper.deleteCustomerMgt(customer.getCustomerId());
            customerMgt.setCustomerId(customer.getCustomerId());
            customerMgt.setRegistrationDate(customer.getWriter().getRegistrationDate());
            customerMgt.setUpdateDate(customerMgt.getRegistrationDate());
            customerMgt.setRegCertDate(customerMgt.getRegistrationDate());
            customerMgt.setStopYn(StringConstants.N);
            cuMgtMapper.insertCustomerMgt(customerMgt);
            CustomerCard memberCard = new CustomerCard();
            memberCard.setCustomerId(customer.getCustomerId());
            memberCard.setCutCardNo(customerMgt.getCutCardNo());
            memberCard.setCustStatCode("MEML01");
            memberCard.setWriter(customer.getWriter());
            cardMapper.insertMemberCard(memberCard);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomerDto retrieveCustomer(String customerId) {
        //
        CustomerDto result = mapper.selectCustomer(customerId);
        if(result != null) {
            result.setCustomerMgt(cuMgtMapper.selectCustomerMgtByCustomerId(customerId));
            result.setCustomerCard(cardMapper.selectMemberCard(result.getCustomerMgt().getCutCardNo()));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer retrieveCustomerByCustomerCardNo(String customerCardNo) {
        //
        return mapper.selectCustomerByCustomerCardNo(customerCardNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CustomerDto> retrieveCustomerDtoByCustomerSearchCond(CustomerSearchCond searchCond) {
        //
        int totalItemCount = mapper.countCustomerDtoByCustomerSearchCond(searchCond);
        Page<CustomerDto> resultSet = new Page<>();
        searchCond.setTotalItemCount(totalItemCount);
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0) {
            resultSet.setResult(new ArrayList<>(0));
        } else {
            resultSet.setResult(mapper.selectCustomerDtoByCustomerSearchCond(searchCond));
        }
        return resultSet;
    }

    @Override
    public CustomerMgt retrieveCustomerMgtByCustomerId(String customerId) {
        //
        return cuMgtMapper.selectCustomerMgtByCustomerId(customerId);
    }

    @Override
    public List<Customer> retrieveCustomerByCompanyId(String companyId) {
        //
        return mapper.selectCustomerByCompanyId(companyId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer retrieveCustomerByCustomerId(String userId) {
        //
        return mapper.selectCustomerByCustomerId(userId);
    }

    @Override
    public void removeCustomer(String customerId) {
        //
        mapper.deleteCustomer(customerId);
        cuMgtMapper.deleteCustomerMgt(customerId);
    }
}

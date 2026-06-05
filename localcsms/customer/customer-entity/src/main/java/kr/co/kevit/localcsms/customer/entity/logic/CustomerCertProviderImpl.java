/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.customer.entity.CustomerCertProvider;
import kr.co.kevit.localcsms.customer.entity.dao.CustomerCertMapper;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerCert;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCertSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 3. 18.
 */
@Component
public class CustomerCertProviderImpl implements CustomerCertProvider {
    
    @Autowired
    private CustomerCertMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCustomerCert(CustomerCert cert) {
        // 
        mapper.insertCustomerCert(cert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyCustomerCert(CustomerCert cert) {
        // 
        mapper.updateCustomerCert(cert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCustomerCert(String emaid) {
        // 
        mapper.deleteCustomerCert(emaid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CustomerCert> retrieveCustomerCert(String customerId) {
        //
        return mapper.selectCustomerCert(customerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CustomerCert> retrieveCustomerCertByPcid(String pcid) {
        //
        return mapper.selectCustomerCertByPcid(pcid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomerCert retrieveCustomerCertByEmaid(String emaid) {
        // 
        return mapper.selectCustomerCertByEmaid(emaid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CustomerCert> retrieveCustomerCertBySearchCond(CustomerCertSearchCond searchCond) {
        // 
        Page<CustomerCert> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(mapper.countCustomerCertBySearchCond(searchCond));
        if(searchCond.getTotalItemCount() > 0) {
            resultSet.setResult(mapper.selectCustomerCertBySearchCond(searchCond));
        }else {
            resultSet.setResult(new ArrayList<>(0));
        }
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeCustomerCert(CustomerCert cert) {
        // 
        int result = mapper.updateCustomerCert(cert);
        if(result == 0) {
            mapper.insertCustomerCert(cert);
        }
    }

}

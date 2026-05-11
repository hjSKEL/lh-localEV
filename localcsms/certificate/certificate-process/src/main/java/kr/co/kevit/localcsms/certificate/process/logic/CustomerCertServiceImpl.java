/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.certificate.process.logic;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.certificate.entity.CustomerCertProvider;
import kr.co.kevit.localcsms.certificate.entity.domain.CustomerCert;
import kr.co.kevit.localcsms.certificate.entity.shared.CustomerCertSearchCond;
import kr.co.kevit.localcsms.certificate.process.CustomerCertService;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 3. 18.
 */
@Service
public class CustomerCertServiceImpl implements CustomerCertService{
    
    @Autowired
    private CustomerCertProvider provider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCustomerCert(CustomerCert cert) {
        // 
        provider.registerCustomerCert(cert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyCustomerCert(CustomerCert cert) {
        // 
        provider.modifyCustomerCert(cert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCustomerCert(String emaid) {
        // 
        provider.removeCustomerCert(emaid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CustomerCert> retrieveCustomerCert(String customerId) {
        // 
        return provider.retrieveCustomerCert(customerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomerCert retrieveCustomerCertByEmaid(String emaid) {
        // 
        return provider.retrieveCustomerCertByEmaid(emaid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CustomerCert> retrieveCustomerCertBySearchCond(CustomerCertSearchCond searchCond) {
        // 
        return provider.retrieveCustomerCertBySearchCond(searchCond);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeCustomerCert(CustomerCert cert) {
        // 
        provider.mergeCustomerCert(cert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String retrieveEMaidGenereated() {
        // KR-KVT-000000000
        String eMaidPreFix = "KRKVT";
        String eMaid = eMaidPreFix + createEMaid();
        CustomerCert cert = provider.retrieveCustomerCertByEmaid(eMaid);
        while(cert != null) {
            eMaid = eMaidPreFix + createEMaid();
            cert = provider.retrieveCustomerCertByEmaid(eMaid);
        }
        return eMaid;
    }
    
    private String createEMaid() {
        //
        Random rand = new Random();
        StringBuilder builder = new StringBuilder(9);
        
        for(int i = 0; i < 9; ++i) {
            int iValue = rand.nextInt(36); // 0 <= iValue < 10
            builder.append(StringConstants.KEVIT_NUM[iValue]);
        }
        return builder.toString();
    }

}
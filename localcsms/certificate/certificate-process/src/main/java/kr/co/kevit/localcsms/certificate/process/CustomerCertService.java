/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.certificate.process;

import java.util.List;

import kr.co.kevit.localcsms.certificate.entity.domain.CustomerCert;
import kr.co.kevit.localcsms.certificate.entity.shared.CustomerCertSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2021. 3. 18.
 */
public interface CustomerCertService {

    String retrieveEMaidGenereated();
    
    void registerCustomerCert(CustomerCert cert);

    void modifyCustomerCert(CustomerCert cert);
    
    void mergeCustomerCert(CustomerCert cert);

    void removeCustomerCert(String emaid);

    List<CustomerCert> retrieveCustomerCert(String customerId);
    
    /**
     * for IF에서 조회.
     * @param emaid
     * @return
     */
    CustomerCert retrieveCustomerCertByEmaid(String emaid);

    Page<CustomerCert> retrieveCustomerCertBySearchCond(CustomerCertSearchCond searchCond);

}

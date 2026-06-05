/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.customer.entity.domain.CustomerCert;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCertSearchCond;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 3. 18.
 */
@Repository
public interface CustomerCertMapper {
    
    int insertCustomerCert(@Param("cert")CustomerCert cert);
    
    int updateCustomerCert(@Param("cert")CustomerCert cert);
    
    int deleteCustomerCert(@Param("emaid")String emaid);
    
    List<CustomerCert> selectCustomerCert(@Param("customerId")String customerId);

    List<CustomerCert> selectCustomerCertByPcid(@Param("pcid")String pcid);

    CustomerCert selectCustomerCertByEmaid(@Param("emaid")String emaid);
    
    int countCustomerCertBySearchCond(@Param("searchCond")CustomerCertSearchCond searchCond);
    
    List<CustomerCert> selectCustomerCertBySearchCond(@Param("searchCond")CustomerCertSearchCond searchCond);

}

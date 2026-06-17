/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.process.logic;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.customer.entity.CustomerMgtProvider;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.process.CustomerMgtService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2020. 8. 20.
 */
@Service
@Transactional
public class CustomerMgtServiceImpl implements CustomerMgtService {

    @Autowired
    private CustomerMgtProvider provider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyCustomerMgt(CustomerMgt customerMgt) {
        //
        provider.modifyCustomerMgt(customerMgt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyCustomerGrade(String customerId, String cutGrdCode) {
        //
        if (cutGrdCode == null || cutGrdCode.trim().isEmpty()) {
            throw new KEVITException("고객등급을 선택하세요.");
        }
        CustomerMgt customerMgt = provider.retrieveCustomerMgtByCustomerId(customerId);
        if (customerMgt == null) {
            throw new KEVITException("고객 관리 정보가 없습니다.");
        }
        customerMgt.setCutGrdCode(cutGrdCode);
        provider.modifyCustomerMgt(customerMgt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyCustomerStopYn(String customerId, String stopYn) {
        //
        if (stopYn == null || stopYn.trim().isEmpty()) {
            throw new KEVITException("정지여부를 선택하세요.");
        }
        CustomerMgt customerMgt = provider.retrieveCustomerMgtByCustomerId(customerId);
        if (customerMgt == null) {
            throw new KEVITException("고객 관리 정보가 없습니다.");
        }
        customerMgt.setStopYn(stopYn);
        customerMgt.setStopDate(StringConstants.Y.equals(stopYn) ? new Date() : null);
        provider.modifyCustomerMgt(customerMgt);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public CustomerMgt retrieveCustomerMgtByCustomerCardNo(String customerCardNo) {
        //
        return provider.retrieveCustomerMgtByCustomerCardNo(customerCardNo);
    }

}

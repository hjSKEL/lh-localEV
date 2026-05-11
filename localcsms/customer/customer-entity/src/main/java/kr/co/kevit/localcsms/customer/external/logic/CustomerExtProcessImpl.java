/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.external.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.entity.dao.CryptoKeyMapper;
import kr.co.kevit.localcsms.common.util.security.AES256Util;
import kr.co.kevit.localcsms.customer.entity.dao.CustomerMapper;
import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.external.CustomerExtProcess;

/**
 *
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 11. 13.
 */
@Component
public class CustomerExtProcessImpl implements CustomerExtProcess {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CryptoKeyMapper cryptoKeyMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer retrieveCustomerByUserId(String userId) {
        //
        Customer customer = customerMapper.selectCustomer(userId);
        if (customer == null)
            return null;

        byte[] keyData = convertStringToByte(cryptoKeyMapper.selectCryptoKey(Customer.class.getSimpleName()));
        customer.setMblPhoneNo(AES256Util.decryption(keyData, customer.getMblPhoneNo()));
        return customer;
    }

    private byte[] convertStringToByte(String keyStr) {
        int len = keyStr.length();
        byte[] result = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            result[i / 2] = (byte) ((Character.digit(keyStr.charAt(i), 16) << 4)
                    + Character.digit(keyStr.charAt(i + 1), 16));
        }
        return result;
    }
}

/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.process.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.authority.external.UserExtProcess;
import kr.co.kevit.localcsms.common.entity.CryptoKeyProvider;
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.security.AES256Util;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.localcsms.customer.entity.CustomerCardProvider;
import kr.co.kevit.localcsms.customer.entity.CustomerMgtProvider;
import kr.co.kevit.localcsms.customer.entity.CustomerProvider;
import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerCard;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerSearchCond;
import kr.co.kevit.localcsms.customer.process.CustomerService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 11. 13.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerProvider provider;

    @Autowired
    private CustomerMgtProvider cuMgtProvider;

    @Autowired
    private CryptoKeyProvider cryptoKeyProvider;
    
    @Autowired
    private CustomerCardProvider cardProvider;

    @Autowired
    private UserExtProcess userExtProcess;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCustomer(CustomerDto customer) {
        // 회원카드는 등록 시점에 필수가 아님 - 등록 후 회원카드관리에서 별도 추가
        if(customer.getCustomerMgt() != null && StringUtils.isNotEmpty(customer.getCustomerMgt().getCutCardNo())) {
            CustomerCard card = cardProvider.retrieveMemberCard(customer.getCustomerMgt().getCutCardNo());
            if(card != null) {
                throw new KEVITException("이미 등록된 카드번호 입니다.");
            }
        }

        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Customer.class);
        customer.setMblPhoneNo(AES256Util.encryption(keyData, customer.getMblPhoneNo()));

        if (StringUtils.isEmpty(customer.getCompanyId())) {
            customer.setCompanyId(StringConstants.DEFAULT_COMPANYID);
        }
        provider.registerCustomer(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyCustomer(Customer customer) {
        //
        CustomerDto oldCustomer = provider.retrieveCustomer(customer.getCustomerId());
        if(oldCustomer == null) {
            throw new KEVITException("회원 정보가 없습니다.");
        }
        
        // 회원카드 변경은 회원카드관리에서 별도 처리 - 고객 정보 수정 시에는 카드 필수 아님
        if(customer.getCustomerMgt() != null && oldCustomer.getCustomerMgt() != null
                && !customer.getCustomerMgt().getCutCardNo().equals(oldCustomer.getCustomerMgt().getCutCardNo())) {
            if(!StringConstants.Y.equals(oldCustomer.getCustomerMgt().getStopYn())) {
                throw new KEVITException("기존 카드번호를 정지 시키셔야 합니다.");
            }
            CustomerCard card = cardProvider.retrieveMemberCard(customer.getCustomerMgt().getCutCardNo());
            if(card != null) {
                throw new KEVITException("신규 회원 카드가 이미 등록된 카드번호 입니다.");
            }
        }

        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Customer.class);
        customer.setMblPhoneNo(AES256Util.encryption(keyData, customer.getMblPhoneNo()));
        provider.modifyCustomer(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public CustomerDto retrieveCustomer(String userId) {
        //
        CustomerDto customer = provider.retrieveCustomer(userId);
        if (customer == null)
            return null;

        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Customer.class);
        customer.setMblPhoneNo(AES256Util.decryption(keyData, customer.getMblPhoneNo()));
        
        return customer;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Customer retrieveCustomerByCustomerCardNo(String customerCardNo) {
        //
        Customer customer = provider.retrieveCustomerByCustomerCardNo(customerCardNo);
        if (customer == null)
            return null;

        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Customer.class);
        customer.setMblPhoneNo(AES256Util.decryption(keyData, customer.getMblPhoneNo()));
        return customer;
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<CustomerDto> retrieveCustomerDtoByCustomerSearchCond(CustomerSearchCond searchCond) {
        //
        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Customer.class);
        if (StringUtils.isNotEmpty(searchCond.getMblPhoneNo())) {
            searchCond.setMblPhoneNo(AES256Util.encryption(keyData, searchCond.getMblPhoneNo()));
        }

        Page<CustomerDto> resultSet = provider.retrieveCustomerDtoByCustomerSearchCond(searchCond);

        if (resultSet.getCriteria().getTotalItemCount() == 0) {
            return resultSet;
        }
        Map<String, String> phoneMap = new HashMap<>();
        List<String> companyIds = new ArrayList<>();
        for (CustomerDto customer : resultSet.getResult()) {
            String phoneNo = phoneMap.get(customer.getMblPhoneNo());
            if (phoneNo == null) {
                phoneNo = AES256Util.decryption(keyData, customer.getMblPhoneNo());
                phoneMap.put(customer.getMblPhoneNo(), phoneNo);
            }
            customer.setMblPhoneNo(phoneNo);
            companyIds.add(customer.getCompanyId());
        }
        return resultSet;
    }

    @Transactional(readOnly = true)

    @Override
    public CustomerMgt retrieveCustomerMgtByCustomerId(String customerId) {
        //
        return provider.retrieveCustomerMgtByCustomerId(customerId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Customer retrieveCustomerByUserId(String userId) {
        //
        Customer customer = provider.retrieveCustomer(userId);
        if (customer == null)
            return null;

        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Customer.class);
        customer.setMblPhoneNo(AES256Util.decryption(keyData, customer.getMblPhoneNo()));
        return customer;
    }

    @Override
    public boolean isReadyCardMappingCustomer(String customerId) {
        // 회원에 카드매핑여부 체크
        CustomerMgt customerMgt = provider.retrieveCustomerMgtByCustomerId(customerId);
        return customerMgt == null;
    }

    @Transactional(readOnly = true)

    @Override
    public CustomerDto retrieveCustomerWithCard(String customerId) {
        //
        CustomerDto customer = provider.retrieveCustomer(customerId);
        if (customer == null)
            return null;
        
        customer.setCustomerMgt(provider.retrieveCustomerMgtByCustomerId(customerId));

        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Customer.class);
        customer.setMblPhoneNo(AES256Util.decryption(keyData, customer.getMblPhoneNo()));

        // 사용자정보
        User user = userExtProcess.retrieveUserByUserId(customerId);
        customer.setLoginId(user.getLoginId());
        return customer;
    }

    @Override
    public void modifyCustomerAndCard(CustomerDto customer) {
        //
        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Customer.class);
        customer.setMblPhoneNo(AES256Util.encryption(keyData, customer.getMblPhoneNo()));
        provider.modifyCustomer(customer);

        // 비밀번호 있는경우 등록
        if (!StringUtils.isEmpty(customer.getUserPwd())) {
            User user = new User();
            user.setLoginId(customer.getLoginId());
            user.setUserId(customer.getCustomerId());
            user.setUserPwd(customer.getUserPwd());
            user.setWriter(customer.getWriter());
            userExtProcess.saveUser(user);
        }

        // 고객카드 등록
        CustomerMgt customerMgt = cuMgtProvider.retrieveCustomerMgtByCustomerCardNo(customer.getCustomerCard().getCutCardNo());
        customerMgt.setCustomerId(customer.getCustomerId());
        cuMgtProvider.modifyCustomerMgt(customerMgt);
    }

    @Transactional(readOnly = true)

    @Override
    public Page<CustomerDto> retrieveCustomerBySearchCond(CustomerSearchCond searchCond) {
        //
        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Customer.class);

        Page<CustomerDto> resultSet = provider.retrieveCustomerDtoByCustomerSearchCond(searchCond);

        if (resultSet.getCriteria().getTotalItemCount() == 0) {
            return resultSet;
        }
        Map<String, String> phoneMap = new HashMap<>();
        for (CustomerDto customer : resultSet.getResult()) {
            String phoneNo = phoneMap.get(customer.getMblPhoneNo());
            if (phoneNo == null) {
                phoneNo = AES256Util.decryption(keyData, customer.getMblPhoneNo());
                phoneMap.put(customer.getMblPhoneNo(), phoneNo);
            }
            customer.setMblPhoneNo(phoneNo);
        }
        return resultSet;
    }

    @Override
    public void removeCustomer(String customerId) {
        //
        Customer customer = provider.retrieveCustomerByCustomerId(customerId);
        if (customer == null) {
            throw new KEVITException("CUCU001", "등록된 고객이 없습니다.");
        }
        CustomerMgt customerMgt = customer.getCustomerMgt();
        if (customerMgt != null) {
            customerMgt.setStopYn(StringConstants.Y);
            customerMgt.setStopDate(new Date());
            cuMgtProvider.modifyCustomerMgt(customerMgt);
        }

        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Customer.class);
        if (!StringUtils.isEmpty(customer.getMblPhoneNo())) {
            customer.setMblPhoneNo("D" + AES256Util.decryption(keyData, customer.getMblPhoneNo()));
            customer.setMblPhoneNo(AES256Util.encryption(keyData, customer.getMblPhoneNo()));
        }
        provider.modifyCustomer(customer);

        User user = userExtProcess.retrieveUserByUserId(customerId);
        if (user == null) {
            throw new KEVITException("CUCU003", "사용자 계정 삭제 중 내부 오류 발생");
        }
        // 계정과 역활 삭제
        userExtProcess.removeUser(user.getLoginId());
    }

    @Transactional(readOnly = true)

    @Override
    public CustomerDto retrieveCustomerByCustomer(CustomerDto customer) {
        //
        byte[] keyData = cryptoKeyProvider.retriveCryptoKey(Customer.class);
        if (!StringUtils.isEmpty(customer.getMblPhoneNo())) {
            customer.setMblPhoneNo(AES256Util.encryption(keyData, customer.getMblPhoneNo()));
        }

        CustomerDto customerDto = provider.retrieveCustomer(customer.getCustomerId());
        if (customerDto != null) {
            provider.retrieveCustomerMgtByCustomerId(customer.getCustomerId());
            if (!StringUtils.isEmpty(customerDto.getMblPhoneNo())) {
                customerDto.setMblPhoneNo(AES256Util.decryption(keyData, customerDto.getMblPhoneNo()));
            }
        }
        return customerDto;
    }
}

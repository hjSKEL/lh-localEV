/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation.
 * All rights reserved. This software is the proprietary information of
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.external.logic;

import kr.co.kevit.localcsms.common.entity.dao.CryptoKeyMapper;
import kr.co.kevit.localcsms.common.util.security.AES256Util;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.localcsms.organization.entity.dao.EmployeeMapper;
import kr.co.kevit.localcsms.organization.entity.domain.Employee;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeDto;
import kr.co.kevit.localcsms.organization.external.EmployeeExtProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 19.
 */
@Component
public class EmployeeExtProcessImpl implements EmployeeExtProcess {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private CryptoKeyMapper cryptoKeyMapper;

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Employee retrieveEmployeeById(String id) {
        //
        Employee employee = employeeMapper.selectEmployeeById(id);
        if(StringUtils.isNotEmpty(employee.getMblPhoneNo())) {
            byte[] keyData = convertStringToByte(cryptoKeyMapper.selectCryptoKey(Employee.class.getSimpleName()));
            employee.setMblPhoneNo(AES256Util.decryption(keyData, employee.getMblPhoneNo()));
        }
        return employee;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmployeeDto> retrieveEmployeeByIds(List<String> ids) {
        //
        return employeeMapper.selectEmployeeByIds(ids);
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

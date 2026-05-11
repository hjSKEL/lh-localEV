/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.logic;

import kr.co.kevit.localcsms.common.entity.CryptoKeyProvider;
import kr.co.kevit.localcsms.common.entity.dao.CryptoKeyMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 4. 18.
 */
@Component
public class CryptoKeyProviderImpl implements CryptoKeyProvider {

    @Autowired
    private CryptoKeyMapper mapper;

    @Override
    public byte[] retriveCryptoKey(Class<?> obj) {
        //
        String id = obj.getSimpleName();
        String key = mapper.selectCryptoKey(id);
        return convertStringToByte(key);
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

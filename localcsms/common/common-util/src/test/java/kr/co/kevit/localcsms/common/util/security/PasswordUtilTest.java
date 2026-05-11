/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.security;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

import org.junit.Test;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 15.
 */
public class PasswordUtilTest {

    @Test
    public void test() {
        String password = "test";
        PasswordUtil passwordUtil = PasswordUtil.getInstance();
        String salt = passwordUtil.generateSalt();
        System.out.println(salt);
        //salt = "zb+cu1jJOUKaD8QQDqyUPw==";
        String passwordEncode = passwordUtil.encryptPassword(password, salt);
        System.out.println(passwordEncode);
        //a44b5f5ae54374e008e2c69100f43bee230730f022e0a064b200b0944251a99b
    }
    
    @Test
    public void testHash() {
        try {
            Path path = Paths.get("C:\\projects\\00.KEVIT\\996.GS\\00.산출물\\00.sources\\local-csms.zip");
            byte[] data = Files.readAllBytes(path);
            MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
            mDigest.update(data);
    
            byte[] msgStr = mDigest.digest();
    
            StringBuilder sbuf = new StringBuilder(255);
            for (int i = 0; i < msgStr.length; i++) {
                byte tmpStrByte = msgStr[i];
                String tmpEncTxt = Integer.toString((tmpStrByte & 0xff) + 0x100, 16).substring(1);
    
                sbuf.append(tmpEncTxt);
            }
            System.out.println(sbuf.toString());
        } catch (Exception e) {
            //
        }
    }

}

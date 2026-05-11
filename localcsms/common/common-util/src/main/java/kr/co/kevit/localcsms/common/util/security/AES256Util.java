package kr.co.kevit.localcsms.common.util.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Sol
 *
 */
public class AES256Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(AES256Util.class);

    // 암호화
    public static String encryption(byte[] keyData, String str) {
        //
        if(str == null || str.trim().isEmpty()) {
            return "";
        }
        
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(makeIV(keyData)));
            byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
            return new String(Base64.encodeBase64(encrypted));
        } catch (java.io.UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
                | BadPaddingException e) {
            LOGGER.error(e.getMessage());
            return "";
        }
    }

    private static byte[] makeIV(byte[] keyData) {
        byte[] iv = new byte[16];
        for (int i = 0; i < 16; i++) {
            iv[i] = keyData[i];
        }
        return iv;
    }

    // 복호화
    public static String decryption(byte[] keyData, String str) {
        //
        if(str == null || str.trim().isEmpty()) {
            return "";
        }
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(makeIV(keyData)));
            byte[] byteStr = Base64.decodeBase64(str.getBytes());
            return new String(c.doFinal(byteStr), "UTF-8");
        } catch (java.io.UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
                | BadPaddingException e) {
            LOGGER.error(e.getMessage());
            return "";
        }
    }
}

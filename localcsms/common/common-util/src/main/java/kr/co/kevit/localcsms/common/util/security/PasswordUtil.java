package kr.co.kevit.localcsms.common.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 
 * @author Sol
 *
 */
public class PasswordUtil {
    
    private static PasswordUtil instance = new PasswordUtil();
    
    public PasswordUtil() {
        //
    }
    
    public static PasswordUtil getInstance() {
        return instance;
    }
    
    /**
     * Password를 암호화한다.
     * 
     * @param password
     * @return
     */
    public String encryptPassword(String password, String salt) {
        return getEncSHA256(password, salt);
    }
    
    public String generateSalt() {
        //
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
            byte[] bytes = new byte[16];
            random.nextBytes(bytes);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            // 
        }
        return null;
    }
    
    /**
     * 무작위 암호를 생성한다.
     * <pre>
     * 1. 10자리 암호 생성
     * 2. a-z/A-Z/0-9 범위내에서 선택
     * </pre>
     * @return
     */
    public String getRandomPassword() {
            
        StringBuilder randomPassword = new StringBuilder(30);
        SecureRandom rnd = new SecureRandom();
        for (int i = 0; i < 10; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
            case 0:
                // a-z
                randomPassword.append((char) ((int) (rnd.nextInt(26)) + 97));
                break;
            case 1:
                // A-Z
                randomPassword.append((char) ((int) (rnd.nextInt(26)) + 65));
                break;
            case 2:
                // 0-9
                randomPassword.append((rnd.nextInt(10)));
                break;
            }
        }
        return randomPassword.toString();
    }
    
    /**
     * 문자열을 SHA-256 방식으로 암호화
     * 
     * @param txt 암호화 하려하는 문자열
     * @param salt
     * @return String
     * @throws Exception
     */
    private String getEncSHA256(String txt, String salt) {
        StringBuilder sbuf = new StringBuilder(255);

        MessageDigest mDigest;
        try {
            mDigest = MessageDigest.getInstance("SHA-256");
            mDigest.update(salt.getBytes());
            mDigest.update(txt.getBytes());
    
            byte[] msgStr = mDigest.digest();
    
            for (int i = 0; i < msgStr.length; i++) {
                byte tmpStrByte = msgStr[i];
                String tmpEncTxt = Integer.toString((tmpStrByte & 0xff) + 0x100, 16).substring(1);
    
                sbuf.append(tmpEncTxt);
            }
        } catch (NoSuchAlgorithmException e) {
            //
            return null;
        }

        return sbuf.toString();
    }
}

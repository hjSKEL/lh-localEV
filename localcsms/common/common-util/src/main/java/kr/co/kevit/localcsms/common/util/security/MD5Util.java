package kr.co.kevit.localcsms.common.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(MD5Util.class);

    // 암호화
    public static byte[] encryption(String txt) {
        //
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("----------------");
            LOGGER.debug(txt);
            LOGGER.debug("----------------");
        }
        byte[] msgStr = null;
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(txt.getBytes());
            return mDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            //
            LOGGER.error(e.getMessage(), e);
        }
        return msgStr;
    }
}

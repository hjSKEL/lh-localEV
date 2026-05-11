package kr.co.kevit.localcsms.common.entity;

/**
 * 
 * @author Sol
 *
 */
public interface CryptoKeyProvider {

    /**
     * 
     * @param obj
     * @return 32 bytes
     */
    byte[] retriveCryptoKey(Class<?> obj);

}

package kr.co.kevit.localcsms.common.util.security.aria;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

public class ARIAUtil {

    // public static void main (String args[]) {
    // try {
    // ARIAEngine.ARIA_test();
    // } catch(Exception e) {}
    // }

    /**
     * ARIA encryption algorithm block size
     */
    private static final int ARIA_BLOCK_SIZE = 16;

    /**
     * ARIA algorithm to encrypt the data.
     * 
     * @param data
     *            Target Data
     * @param key
     *            Masterkey
     * @param keySize
     *            Masterkey Size
     * @param charset
     *            Data character set
     * @return Encrypted data
     * @throws UnsupportedEncodingException
     *             If character is not supported
     * @throws InvalidKeyException
     *             If the Masterkey is not valid
     */
    public static byte[] encrypt(byte[] data, byte[] key, int keySize, String charset)
            throws UnsupportedEncodingException, InvalidKeyException {

        ARIAEngine engine = new ARIAEngine(key.length * 8);
        engine.setKey(key);
        engine.setupRoundKeys();
        byte[] in = PKCS5Padding.pad(data, ARIA_BLOCK_SIZE);
        byte[] out = new byte[in.length];

        for (int i = 0; i < in.length; i += ARIA_BLOCK_SIZE) {
            engine.encrypt(in, i, out, i);
        }

        return out;
    }

    /**
     * ARIA algorithm to decrypt the data.
     * 
     * @param data
     *            Target Data
     * @param key
     *            Masterkey
     * @param keySize
     *            Masterkey Size
     * @param charset
     *            Data character set
     * @return Decrypted data
     * @throws UnsupportedEncodingException
     *             If character is not supported
     * @throws InvalidKeyException
     *             If the Masterkey is not valid
     */
    public static String decrypt(byte[] decrypt, byte[] key, int keySize, String charset)
            throws UnsupportedEncodingException, InvalidKeyException {

        ARIAEngine engine = new ARIAEngine(keySize);
        engine.setKey(key);
        engine.setupDecRoundKeys();

        int blockCount = decrypt.length / ARIA_BLOCK_SIZE;
        for (int i = 0; i < blockCount; i++) {

            byte buffer[] = new byte[ARIA_BLOCK_SIZE];
            System.arraycopy(decrypt, (i * ARIA_BLOCK_SIZE), buffer, 0, ARIA_BLOCK_SIZE);

            buffer = engine.decrypt(buffer, 0);
            System.arraycopy(buffer, 0, decrypt, (i * ARIA_BLOCK_SIZE), buffer.length);
        }

        if (charset == null) {
            return new String(BlockPadding.getInstance().removePadding(decrypt, ARIA_BLOCK_SIZE));
        } else {
            return new String(BlockPadding.getInstance().removePadding(decrypt, ARIA_BLOCK_SIZE), charset);
        }
    }

    /**
     * The sample code in the Cipher class
     * 
     * @param args
     *            none
     */
    // public static void main(String args[]) {
    //
    // try {
    // String key16 = "evXULVAC2017";
    //
    // String data = String data = "E1000000013820170707172812CHAR0101223885173CHRS030CHE001UL1.0.0             0000000000100             0000000000000000000000000EVT008                 ";
    //
    // byte[] encrypt = ARIAUtil.encrypt(data.getBytes(), key16.getBytes(),
    // key16.getBytes().length*8, null);
    // System.out.println("암호문 :"+encrypt.length);
    //
    // StringBuilder sb = new StringBuilder();
    // for(final byte b: encrypt){
    // sb.append(String.format("%02x", b&0xff));
    // }
    // System.out.println("암호문2 :"+sb.toString());
    //
    // String decryptData = ARIAUtil.decrypt(encrypt, key16.getBytes(),
    // key16.getBytes().length*8, null);
    // System.out.println("복호문 :"+decryptData);
    //
    //
    //
    // } catch(Exception e) {
    // System.out.println(e.getMessage());
    // }
    // }

}

package kr.co.kevit.localcsms.common.util.security;

/**
 * 바이트 배열 ↔ HEX 문자열 변환 유틸리티.
 */
public class ByteUtils {

    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    private ByteUtils() {}

    public static byte[] hexStringToByteArray(String hex) {
        if (hex == null) return new byte[0];
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    public static String byte2hexString(byte[] bytes, int length) {
        if (bytes == null) return "";
        int len = Math.min(bytes.length, length);
        StringBuilder sb = new StringBuilder(len * 2);
        for (int i = 0; i < len; i++) {
            sb.append(HEX_CHARS[(bytes[i] >> 4) & 0x0F]);
            sb.append(HEX_CHARS[bytes[i] & 0x0F]);
        }
        return sb.toString();
    }

    public static String toHexFromByte(byte[] bytes) {
        if (bytes == null) return "";
        return byte2hexString(bytes, bytes.length);
    }
}

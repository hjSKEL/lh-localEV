package kr.co.kevit.localcsms.common.util.security;

import java.util.ArrayList;
import java.util.List;

/**
 * PEM 인증서 체인 파싱 유틸리티.
 */
public class CertStringUtils {

    private static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERT = "-----END CERTIFICATE-----";

    private CertStringUtils() {}

    /**
     * PEM 형식의 인증서 체인 문자열을 개별 인증서 리스트로 분리한다.
     */
    public static List<String> parseChain(String certChain) {
        List<String> certs = new ArrayList<>();
        if (certChain == null || certChain.isEmpty()) return certs;

        int start = 0;
        while ((start = certChain.indexOf(BEGIN_CERT, start)) != -1) {
            int end = certChain.indexOf(END_CERT, start);
            if (end == -1) break;
            end += END_CERT.length();
            certs.add(certChain.substring(start, end).trim());
            start = end;
        }
        return certs;
    }
}

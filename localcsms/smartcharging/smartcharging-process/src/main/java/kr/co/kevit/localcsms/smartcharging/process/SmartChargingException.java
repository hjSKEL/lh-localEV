package kr.co.kevit.localcsms.smartcharging.process;

/**
 * Smart Charging 프로파일 검증/처리 실패. 사유 메시지를 담아 호출측이 Rejected 응답에 사용.
 */
public class SmartChargingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SmartChargingException(String message) {
        super(message);
    }
}

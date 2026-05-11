package kr.co.kevit.localcsms.ocpp20.bean;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 서버→충전기 CALL에 대한 CALLRESULT / CALLERROR 처리 Bean 인터페이스.
 *
 * 충전기가 서버 명령에 응답(CALLRESULT) 또는 오류(CALLERROR)를 반환했을 때 호출됩니다.
 * CALLERROR 인 경우 payload 에 {@code errorCode}, {@code errorDescription} 필드가 포함됩니다.
 *
 * 구현 예시:
 * <pre>
 *   {@literal @}Component("Reset")
 *   public class ResetBean implements ResponderBean {
 *       public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception { ... }
 *   }
 * </pre>
 */
public interface ResponderBean {

    /**
     * CALLRESULT / CALLERROR 처리.
     *
     * @param cpCsId   충전소+충전기 식별자 (cpId-csId)
     * @param payload  충전기가 반환한 CALLRESULT payload 또는 CALLERROR 정보
     * @param uniqueId OCPP 전문 UUID (메시지 고유 식별자)
     */
    void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception;
}

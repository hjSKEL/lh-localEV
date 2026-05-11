package kr.co.kevit.localcsms.ocpp.bean;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp.model.OcppMessage;

/**
 * OCPP Action 처리 Bean 인터페이스.
 *
 * 구현 예시:
 * <pre>
 *   @Component
 *   public class HeartbeatBean implements ControlerBean {
 *       public ObjectNode control(String cpCsId, OcppMessage msg) { ... }
 *   }
 * </pre>
 */
public interface ControlerBean {

    /**
     * Action 처리 후 충전기에 전달할 CALLRESULT payload를 반환합니다.
     *
     * @param cpCsId 충전소+충전기 식별자 (cpId+csId)
     * @param msg    수신된 OCPP 메시지
     * @return CALLRESULT payload (null 불가)
     */
    ObjectNode control(String cpCsId, OcppMessage msg) throws Exception;
}

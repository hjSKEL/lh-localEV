package kr.co.kevit.localcsms.ocpp20.bean;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;

/**
 * OCPP Action 처리 Bean 인터페이스.
 *
 * 구현 예시:
 * <pre>
 *   @Component("Heartbeat")
 *   public class HeartbeatBean implements ControlerBean {
 *       public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception { ... }
 *   }
 * </pre>
 *
 * Spring이 @Component("ActionName") 으로 등록된 구현체를
 * Map&lt;String, ControlerBean&gt; 형태로 자동 주입합니다.
 * key = bean name(액션명), value = 구현체
 */
public interface ControlerBean {

    /**
     * Action 처리 후 충전기에 전달할 CALLRESULT payload를 반환합니다.
     *
     * @param cpCsId 충전소+충전기 식별자 (cpId-csId)
     * @param msg    수신된 OCPP 메시지
     * @return CALLRESULT payload (null 반환 시 빈 ObjectNode 전송)
     */
    ObjectNode control(String cpCsId, OcppMessage msg) throws Exception;
}

package kr.co.kevit.localcsms.ocpp20.bean;

import com.fasterxml.jackson.databind.node.ObjectNode;

import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;

/**
 * inbound CALL 처리({@link ControlerBean}) 직후 CSMS 가 능동 CALL 을 이어서 보낼 수 있게 하는 훅.
 *
 * <p>예: NotifyEVChargingNeeds 응답(CALLRESULT) 송신 후 SetChargingProfile CALL 을 push.
 * 핸들러가 CALLRESULT 를 먼저 write 한 뒤 {@link #followUp} 결과를 송신하므로 OCPP 메시지 순서가 보장됨.</p>
 */
public interface FollowUpCapable {

    /**
     * @param cpCsId   충전소+충전기 식별자
     * @param msg      방금 처리한 inbound CALL
     * @param response control() 이 반환한 CALLRESULT payload (이미 송신됨)
     * @return 이어서 송신할 CALL, 없으면 null
     */
    OutboundCall followUp(String cpCsId, OcppMessage msg, ObjectNode response) throws Exception;
}

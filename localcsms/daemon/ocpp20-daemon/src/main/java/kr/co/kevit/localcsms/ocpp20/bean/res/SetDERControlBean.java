package kr.co.kevit.localcsms.ocpp20.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.ocpp20.bean.ResponderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * SetDERControlResponse 수신 — 운영자가 push 한 SetDERControlRequest 의 결과.
 * 본 응답은 admin-web 의 send2x optimistic ACK 와 별개로, 비동기로 CS 가 보내는 진짜 결과.
 *
 * <p>현재 cross-JVM uniqueId↔controlId 매핑이 없으므로 log 로 추적 후
 * admin-web 화면에서 GetDERControl 로 실제 상태 확인.</p>
 */
@Component("SetDERControl")
public class SetDERControlBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(SetDERControlBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        kr.co.kevit.ocpp201.response.SetDERControl response =
                objectMapper.treeToValue(payload, kr.co.kevit.ocpp201.response.SetDERControl.class);
        log.info("[OCPP20] SetDERControlResponse cpCsId={} uniqueId={} status={} statusInfo={}",
                cpCsId, uniqueId, response.getStatus(),
                response.getStatusInfo() != null ? response.getStatusInfo().getReasonCode() : null);
    }
}

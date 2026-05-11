package kr.co.kevit.localcsms.ocpp20.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

/**
 * 원격 명령 REST 요청 바디.
 *
 * POST /ocpp20/command/{cpId}
 * { "action": "RequestStartTransaction", "payload": { ... } }
 */
@Getter
@Setter
public class OcppCommandRequest {
    private String   action;
    private JsonNode payload;
    private String   uuid;
}

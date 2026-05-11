package kr.co.kevit.localcsms.ocpp.model;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 원격 명령 REST 요청 바디.
 *
 * POST /command/{cpId}
 * { "action": "RemoteStartTransaction", "payload": { ... } }
 */
public class OcppCommandRequest {
    private String   action;
    private JsonNode payload;
    private String   uuid;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

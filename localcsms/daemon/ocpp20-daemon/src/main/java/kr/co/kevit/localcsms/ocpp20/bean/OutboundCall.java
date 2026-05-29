package kr.co.kevit.localcsms.ocpp20.bean;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * CSMS → CS 후속 능동 CALL (action + payload). {@link FollowUpCapable} 가 반환.
 */
public class OutboundCall {

    private final String action;
    private final ObjectNode payload;

    public OutboundCall(String action, ObjectNode payload) {
        this.action = action;
        this.payload = payload;
    }

    public String getAction() {
        return action;
    }

    public ObjectNode getPayload() {
        return payload;
    }
}

package kr.co.kevit.localcsms.ocpp.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

/**
 * OCPP 1.6 메시지 래퍼.
 *
 * CALL        : [2, uniqueId, action, payload]
 * CALLRESULT  : [3, uniqueId, payload]
 * CALLERROR   : [4, uniqueId, errorCode, errorDescription, errorDetails]
 */
@Getter
public class OcppMessage {

    public static final int CALL       = 2;
    public static final int CALLRESULT = 3;
    public static final int CALLERROR  = 4;

    private final int      messageTypeId;
    private final String   uniqueId;
    private final String   action;
    private final JsonNode payload;

    public OcppMessage(int messageTypeId, String uniqueId, String action, JsonNode payload) {
        this.messageTypeId = messageTypeId;
        this.uniqueId      = uniqueId;
        this.action        = action;
        this.payload       = payload;
    }
}

package kr.co.kevit.localcsms.ocpp.controller;

import kr.co.kevit.localcsms.ocpp.handler.OcppWebSocketHandler;
import kr.co.kevit.localcsms.ocpp.model.OcppCommandRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 원격 명령 수신 REST API
 *
 * POST /command/{cpId}
 * {
 * "action": "RemoteStartTransaction",
 * "payload": { "connectorId": 1, "idTag": "ABC123" }
 * }
 */
@RestController
@RequestMapping("/command")
public class OcppCommandController {

    private static final Logger log = LoggerFactory.getLogger(OcppCommandController.class);

    private final OcppWebSocketHandler ocppWebSocketHandler;

    public OcppCommandController(OcppWebSocketHandler ocppWebSocketHandler) {
        this.ocppWebSocketHandler = ocppWebSocketHandler;
    }

    @PostMapping("/{cpId}")
    public ResponseEntity<Map<String, Object>> sendCommand(
            @PathVariable String cpId,
            @RequestBody OcppCommandRequest request) {

        log.info("[CMD] 원격 명령 수신: cpId={} action={}", cpId, request.getAction());

        if (!ocppWebSocketHandler.isConnected(cpId)) {
            return ResponseEntity.ok(Map.of(
                    "status", "FAIL",
                    "message", "충전기가 연결되어 있지 않습니다: " + cpId));
        }

        try {
            ocppWebSocketHandler.sendCommand(cpId, request.getAction(), request.getPayload(), request.getUuid());
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "명령 전송 완료"));
        } catch (Exception e) {
            log.error("[CMD] 명령 전송 실패: cpId={} action={} error={}", cpId, request.getAction(), e.getMessage());
            return ResponseEntity.ok(Map.of(
                    "status", "FAIL",
                    "message", e.getMessage()));
        }
    }

    /** 현재 연결된 충전기 목록 조회 */
    @GetMapping("/sessions")
    public ResponseEntity<Map<String, Object>> getSessions() {
        return ResponseEntity.ok(Map.of(
                "connected", ocppWebSocketHandler.getConnectedCpIds()));
    }
}

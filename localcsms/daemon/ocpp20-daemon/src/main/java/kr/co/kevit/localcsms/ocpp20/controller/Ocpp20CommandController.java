package kr.co.kevit.localcsms.ocpp20.controller;

import kr.co.kevit.localcsms.ocpp20.handler.Ocpp20WebSocketHandler;
import kr.co.kevit.localcsms.ocpp20.model.OcppCommandRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * OCPP 2.0.1 원격 명령 REST API.
 *
 * POST /ocpp20/command/{cpId}
 * GET  /ocpp20/command/sessions
 */
@RestController
@RequestMapping("/ocpp20/command")
public class Ocpp20CommandController {

    private static final Logger log = LoggerFactory.getLogger(Ocpp20CommandController.class);

    private final Ocpp20WebSocketHandler ocpp20WebSocketHandler;

    public Ocpp20CommandController(Ocpp20WebSocketHandler ocpp20WebSocketHandler) {
        this.ocpp20WebSocketHandler = ocpp20WebSocketHandler;
    }

    @PostMapping("/{cpId}")
    public ResponseEntity<Map<String, Object>> sendCommand(
            @PathVariable String cpId,
            @RequestBody OcppCommandRequest request) {

        log.info("[OCPP20-CMD] 원격 명령 수신: cpId={} action={}", cpId, request.getAction());

        if (!ocpp20WebSocketHandler.isConnected(cpId)) {
            return ResponseEntity.ok(Map.of(
                    "status",  "FAIL",
                    "message", "충전기가 연결되어 있지 않습니다: " + cpId));
        }

        try {
            ocpp20WebSocketHandler.sendCommand(cpId, request.getAction(), request.getPayload(), request.getUuid());
            return ResponseEntity.ok(Map.of(
                    "status",  "SUCCESS",
                    "message", "명령 전송 완료"));
        } catch (Exception e) {
            log.error("[OCPP20-CMD] 명령 전송 실패: cpId={} action={} error={}",
                    cpId, request.getAction(), e.getMessage());
            return ResponseEntity.ok(Map.of(
                    "status",  "FAIL",
                    "message", e.getMessage()));
        }
    }

    /** 현재 연결된 충전기 목록 */
    @GetMapping("/sessions")
    public ResponseEntity<Map<String, Object>> getSessions() {
        return ResponseEntity.ok(Map.of(
                "connected", ocpp20WebSocketHandler.getConnectedCpIds()));
    }
}

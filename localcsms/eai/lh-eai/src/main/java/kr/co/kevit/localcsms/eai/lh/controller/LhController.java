package kr.co.kevit.localcsms.eai.lh.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.eai.lh.dto.LhErrorResponse;
import kr.co.kevit.localcsms.eai.lh.dto.LhRequest;
import kr.co.kevit.localcsms.eai.lh.dto.LhResponse;
import kr.co.kevit.localcsms.eai.lh.wss.LhWssClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * LH 외부 호출 엔드포인트.
 *
 * <pre>
 * POST /api/v1/lh
 *
 * 요청 : {"stationid": "1", "payload": "..."}
 *
 * 정상 (HTTP 200):
 *   {"result": "200", "message": "OK", "sendTime": "2026-05-22T05:24:25.699Z"}
 *
 * 비정상 (결과코드 400):
 *   {"result": "400", "message": "Bad request (bad request param info)", "data": null}
 * </pre>
 *
 * 동작: 요청 본문을 그대로 직렬화하여 {@link LhWssClient} 를 통해 WSS 세션으로 전달한다.
 *       전송 성공/실패와 무관하게 호출자에게는 표준 응답 포맷으로 응답한다 (빈 로직).
 */
@RestController
@RequestMapping("/api/v1")
public class LhController {

    private static final Logger log = LoggerFactory.getLogger(LhController.class);

    private final LhWssClient wssClient;
    private final ObjectMapper objectMapper;

    public LhController(LhWssClient wssClient, ObjectMapper objectMapper) {
        this.wssClient = wssClient;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/lh")
    public ResponseEntity<?> receive(@RequestBody LhRequest request) {
        String stationid = request == null ? null : request.getStationid();
        log.info("[LH] 요청 수신 stationid={}", stationid);

        if (stationid == null || stationid.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LhErrorResponse("400", "Bad request (bad request param info)", null));
        }

        // 호출 받은 내용을 WSS 채널로 전달
        try {
            String payload = objectMapper.writeValueAsString(request);
            boolean sent = wssClient.forward(payload);
            if (!sent) {
                log.warn("[LH] WSS 전달 실패 stationid={}", stationid);
            }
        } catch (Exception e) {
            log.error("[LH] WSS 직렬화/전달 오류 stationid={}", stationid, e);
        }

        return ResponseEntity.ok(new LhResponse("200", "OK", Instant.now().toString()));
    }
}

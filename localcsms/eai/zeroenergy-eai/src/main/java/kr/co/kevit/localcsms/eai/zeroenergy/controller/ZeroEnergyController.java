package kr.co.kevit.localcsms.eai.zeroenergy.controller;

import kr.co.kevit.localcsms.eai.zeroenergy.dto.ZeroEnergyErrorResponse;
import kr.co.kevit.localcsms.eai.zeroenergy.dto.ZeroEnergyRequest;
import kr.co.kevit.localcsms.eai.zeroenergy.dto.ZeroEnergyResponse;
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
 * Zero Energy 충전량 조회 엔드포인트.
 *
 * <pre>
 * POST /api/v1/zeroenergy
 *
 * 요청 : {"stationid":"1"}
 *
 * 정상 (HTTP 200):
 *   {"totalCharge": "117.30", "sendTime": "2023-12-27T05:24:25.699Z"}
 *
 * 비정상 (결과코드 400):
 *   {"result": "400", "message": "Bad request (bad request param info)", "data": null}
 * </pre>
 */
@RestController
@RequestMapping("/api/v1")
public class ZeroEnergyController {

    private static final Logger log = LoggerFactory.getLogger(ZeroEnergyController.class);

    @PostMapping("/zeroenergy")
    public ResponseEntity<?> getZeroEnergy(@RequestBody ZeroEnergyRequest request) {
        String stationid = request == null ? null : request.getStationid();
        log.info("[ZEROENERGY] 요청 수신 stationid={}", stationid);

        // 요청 파라미터 검증 — 실패 시 400 + ZeroEnergyErrorResponse
        if (stationid == null || stationid.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ZeroEnergyErrorResponse("400", "Bad request (bad request param info)", null));
        }

        // TODO: 실제 충전 누적량 조회 로직으로 교체
        ZeroEnergyResponse response = new ZeroEnergyResponse("117.30", Instant.now().toString());
        return ResponseEntity.ok(response);
    }
}

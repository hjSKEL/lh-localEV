package kr.co.kevit.localcsms.eai.api.controller.ocpp16;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.type.ResetType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration 그룹 엔드포인트.
 *
 * POST /reset
 * POST /getConfiguration
 * POST /changeConfiguration
 * POST /updatePassword
 * POST /clearCache
 */
@RestController
@RequestMapping("/ocpp16")
public class ConfigurationController {

    private final Daemon16Client daemonClient;
    private final ChargingStationService chargingStationService;

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int PASSWORD_LENGTH = 16;
    private final SecureRandom random = new SecureRandom();

    public ConfigurationController(Daemon16Client daemonClient,
                                   ChargingStationService chargingStationService) {
        this.daemonClient = daemonClient;
        this.chargingStationService = chargingStationService;
    }

    /** Trigger a Reset.req from the CSMS. */
    @PostMapping("/reset")
    public ResponseEntity<ApiResult> reset(
            @RequestParam String chargingStationIdentity,
            @RequestParam ResetType type) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", type.name());
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "Reset", payload, null));
    }

    /** Trigger a GetConfiguration.req from the CSMS. */
    @PostMapping("/getConfiguration")
    public ResponseEntity<ApiResult> getConfiguration(
            @RequestParam String chargingStationIdentity,
            @RequestParam(required = false) String key) {
        Map<String, Object> payload = new LinkedHashMap<>();
        if (key != null && !key.isBlank()) {
            payload.put("key", List.of(key));
        }
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetConfiguration", payload, null));
    }

    /** Trigger a ChangeConfiguration.req from the CSMS. */
    @PostMapping("/changeConfiguration")
    public ResponseEntity<ApiResult> changeConfiguration(
            @RequestParam String chargingStationIdentity,
            @RequestParam String key,
            @RequestParam String value) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("key", key);
        payload.put("value", value);
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "ChangeConfiguration", payload, null));
    }

    /**
     * Trigger a ChangeConfiguration.req to update the basic authentication password.
     *
     * 1) 16자리 랜덤 비밀번호 생성 (A-Z, 0-9)
     * 2) Hex 인코딩하여 충전기에 전달
     * 3) 평문을 lastCsPassword 에 DB 저장 (스테이징)
     * 4) 충전기 응답 Accepted → csPassword ← lastCsPassword 확정
     */
    @PostMapping("/updatePassword")
    public ResponseEntity<ApiResult> updatePassword(
            @RequestParam String chargingStationIdentity) {
        String cpCsId = chargingStationIdentity;

        int idx = cpCsId.lastIndexOf('-');
        if (idx <= 0 || idx == cpCsId.length() - 1) {
            return ResponseEntity.ok(ApiResult.rejected("cpCsId 형식 오류: " + cpCsId));
        }
        String cpId = cpCsId.substring(0, idx);
        String csId = cpCsId.substring(idx + 1);

        ChargingStation cs = chargingStationService.retrieveChargingStationByCpIdNCsId(cpId, csId);
        if (cs == null) {
            return ResponseEntity.ok(ApiResult.rejected("충전기를 찾을 수 없습니다: " + cpCsId));
        }

        String newPassword = generatePassword();
        String hexPassword = toHex(newPassword);

        cs.setLastCsPassword(newPassword);
        chargingStationService.modifyChargingStation(cs);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("key", "AuthorizationKey");
        payload.put("value", hexPassword);
        return ResponseEntity.ok(daemonClient.send(cpCsId, "ChangeConfiguration", payload, null));
    }

    /** Trigger a ClearCache.req from the CSMS. */
    @PostMapping("/clearCache")
    public ResponseEntity<ApiResult> clearCache(
            @RequestParam String chargingStationIdentity) {
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "ClearCache", null, null));
    }

    private String generatePassword() {
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    private String toHex(String input) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}

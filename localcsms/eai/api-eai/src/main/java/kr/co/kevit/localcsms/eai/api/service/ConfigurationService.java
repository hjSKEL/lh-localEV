package kr.co.kevit.localcsms.eai.api.service;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.type.ResetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration 관련 OCPP 1.6 액션 서비스.
 *
 * reset / getConfiguration / changeConfiguration / updatePassword / clearCache
 */
@Service
public class ConfigurationService {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationService.class);

    private final Daemon16Client daemonClient;
    private final ChargingStationService chargingStationService;

    public ConfigurationService(Daemon16Client daemonClient,
                                ChargingStationService chargingStationService) {
        this.daemonClient = daemonClient;
        this.chargingStationService = chargingStationService;
    }

    /** Reset.req 전송 */
    public ApiResult reset(String csId, ResetType type) {
        log.info("[API] reset csId={} type={}", csId, type);
        return daemonClient.send(csId, "Reset", Map.of("type", type.name()), null);
    }

    /** GetConfiguration.req 전송 */
    public ApiResult getConfiguration(String csId, String key) {
        log.info("[API] getConfiguration csId={} key={}", csId, key);
        Map<String, Object> payload = new LinkedHashMap<>();
        if (key != null && !key.isBlank()) {
            payload.put("key", List.of(key));
        }
        return daemonClient.send(csId, "GetConfiguration", payload, null);
    }

    /** ChangeConfiguration.req 전송 */
    public ApiResult changeConfiguration(String csId, String key, String value) {
        log.info("[API] changeConfiguration csId={} key={} value={}", csId, key, value);
        return daemonClient.send(csId, "ChangeConfiguration", Map.of("key", key, "value", value), null);
    }

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int PASSWORD_LENGTH = 16;
    private final SecureRandom random = new SecureRandom();

    /**
     * 비밀번호 변경용 ChangeConfiguration.req 전송.
     *
     * 1) 16자리 랜덤 비밀번호 생성 (A-Z, 0-9)
     * 2) Hex 인코딩하여 충전기에 전달
     * 3) 평문을 lastCsPassword 에 DB 저장 (스테이징)
     * 4) 충전기 응답 Accepted → csPassword ← lastCsPassword 확정
     */
    public ApiResult updatePassword(String cpCsId) {
        log.info("[API] updatePassword cpCsId={}", cpCsId);

        int idx = cpCsId.lastIndexOf('-');
        if (idx <= 0 || idx == cpCsId.length() - 1) {
            return ApiResult.rejected("cpCsId 형식 오류: " + cpCsId);
        }
        String cpId = cpCsId.substring(0, idx);
        String csId = cpCsId.substring(idx + 1);

        ChargingStation cs = chargingStationService.retrieveChargingStationByCpIdNCsId(cpId, csId);
        if (cs == null) {
            return ApiResult.rejected("충전기를 찾을 수 없습니다: " + cpCsId);
        }

        // 1) 신규 비밀번호 생성 (A-Z, 0-9, 16자리)
        String newPassword = generatePassword();
        // 2) Hex 인코딩
        String hexPassword = toHex(newPassword);
        log.info("[API] updatePassword 신규 비밀번호 생성 완료: cpCsId={}", cpCsId);

        // 3) lastCsPassword 에 평문 저장 (스테이징)
        cs.setLastCsPassword(newPassword);
        chargingStationService.modifyChargingStation(cs);
        log.info("[API] updatePassword lastCsPassword 스테이징 완료: cpCsId={}", cpCsId);

        // 4) 충전기에 Hex 인코딩된 비밀번호 전송
        // 비동기: daemon이 전송 성공 여부만 반환, 충전기 Accepted 시 daemon ChangeConfigurationBean 에서 csPassword 확정
        return daemonClient.send(cpCsId, "ChangeConfiguration",
                Map.of("key", "AuthorizationKey", "value", hexPassword), null);
    }

    /** A-Z, 0-9 랜덤 비밀번호 생성 */
    private String generatePassword() {
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    /** 문자열을 Hex 인코딩 */
    private String toHex(String input) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    /** ClearCache.req 전송 */
    public ApiResult clearCache(String csId) {
        log.info("[API] clearCache csId={}", csId);
        return daemonClient.send(csId, "ClearCache", null, null);
    }
}

package kr.co.kevit.localcsms.eai.api.controller.ocpp16;

import kr.co.kevit.localcsms.charger.entity.domain.CsLocalCustomer;
import kr.co.kevit.localcsms.charger.entity.domain.CsLocalCustomerToken;
import kr.co.kevit.localcsms.charger.process.CsLocalCustomerService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.type.UpdateType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * LocalList 그룹 엔드포인트.
 *
 * POST /getLocalListVersion
 * POST /sendLocalList
 */
@RestController
@RequestMapping("/ocpp16")
public class LocalListController {

    private static final Logger log = LoggerFactory.getLogger(LocalListController.class);

    private final Daemon16Client daemonClient;
    private final CsLocalCustomerService csLocalCustomerService;

    public LocalListController(Daemon16Client daemonClient,
            CsLocalCustomerService csLocalCustomerService) {
        this.daemonClient = daemonClient;
        this.csLocalCustomerService = csLocalCustomerService;
    }

    /** Trigger a GetLocalListVersion.req from the CSMS. */
    @PostMapping("/getLocalListVersion")
    public ResponseEntity<ApiResult> getLocalListVersion(
            @RequestParam String chargingStationIdentity) {
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetLocalListVersion", null, null));
    }

    /** Trigger a SendLocalList.req from the CSMS. */
    @PostMapping("/sendLocalList")
    public ResponseEntity<ApiResult> sendLocalList(
            @RequestParam String chargingStationIdentity,
            @RequestParam(required = false) Integer versionNumber,
            @RequestParam UpdateType updateType,
            @RequestBody(required = false) Map<String, Object> body) {

        // 1) cpId / csId 파싱
        int idx = chargingStationIdentity.lastIndexOf('-');
        if (idx < 0) {
            return ResponseEntity.ok(ApiResult.rejected("chargingStationIdentity 형식 오류"));
        }
        String cpId = chargingStationIdentity.substring(0, idx);
        String csId = chargingStationIdentity.substring(idx + 1);

        // versionNumber 미입력 시 DB 현재 버전 + 1 자동 설정
        if (versionNumber == null) {
            CsLocalCustomer existing = csLocalCustomerService.findOne(cpId, csId);
            versionNumber = (existing != null) ? existing.getVersionNo() + 1 : 1;
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("listVersion", versionNumber);
        payload.put("updateType", updateType.name());

        if (body != null && body.containsKey("idTags")) {
            @SuppressWarnings("unchecked")
            List<String> idTokens = (List<String>) body.get("idTags");

            if (idTokens != null && !idTokens.isEmpty()) {
                // 2) DB 기존 토큰 조회 → 중복 제거용 Set 구성
                List<CsLocalCustomerToken> existing = csLocalCustomerService.findTokens(cpId, csId);
                Set<String> existingSet = new HashSet<>();
                for (CsLocalCustomerToken t : existing) {
                    existingSet.add(t.getToken());
                }
                log.info("[SendLocalList] cpId={} csId={} 입력토큰={} 기존토큰={}",
                        cpId, csId, idTokens.size(), existingSet.size());

                // 3-A) DB 저장용: updateType 무관, DB에 없는 토큰만 선별
                List<String> tokensToSave = new ArrayList<>();
                for (String token : idTokens) {
                    if (!existingSet.contains(token)) {
                        tokensToSave.add(token);
                    }
                }

                // 3-B) daemon 전송용:
                // Full → DB 중복 제외 (tokensToSave 와 동일)
                // Differential → 중복 비교 없이 입력 전체 전송
                List<String> tokensForDaemon;
                if (UpdateType.Full == updateType) {
                    tokensForDaemon = tokensToSave;
                    log.info("[SendLocalList] Full - DB저장={} daemon전송={}", tokensToSave.size(), tokensForDaemon.size());
                } else {
                    tokensForDaemon = new ArrayList<>(idTokens);
                    log.info("[SendLocalList] Differential - DB저장={} daemon전송={}", tokensToSave.size(),
                            tokensForDaemon.size());
                }

                // 4) 신규 토큰 DB 저장 (TB_CHLC002) - updateType 무관, DB에 없는 것만
                for (String token : tokensToSave) {
                    CsLocalCustomerToken t = new CsLocalCustomerToken();
                    t.setCpId(cpId);
                    t.setCsId(csId);
                    t.setToken(token);
                    t.setTokenType("ISO14443");
                    t.setTokenStatus("Accepted");
                    try {
                        csLocalCustomerService.addToken(t);
                    } catch (Exception e) {
                        log.error("[SendLocalList] 토큰 DB 저장 실패: token={} error={}", token, e.getMessage());
                    }
                }

                // 5) 헤더(TB_CHLC001) 저장 또는 버전 갱신
                // versionNo: daemon 전송 목록이 있을 때만 갱신
                try {
                    CsLocalCustomer header = csLocalCustomerService.findOne(cpId, csId);
                    if (header == null) {
                        header = new CsLocalCustomer();
                        header.setCpId(cpId);
                        header.setCsId(csId);
                        if (!tokensForDaemon.isEmpty())
                            header.setVersionNo(versionNumber);
                        header.setLastUpdateType(updateType.name());
                        header.setLastSendDate(new Date());
                        header.setWriter(new Writer(StringConstants.SYSTEM_EMPLOYEE));
                        csLocalCustomerService.saveHeader(header);
                    } else {
                        if (!tokensForDaemon.isEmpty())
                            header.setVersionNo(versionNumber);
                        header.setLastUpdateType(updateType.name());
                        header.setLastSendDate(new Date());
                        header.setWriter(new Writer(StringConstants.SYSTEM_EMPLOYEE));
                        csLocalCustomerService.updateHeader(header);
                    }
                } catch (Exception e) {
                    log.error("[SendLocalList] 헤더 DB 저장 실패: cpId={} csId={} error={}", cpId, csId, e.getMessage());
                }

                // 6) daemon 전송용 localAuthorizationList 구성
                List<Map<String, Object>> localAuthorizationList = new ArrayList<>();
                for (String token : tokensForDaemon) {
                    Map<String, Object> idTagMap = new HashMap<>();
                    idTagMap.put("idTag", token);
                    Map<String, Object> idTagInfoMap = new HashMap<>();
                    idTagInfoMap.put("status", "Accepted");
                    idTagMap.put("idTagInfo", idTagInfoMap);
                    localAuthorizationList.add(idTagMap);
                }

                if (!localAuthorizationList.isEmpty()) {
                    payload.put("localAuthorizationList", localAuthorizationList);
                }
            }
        }

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "SendLocalList", payload, null));
    }
}

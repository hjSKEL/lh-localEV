package kr.co.kevit.localcsms.adminweb.resource.charger;

import com.google.gson.Gson;
import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.share.JsonResultSet;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingProfileSearchCond;
import kr.co.kevit.localcsms.charger.process.ChargingProfileService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfileKind;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfilePurpose;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * TB_CHPF001 / TB_CHPF002 - 충전 프로파일 REST Resource
 *
 * GET    /ws/chargingProfile/list              - 페이지 목록 (cpId, csId 선택)
 * GET    /ws/chargingProfile/{profileId}       - 단건 조회 (schedule JSON 포함)
 * POST   /ws/chargingProfile                   - 등록 (PRFL_ID 자동 생성)
 * PUT    /ws/chargingProfile/{profileId}       - 수정
 * DELETE /ws/chargingProfile/{profileId}       - 삭제
 */
@RestController
@RequestMapping("ws/chargingProfile")
public class ChargingProfileResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargingProfileResource.class);

    @Autowired
    private ChargingProfileService chargingProfileService;

    @Autowired
    private SequenceService sequenceService;

    /** 목록 조회 (페이지 처리) */
    @GetMapping("/list")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Page<ChargingProfile> searchList(ChargingProfileSearchCond cond) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/chargingProfile/list DATA:{}", loginUser.getUserId(), new Gson().toJson(cond));
        try {
            Page<ChargingProfile> result = chargingProfileService.retrieveBySearchCond(cond);
            LOGGER.info("[RES] USER:{} URL:ws/chargingProfile/list SUCCESS count:{}", loginUser.getUserId(), result.getCriteria().getTotalItemCount());
            return result;
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/chargingProfile/list FAIL error={}", e.getMessage(), e);
            return null;
        }
    }

    /** 단건 조회 (schedule JSON 포함) */
    @GetMapping("/{profileId}")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Map<String, Object> findOne(@PathVariable int profileId,
                                       @RequestParam String cpId,
                                       @RequestParam String csId) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/chargingProfile/{} cpId={} csId={}", loginUser.getUserId(), profileId, cpId, csId);
        ChargingProfile p = chargingProfileService.findOne(profileId, cpId, csId);
        if (p == null) return null;
        return toMap(p);
    }

    /** 등록 (PRFL_ID 는 Sequence 자동 생성) */
    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet register(@RequestBody Map<String, Object> body) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/chargingProfile POST cpId={} csId={}", loginUser.getUserId(), body.get("cpId"), body.get("csId"));
        try {
            ChargingProfile profile = fromMap(body);
            profile.setProfileId(sequenceService.generateChargingProfileSeq());
            profile.setWriter(new Writer(loginUser.getUserId()));
            chargingProfileService.saveProfile(profile);
            LOGGER.info("[RES] USER:{} URL:ws/chargingProfile POST SUCCESS profileId={}", loginUser.getUserId(), profile.getProfileId());
            return new JsonResultSet(ResultStatus.SUCCESS, profile.getProfileId());
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/chargingProfile POST FAIL error={}", e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL, e.getMessage());
        }
    }

    /** 수정 */
    @PutMapping("/{profileId}")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet update(@PathVariable int profileId,
                                @RequestBody Map<String, Object> body) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/chargingProfile/{} PUT", loginUser.getUserId(), profileId);
        try {
            ChargingProfile profile = fromMap(body);
            profile.setProfileId(profileId);
            profile.setWriter(new Writer(loginUser.getUserId()));
            chargingProfileService.updateProfile(profile);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/chargingProfile/{} PUT FAIL error={}", profileId, e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL, e.getMessage());
        }
    }

    /** 삭제 */
    @DeleteMapping("/{profileId}")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet delete(@PathVariable int profileId,
                                @RequestParam String cpId,
                                @RequestParam String csId) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/chargingProfile/{} DELETE cpId={} csId={}", loginUser.getUserId(), profileId, cpId, csId);
        try {
            chargingProfileService.deleteProfile(profileId, cpId, csId);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/chargingProfile/{} DELETE FAIL error={}", profileId, e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL, e.getMessage());
        }
    }

    /** JS Map → ChargingProfile 도메인 변환 (purpose/kind 는 CHPP/CHKD 코드 문자열) */
    private ChargingProfile fromMap(Map<String, Object> body) {
        ChargingProfile p = new ChargingProfile();
        p.setCpId((String) body.get("cpId"));
        p.setCsId((String) body.get("csId"));
        p.setEvseId(body.get("evseId") instanceof Number ? ((Number) body.get("evseId")).intValue() : 0);
        p.setStackLevel(body.get("stackLevel") instanceof Number ? ((Number) body.get("stackLevel")).intValue() : 0);
        p.setPurpose(ChargingProfilePurpose.getTypeByCode((String) body.get("purpose")));
        p.setKind(ChargingProfileKind.getTypeByCode((String) body.get("kind")));
        p.setRecurrencyKind((String) body.get("recurrencyKind"));
        p.setValidFrom(body.get("validFrom") instanceof Number ? new Date(((Number) body.get("validFrom")).longValue()) : null);
        p.setValidTo(body.get("validTo") instanceof Number ? new Date(((Number) body.get("validTo")).longValue()) : null);
        p.setRechargingId((String) body.get("rechargingId"));
        p.setScheduleListJson((String) body.get("scheduleListJson"));
        return p;
    }

    /** ChargingProfile 도메인 → JS Map 변환 (purpose/kind 를 코드 문자열로 직렬화) */
    private Map<String, Object> toMap(ChargingProfile p) {
        java.util.LinkedHashMap<String, Object> m = new java.util.LinkedHashMap<>();
        m.put("profileId",       p.getProfileId());
        m.put("cpId",            p.getCpId());
        m.put("csId",            p.getCsId());
        m.put("evseId",          p.getEvseId());
        m.put("stackLevel",      p.getStackLevel());
        m.put("purpose",         p.getPurpose()  != null ? p.getPurpose().getCode()  : null);
        m.put("kind",            p.getKind()     != null ? p.getKind().getCode()     : null);
        m.put("recurrencyKind",  p.getRecurrencyKind());
        m.put("validFrom",       p.getValidFrom() != null ? p.getValidFrom().getTime() : null);
        m.put("validTo",         p.getValidTo()   != null ? p.getValidTo().getTime()   : null);
        m.put("rechargingId",    p.getRechargingId());
        m.put("csStatus",        p.getCsStatus());
        m.put("scheduleListJson",p.getScheduleListJson());
        if (p.getWriter() != null) {
            java.util.Map<String, Object> writer = new java.util.LinkedHashMap<>();
            writer.put("registrationDate", p.getWriter().getRegistrationDate() != null ? p.getWriter().getRegistrationDate().getTime() : null);
            writer.put("regUserId",  p.getWriter().getRegUserId());
            writer.put("updateDate", p.getWriter().getUpdateDate() != null ? p.getWriter().getUpdateDate().getTime() : null);
            writer.put("updUserId",  p.getWriter().getUpdUserId());
            m.put("writer", writer);
        }
        return m;
    }
}

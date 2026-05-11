package kr.co.kevit.localcsms.charger.process;

import kr.co.kevit.localcsms.charger.entity.domain.CsConfiguration;
import kr.co.kevit.localcsms.charger.entity.shared.CsConfigurationSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

import java.util.List;

/**
 * TB_CHCF001 - 충전기 설정정보 Service (OCPP 1.6)
 */
public interface CsConfigurationService {

    /** 단건 저장 (update 후 0건이면 insert) */
    void saveConfiguration(CsConfiguration config);

    /** GetConfiguration 응답 결과 일괄 저장 */
    void saveAll(String cpId, String csId, List<CsConfiguration> configs);

    /** PK 기준 단건 조회 */
    CsConfiguration findOne(String cpId, String csId, String configKey);

    /** 충전기 전체 설정 목록 조회 */
    List<CsConfiguration> retrieveByCpIdAndCsId(String cpId, String csId);

    /** 조건 검색 (페이지 처리) */
    Page<CsConfiguration> retrieveBySearchCond(CsConfigurationSearchCond cond);

    /** 충전기 설정 전체 삭제 */
    void deleteAllByCpIdAndCsId(String cpId, String csId);

    /** 단건 삭제 */
    void deleteOne(String cpId, String csId, String configKey);
}

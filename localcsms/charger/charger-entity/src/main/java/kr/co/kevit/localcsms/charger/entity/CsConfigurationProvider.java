package kr.co.kevit.localcsms.charger.entity;

import kr.co.kevit.localcsms.charger.entity.domain.CsConfiguration;
import kr.co.kevit.localcsms.charger.entity.shared.CsConfigurationSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

import java.util.List;

/**
 * TB_CHCF001 - 충전기 설정정보 Provider (OCPP 1.6)
 */
public interface CsConfigurationProvider {

    /** update 후 0건이면 insert */
    void saveConfiguration(CsConfiguration config);

    /** 목록 일괄 저장 */
    void saveAll(String cpId, String csId, List<CsConfiguration> configs);

    CsConfiguration findOne(String cpId, String csId, String configKey);

    List<CsConfiguration> findByCpIdAndCsId(String cpId, String csId);

    Page<CsConfiguration> findBySearchCond(CsConfigurationSearchCond cond);

    void deleteAllByCpIdAndCsId(String cpId, String csId);

    void deleteOne(String cpId, String csId, String configKey);
}

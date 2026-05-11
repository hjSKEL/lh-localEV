package kr.co.kevit.localcsms.charger.entity.dao;

import kr.co.kevit.localcsms.charger.entity.domain.CsConfiguration;
import kr.co.kevit.localcsms.charger.entity.shared.CsConfigurationSearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TB_CHCF001 - 충전기 설정정보 Mapper (OCPP 1.6)
 */
@Repository
public interface CsConfigurationMapper {

    /** 단건 upsert (ON DUPLICATE KEY UPDATE) */
    int mergeConfiguration(@Param("c") CsConfiguration config);

    /** 배치 upsert */
    int mergeBatch(@Param("list") List<CsConfiguration> list);

    /** update-then-insert: UPDATE */
    int updateConfiguration(@Param("c") CsConfiguration config);

    /** update-then-insert: INSERT */
    int insertConfiguration(@Param("c") CsConfiguration config);

    /** PK 기준 단건 조회 */
    CsConfiguration findOne(
            @Param("cpId") String cpId,
            @Param("csId") String csId,
            @Param("configKey") String configKey);

    /** 충전기 전체 설정 조회 */
    List<CsConfiguration> selectByCpIdAndCsId(
            @Param("cpId") String cpId,
            @Param("csId") String csId);

    /** 조건 검색 (Page 처리 포함) */
    List<CsConfiguration> selectBySearchCond(@Param("cond") CsConfigurationSearchCond cond);

    int countBySearchCond(@Param("cond") CsConfigurationSearchCond cond);

    /** 충전기 설정 전체 삭제 */
    int deleteAllByCpIdAndCsId(
            @Param("cpId") String cpId,
            @Param("csId") String csId);

    /** 단건 삭제 */
    int deleteOne(
            @Param("cpId") String cpId,
            @Param("csId") String csId,
            @Param("configKey") String configKey);
}

package kr.co.kevit.localcsms.charger.entity.dao;

import kr.co.kevit.localcsms.charger.entity.domain.CsVariable;
import kr.co.kevit.localcsms.charger.entity.shared.CsVariableSearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TB_CHCF002 - 충전기 설정 변수 Mapper
 */
@Repository
public interface CsVariableMapper {

    /** 단건 upsert (ON DUPLICATE KEY UPDATE) */
    int mergeVariable(@Param("v") CsVariable variable);

    /** 배치 upsert - GetVariables 응답 목록 한 번에 저장 */
    int mergeBatch(@Param("list") List<CsVariable> list);

    /** update-then-insert 방식: PK 기준 UPDATE */
    int updateVariable(@Param("v") CsVariable variable);

    /** update-then-insert 방식: INSERT */
    int insertVariable(@Param("v") CsVariable variable);

    /** PK 전체 조건으로 단건 조회 */
    CsVariable findOne(
            @Param("cpId") String cpId,
            @Param("csId") String csId,
            @Param("compNm") String compNm,
            @Param("varNm") String varNm,
            @Param("varInst") String varInst,
            @Param("attrTp") String attrTp);

    /** 충전기 전체 변수 조회 */
    List<CsVariable> selectByCpIdAndCsId(
            @Param("cpId") String cpId,
            @Param("csId") String csId);

    /** 조건 검색 (Page 처리 포함) */
    List<CsVariable> selectBySearchCond(@Param("cond") CsVariableSearchCond cond);

    int countBySearchCond(@Param("cond") CsVariableSearchCond cond);

    /** 충전기 변수 전체 삭제 (Full 동기화 전 사용) */
    int deleteAllByCpIdAndCsId(
            @Param("cpId") String cpId,
            @Param("csId") String csId);
}

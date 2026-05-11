package kr.co.kevit.localcsms.charger.entity.dao;

import kr.co.kevit.localcsms.charger.entity.domain.CsLocalCustomer;
import kr.co.kevit.localcsms.charger.entity.domain.CsLocalCustomerToken;
import kr.co.kevit.localcsms.charger.entity.shared.CsLocalCustomerSearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TB_CHLC001 / TB_CHLC002 - 충전기 로컬 고객 Mapper
 */
@Repository
public interface CsLocalCustomerMapper {

    // ── TB_CHLC001 ─────────────────────────────────────────────

    /** 헤더 INSERT */
    int insertHeader(@Param("h") CsLocalCustomer header);

    /** 헤더 UPDATE (버전/상태/전송일시) */
    int updateHeader(@Param("h") CsLocalCustomer header);

    /** 헤더 단건 조회 */
    CsLocalCustomer findHeader(@Param("cpId") String cpId,
                               @Param("csId") String csId);

    /** 헤더 조건 목록 조회 */
    List<CsLocalCustomer> selectBySearchCond(@Param("cond") CsLocalCustomerSearchCond cond);

    /** 헤더 조건 카운트 */
    int countBySearchCond(@Param("cond") CsLocalCustomerSearchCond cond);

    /** 헤더 삭제 */
    int deleteHeader(@Param("cpId") String cpId,
                     @Param("csId") String csId);

    // ── TB_CHLC002 ─────────────────────────────────────────────

    /** 토큰 단건 INSERT */
    int insertToken(@Param("t") CsLocalCustomerToken token);

    /** 토큰 INSERT ... ON DUPLICATE KEY UPDATE (Differential용 upsert) */
    int mergeToken(@Param("t") CsLocalCustomerToken token);

    /** 토큰 배치 INSERT (Full 업데이트용) */
    int insertTokenBatch(@Param("list") List<CsLocalCustomerToken> list);

    /** 특정 충전기의 토큰 전체 조회 */
    List<CsLocalCustomerToken> findTokens(@Param("cpId") String cpId,
                                          @Param("csId") String csId);

    /** 특정 충전기의 토큰 전체 삭제 (Full 업데이트 전 초기화) */
    int deleteTokens(@Param("cpId") String cpId,
                     @Param("csId") String csId);

    /** 토큰 단건 삭제 */
    int deleteToken(@Param("cpId") String cpId,
                    @Param("csId") String csId,
                    @Param("token") String token,
                    @Param("tokenType") String tokenType);
}

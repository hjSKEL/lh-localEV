package kr.co.kevit.localcsms.charger.process;

import kr.co.kevit.localcsms.charger.entity.domain.CsLocalCustomer;
import kr.co.kevit.localcsms.charger.entity.domain.CsLocalCustomerToken;
import kr.co.kevit.localcsms.charger.entity.shared.CsLocalCustomerSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

import java.util.List;

/**
 * TB_CHLC001 / TB_CHLC002 - 충전기 로컬 고객 Service
 */
public interface CsLocalCustomerService {

    /** 헤더 저장 */
    void saveHeader(CsLocalCustomer header);

    /** 헤더 수정 */
    void updateHeader(CsLocalCustomer header);

    /** 헤더 + 토큰 목록 단건 조회 */
    CsLocalCustomer findOne(String cpId, String csId);

    /** 헤더 페이지 목록 조회 */
    Page<CsLocalCustomer> retrieveBySearchCond(CsLocalCustomerSearchCond cond);

    /** 헤더 + 토큰 전체 삭제 */
    void deleteHeader(String cpId, String csId);

    /** 토큰 단건 추가 */
    void addToken(CsLocalCustomerToken token);

    /** 토큰 upsert (Differential 업데이트용) */
    void mergeToken(CsLocalCustomerToken token);

    /** 토큰 전체 교체 (Full 업데이트용) */
    void replaceAllTokens(String cpId, String csId, List<CsLocalCustomerToken> tokens);

    /** 특정 충전기 토큰 전체 조회 */
    List<CsLocalCustomerToken> findTokens(String cpId, String csId);

    /** 토큰 단건 삭제 */
    void deleteToken(String cpId, String csId, String token, String tokenType);
}

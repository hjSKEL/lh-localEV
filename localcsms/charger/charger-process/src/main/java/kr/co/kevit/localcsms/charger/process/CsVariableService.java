package kr.co.kevit.localcsms.charger.process;

import kr.co.kevit.localcsms.charger.entity.domain.CsVariable;
import kr.co.kevit.localcsms.charger.entity.shared.CsVariableSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

import java.util.List;

/**
 * TB_CHCF002 - 충전기 설정 변수 Service
 */
public interface CsVariableService {

    /**
     * 단건 저장 (update 후 0건이면 insert).
     */
    void saveVariable(CsVariable variable);

    /**
     * GetVariables / GetBaseReport 응답 결과를 일괄 저장.
     */
    void saveAll(String cpId, String csId, List<CsVariable> variables);

    /**
     * component/variable/attrTp 기준 단건 조회.
     * 없으면 null 반환.
     */
    CsVariable findOne(String cpId, String csId, String compNm, String varNm, String varInst, String attrTp);

    /**
     * 충전기 전체 변수 목록 조회.
     */
    List<CsVariable> retrieveByCpIdAndCsId(String cpId, String csId);

    /**
     * 조건 검색 (페이지 처리).
     */
    Page<CsVariable> retrieveBySearchCond(CsVariableSearchCond cond);
}

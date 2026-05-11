package kr.co.kevit.localcsms.charger.entity;

import kr.co.kevit.localcsms.charger.entity.domain.CsVariable;
import kr.co.kevit.localcsms.charger.entity.shared.CsVariableSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

import java.util.List;

/**
 * TB_CHCF002 - 충전기 설정 변수 Provider
 */
public interface CsVariableProvider {

    /** update 후 0건이면 insert (update-then-insert) */
    void saveVariable(CsVariable variable);

    /** saveVariable 을 목록 전체에 적용 */
    void saveAll(String cpId, String csId, List<CsVariable> variables);

    CsVariable findOne(String cpId, String csId, String compNm, String varNm, String varInst, String attrTp);

    List<CsVariable> findByCpIdAndCsId(String cpId, String csId);

    Page<CsVariable> findBySearchCond(CsVariableSearchCond cond);
}

package kr.co.kevit.localcsms.charger.entity.logic;

import kr.co.kevit.localcsms.charger.entity.CsVariableProvider;
import kr.co.kevit.localcsms.charger.entity.dao.CsVariableMapper;
import kr.co.kevit.localcsms.charger.entity.domain.CsVariable;
import kr.co.kevit.localcsms.charger.entity.shared.CsVariableSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TB_CHCF002 - 충전기 설정 변수 Provider 구현
 */
@Service
public class CsVariableProviderImpl implements CsVariableProvider {

    private final CsVariableMapper mapper;

    public CsVariableProviderImpl(CsVariableMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void saveVariable(CsVariable variable) {
        int updated = mapper.updateVariable(variable);
        if (updated == 0) {
            mapper.insertVariable(variable);
        }
    }

    @Override
    public void saveAll(String cpId, String csId, List<CsVariable> variables) {
        if (variables == null || variables.isEmpty()) return;
        for (CsVariable v : variables) {
            saveVariable(v);
        }
    }

    @Override
    public CsVariable findOne(String cpId, String csId, String compNm, String varNm, String varInst, String attrTp) {
        return mapper.findOne(cpId, csId, compNm, varNm, varInst, attrTp);
    }

    @Override
    public List<CsVariable> findByCpIdAndCsId(String cpId, String csId) {
        return mapper.selectByCpIdAndCsId(cpId, csId);
    }

    @Override
    public Page<CsVariable> findBySearchCond(CsVariableSearchCond cond) {
        int total = mapper.countBySearchCond(cond);
        cond.setTotalItemCount(total);
        List<CsVariable> list = total > 0 ? mapper.selectBySearchCond(cond) : new ArrayList<>();
        return new Page<>(cond, list);
    }
}

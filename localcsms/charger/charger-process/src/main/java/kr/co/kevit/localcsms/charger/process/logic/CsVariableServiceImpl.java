package kr.co.kevit.localcsms.charger.process.logic;

import kr.co.kevit.localcsms.charger.entity.CsVariableProvider;
import kr.co.kevit.localcsms.charger.entity.domain.CsVariable;
import kr.co.kevit.localcsms.charger.entity.shared.CsVariableSearchCond;
import kr.co.kevit.localcsms.charger.process.CsVariableService;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TB_CHCF002 - 충전기 설정 변수 Service 구현
 */
@Service
public class CsVariableServiceImpl implements CsVariableService {

    private final CsVariableProvider csVariableProvider;

    public CsVariableServiceImpl(CsVariableProvider csVariableProvider) {
        this.csVariableProvider = csVariableProvider;
    }

    @Override
    public void saveVariable(CsVariable variable) {
        csVariableProvider.saveVariable(variable);
    }

    @Override
    public void saveAll(String cpId, String csId, List<CsVariable> variables) {
        csVariableProvider.saveAll(cpId, csId, variables);
    }

    @Override
    public CsVariable findOne(String cpId, String csId, String compNm, String varNm, String varInst, String attrTp) {
        return csVariableProvider.findOne(cpId, csId, compNm, varNm, varInst, attrTp);
    }

    @Override
    public List<CsVariable> retrieveByCpIdAndCsId(String cpId, String csId) {
        return csVariableProvider.findByCpIdAndCsId(cpId, csId);
    }

    @Override
    public Page<CsVariable> retrieveBySearchCond(CsVariableSearchCond cond) {
        return csVariableProvider.findBySearchCond(cond);
    }
}

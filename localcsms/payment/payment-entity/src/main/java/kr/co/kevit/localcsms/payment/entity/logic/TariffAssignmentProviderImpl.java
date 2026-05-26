/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.logic;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.TariffAssignmentProvider;
import kr.co.kevit.localcsms.payment.entity.dao.TariffAssignmentMapper;
import kr.co.kevit.localcsms.payment.entity.domain.TariffAssignment;
import kr.co.kevit.localcsms.payment.entity.shared.TariffAssignmentDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffAssignmentSearchCond;

@Component
public class TariffAssignmentProviderImpl implements TariffAssignmentProvider {

    @Autowired
    private TariffAssignmentMapper mapper;

    @Override
    public void registerAssignment(TariffAssignment assignment) {
        mapper.insertAssignment(assignment);
    }

    @Override
    public int modifyStatus(long seq, String statusCd, Date csAckDt, String reasonCd, String updUserId) {
        return mapper.updateStatus(seq, statusCd, csAckDt, reasonCd, updUserId);
    }

    @Override
    public int modifyStatusForActiveDriver(String customerId, String newStatus, String updUserId) {
        return mapper.updateStatusForActiveDriver(customerId, newStatus, updUserId);
    }

    @Override
    public int modifyStatusForActiveDefault(String cpId, String csId, Integer evseId, String newStatus,
                                            String updUserId) {
        return mapper.updateStatusForActiveDefault(cpId, csId, evseId, newStatus, updUserId);
    }

    @Override
    public TariffAssignment retrieveAssignment(long seq) {
        return mapper.selectAssignment(seq);
    }

    @Override
    public TariffAssignment retrieveActiveDriverByCustomerId(String customerId) {
        return mapper.selectActiveDriverByCustomerId(customerId);
    }

    @Override
    public TariffAssignment retrieveActiveDefaultByEvse(String cpId, String csId, Integer evseId) {
        return mapper.selectActiveDefaultByEvse(cpId, csId, evseId);
    }

    @Override
    public Page<TariffAssignmentDto> retrieveAssignmentBySearchCond(TariffAssignmentSearchCond searchCond) {
        int totCnt = mapper.countAssignmentBySearchCond(searchCond);
        Page<TariffAssignmentDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<TariffAssignmentDto> result = mapper.selectAssignmentBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }
}

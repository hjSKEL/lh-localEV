/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.derctrl.entity.logic;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.derctrl.entity.DerControlProvider;
import kr.co.kevit.localcsms.derctrl.entity.dao.DerControlMapper;
import kr.co.kevit.localcsms.derctrl.entity.domain.DerControl;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerControlDto;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerControlSearchCond;

@Component
public class DerControlProviderImpl implements DerControlProvider {

    @Autowired
    private DerControlMapper mapper;

    @Override
    public void registerDerControl(DerControl ctrl) {
        mapper.insertDerControl(ctrl);
    }

    @Override
    public int modifyStatus(String controlId, String originCd, String statusCd,
                            Date csAckDt, String reasonCd, String updUserId) {
        return mapper.updateStatus(controlId, originCd, statusCd, csAckDt, reasonCd, updUserId);
    }

    @Override
    public int modifyStatusForActive(String cpId, String csId, String controlType, String isDefault,
                                     String originCd, String newStatus, String updUserId) {
        return mapper.updateStatusForActive(cpId, csId, controlType, isDefault, originCd, newStatus, updUserId);
    }

    @Override
    public DerControl retrieveDerControl(String controlId, String originCd) {
        return mapper.selectDerControl(controlId, originCd);
    }

    @Override
    public DerControl retrieveActive(String cpId, String csId, String controlType, String isDefault,
                                     String originCd) {
        return mapper.selectActive(cpId, csId, controlType, isDefault, originCd);
    }

    @Override
    public Page<DerControlDto> retrieveDerControlBySearchCond(DerControlSearchCond cond) {
        int totCnt = mapper.countDerControlBySearchCond(cond);
        Page<DerControlDto> resultSet = new Page<>();
        resultSet.setCriteria(cond);
        cond.setTotalItemCount(totCnt);
        if (totCnt == 0) return resultSet;
        List<DerControlDto> result = mapper.selectDerControlBySearchCond(cond);
        resultSet.setResult(result);
        return resultSet;
    }
}

/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.VatHisProvider;
import kr.co.kevit.localcsms.payment.entity.dao.VatHisMapper;
import kr.co.kevit.localcsms.payment.entity.domain.VatHis;
import kr.co.kevit.localcsms.payment.entity.shared.VatHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.VatHisSearchCond;

/**
 * @author bckim
 * @since 2026. 5. 24.
 */
@Component
public class VatHisProviderImpl implements VatHisProvider {

    @Autowired
    private VatHisMapper mapper;

    @Override
    public void registerVatHis(VatHis his) {
        mapper.insertVatHis(his);
    }

    @Override
    public Page<VatHisDto> retrieveVatHisBySearchCond(VatHisSearchCond searchCond) {
        int totCnt = mapper.countVatHisBySearchCond(searchCond);
        Page<VatHisDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<VatHisDto> result = mapper.selectVatHisBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }
}

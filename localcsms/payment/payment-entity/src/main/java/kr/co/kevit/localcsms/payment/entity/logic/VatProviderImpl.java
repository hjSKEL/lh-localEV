/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.VatProvider;
import kr.co.kevit.localcsms.payment.entity.dao.VatMapper;
import kr.co.kevit.localcsms.payment.entity.domain.Vat;
import kr.co.kevit.localcsms.payment.entity.shared.VatDto;
import kr.co.kevit.localcsms.payment.entity.shared.VatSearchCond;

/**
 * @author bckim
 * @since 2026. 5. 24.
 */
@Component
public class VatProviderImpl implements VatProvider {

    @Autowired
    private VatMapper mapper;

    @Override
    public void registerVat(Vat vat) {
        mapper.insertVat(vat);
    }

    @Override
    public int modifyVat(Vat vat) {
        return mapper.updateVat(vat);
    }

    @Override
    public int modifyUseYn(String vatNo, String useYn, String updUserId) {
        return mapper.updateUseYn(vatNo, useYn, updUserId);
    }

    @Override
    public Vat retrieveVat(String vatNo) {
        return mapper.selectVat(vatNo);
    }

    @Override
    public Page<VatDto> retrieveVatBySearchCond(VatSearchCond searchCond) {
        int totCnt = mapper.countVatBySearchCond(searchCond);
        Page<VatDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<VatDto> result = mapper.selectVatBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }
}

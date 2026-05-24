/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.process.logic;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.VatHisProvider;
import kr.co.kevit.localcsms.payment.entity.VatProvider;
import kr.co.kevit.localcsms.payment.entity.domain.Vat;
import kr.co.kevit.localcsms.payment.entity.domain.VatHis;
import kr.co.kevit.localcsms.payment.entity.shared.VatDto;
import kr.co.kevit.localcsms.payment.entity.shared.VatHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.VatHisSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.VatSearchCond;
import kr.co.kevit.localcsms.payment.process.VatService;

/**
 * @author bckim
 * @since 2026. 5. 24.
 */
@Service
@Transactional
public class VatServiceImpl implements VatService {

    @Autowired
    private VatProvider vatProvider;

    @Autowired
    private VatHisProvider vatHisProvider;

    @Override
    public void registerVat(Vat vat) {
        validateRequired(vat);
        if (vatProvider.retrieveVat(vat.getVatNo()) != null) {
            throw new KEVITException("이미 등록된 사업자번호 입니다.");
        }
        if (vat.getUseYn() == null || vat.getUseYn().isEmpty()) {
            vat.setUseYn(Vat.USE_Y);
        }
        if (vat.getCountry() == null || vat.getCountry().isEmpty()) {
            vat.setCountry("KR");
        }
        vatProvider.registerVat(vat);
    }

    @Override
    public void modifyVat(Vat vat) {
        validateRequired(vat);
        if (vatProvider.retrieveVat(vat.getVatNo()) == null) {
            throw new KEVITException("등록되지 않은 사업자번호 입니다.");
        }
        int affected = vatProvider.modifyVat(vat);
        if (affected != 1) {
            throw new KEVITException("사업자 정보 수정 실패. VAT=" + vat.getVatNo());
        }
    }

    @Override
    public void modifyUseYn(String vatNo, String useYn, String updUserId) {
        if (vatNo == null || vatNo.isEmpty()) {
            throw new KEVITException("사업자번호가 비어 있습니다.");
        }
        if (!Vat.USE_Y.equals(useYn) && !Vat.USE_N.equals(useYn)) {
            throw new KEVITException("사용여부는 Y 또는 N 이어야 합니다.");
        }
        if (vatProvider.retrieveVat(vatNo) == null) {
            throw new KEVITException("등록되지 않은 사업자번호 입니다.");
        }
        int affected = vatProvider.modifyUseYn(vatNo, useYn, updUserId);
        if (affected != 1) {
            throw new KEVITException("사용여부 변경 실패. VAT=" + vatNo);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Vat retrieveVat(String vatNo) {
        return vatProvider.retrieveVat(vatNo);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<VatDto> retrieveVatBySearchCond(VatSearchCond searchCond) {
        return vatProvider.retrieveVatBySearchCond(searchCond);
    }

    @Override
    public void recordVatValidation(VatHis his) {
        if (his == null) return;
        if (his.getVerifiedDt() == null) his.setVerifiedDt(new Date());
        vatHisProvider.registerVatHis(his);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<VatHisDto> retrieveVatHisBySearchCond(VatHisSearchCond searchCond) {
        return vatHisProvider.retrieveVatHisBySearchCond(searchCond);
    }

    private void validateRequired(Vat vat) {
        if (vat == null) throw new KEVITException("입력값이 비어 있습니다.");
        if (isBlank(vat.getVatNo()))     throw new KEVITException("사업자번호가 비어 있습니다.");
        if (isBlank(vat.getCompanyNm())) throw new KEVITException("회사명이 비어 있습니다.");
        if (isBlank(vat.getAddr1()))     throw new KEVITException("주소가 비어 있습니다.");
        if (isBlank(vat.getCity()))      throw new KEVITException("시(city)가 비어 있습니다.");
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}

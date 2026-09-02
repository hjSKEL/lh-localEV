/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.common.domain.Code;
import kr.co.kevit.localcsms.common.entity.CodeProvider;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.enumtype.charger.RechargingStatus;
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.localcsms.customer.entity.domain.Customer;
import kr.co.kevit.localcsms.customer.external.CustomerExtProcess;
import kr.co.kevit.localcsms.organization.entity.domain.Company;
import kr.co.kevit.localcsms.organization.external.CompanyExtProcess;
import kr.co.kevit.localcsms.recharger.entity.RechargingProvider;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingMonthlyCustomerDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingSearchCond;
import kr.co.kevit.localcsms.recharger.process.RechargingService;
import java.time.YearMonth;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 17.
 */
@Service
@Transactional
public class RechargingServiceImpl implements RechargingService{
    
    @Autowired
    private RechargingProvider provider;
    
    @Autowired
    private CustomerExtProcess custExtProcess;
    
    @Autowired
    private CompanyExtProcess comExtProcess;
    
    @Autowired
    private CodeProvider codeProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerRecharging(Recharging recharging) {
        // 
        if(StringUtils.isEmpty(recharging.getRechargingId())) {
            StringBuilder buffer = new StringBuilder(22);
            buffer.append(recharging.getCpId());
            buffer.append(recharging.getCsId());
            buffer.append(DateUtils.getCurrentDateAsString(DateUtils.YYYYMMDDHHMMSS));
            recharging.setRechargingId(buffer.toString());
        }
            
        provider.registerRecharging(recharging);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyRecharging(Recharging recharging) {
        //
        provider.modifyRecharging(recharging);
    }

    @Override
    public void modifyMaxEnergy(String rechargingId, Double maxEnergy, String updUserId) {
        if (rechargingId == null || rechargingId.isEmpty()) {
            throw new KEVITException("충전ID가 비어 있습니다.");
        }
        if (maxEnergy == null || maxEnergy < 0) {
            throw new KEVITException("최대 에너지 한도는 0 이상이어야 합니다. (0 = 한도 없음)");
        }
        Recharging rc = provider.retrieveRechargingById(rechargingId);
        if (rc == null) {
            throw new KEVITException("존재하지 않는 충전ID 입니다. " + rechargingId);
        }
        if (!RechargingStatus.RECS02.getCode().equals(rc.getChStatCode())) {
            throw new KEVITException("진행 중인 충전만 한도 변경이 가능합니다. 상태=" + rc.getChStatCode());
        }
        int affected = provider.modifyMaxEnergy(rechargingId, maxEnergy);
        if (affected != 1) {
            throw new KEVITException("최대 에너지 한도 변경에 실패했습니다. ID=" + rechargingId);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Recharging retrieveRecharging4IfById(String id) {
        // 
        return provider.retrieveRechargingById(id);
    }
    

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public RechargingDto retrieveRechargingById(String id) {
        // 
        RechargingDto recharging = provider.retrieveRechargingDtoById(id);
//        if(recharging == null)return null;
//        recharging.setChargingStation(chargerExtProcess.retrieveChargerByCpIdNCsId(recharging.getCpId(), recharging.getCsId()));
//        recharging.getChargingStation().setChargePoint(cpExtProcess.retrieveChargePoint(recharging.getCpId()));
//        PaymentCard paymentCard = pcExtProcess.retrievePaymentCardByCustomerId(recharging.getCustomerId());
//        if (paymentCard != null) {
//            recharging.setCardNo(paymentCard.getCardNo());
//        }
        return recharging;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<RechargingDto> retrieveRechargingByRechargingSearchCond(RechargingSearchCond searchCond) {
        // 
        return provider.retrieveRechargingByRechargingSearchCond(searchCond);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<RechargingDto> retrieveRechargingDtoByIds(List<String> ids) {
        // 
        return provider.retrieveRechargingDtoByIds(ids);
    }

    @Override
    public void completeRecharging(Recharging recharging) {
        Recharging oldRecharging = provider.retrieveRechargingById(recharging.getRechargingId());
        if(oldRecharging == null) {
            throw new KEVITException("RCRC001", "충전회차가 존재하지 않습니다.");
        }
        if(!(RechargingStatus.RECS01.getCode().equals(oldRecharging.getChStatCode()) || RechargingStatus.RECS02.getCode().equals(oldRecharging.getChStatCode()))) {
            throw new KEVITException("RCRC002", "충전시작 또는 충전 중인 것만 수정할 수 있습니다.");
        }
        oldRecharging.setChStatCode(RechargingStatus.RECS03.getCode());
        oldRecharging.setChEndDate(recharging.getChEndDate());
        oldRecharging.setChUseAmount(recharging.getChUseAmount());
        oldRecharging.setChUseUnitCost(recharging.getChUseUnitCost());
        oldRecharging.setChUseCost(recharging.getChUseCost());
        oldRecharging.setPaySum(recharging.getPaySum());
        provider.modifyRecharging(oldRecharging);
        provider.registerRechargingError(recharging);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<RechargingDto> retrieveRechargingWithCustomerByRechargingSearchCond(RechargingSearchCond searchCond) {
        // 
        Page<RechargingDto> resultSet = provider.retrieveRechargingByRechargingSearchCond(searchCond);
        List<RechargingDto> resultList = resultSet.getResult();
        if(resultList == null || resultList.isEmpty())
            return resultSet;
        
        Map<String, Customer> mapCust = new HashMap<>();
        Map<String, Company> mapCom = new HashMap<>();
        for(RechargingDto result : resultList) {
            Customer customer = mapCust.get(result.getCustomerId());
            if(customer == null && !mapCust.containsKey(result.getCustomerId())) {
                // 원장(TB_CUCU001)에 없는 CUT_ID를 참조하는 이력이 있을 수 있음 - null도 캐시해 반복조회 방지
                customer = custExtProcess.retrieveCustomerByUserId(result.getCustomerId());
                mapCust.put(result.getCustomerId(), customer);
            }
            if(customer != null) {
                result.setCustName(customer.getCustName());
                result.setCellphone(customer.getMblPhoneNo());
            }

            Company company = mapCom.get(result.getCompanyId());
            if(company == null && !mapCom.containsKey(result.getCompanyId())) {
                company = comExtProcess.retrieveCompanyById(result.getCompanyId());
                mapCom.put(result.getCompanyId(), company);
            }
            if(company != null) {
                result.setCompanyName(company.getCompanyName());
            }
        }
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<RechargingDto> retrieveRecharging4DownloadByRechargingSearchCond(RechargingSearchCond searchCond) {
        //  
        List<RechargingDto> resultList = provider.retrieveRecharging4DownloadByRechargingSearchCond(searchCond);
        if(resultList.isEmpty())
            return resultList;
        
        List<Code> codeList = codeProvider.retrieveCodeByParentCode("RECS00");
        for(RechargingDto result : resultList) {
            result.setChStartDateStr(DateUtils.dateToString(result.getChStartDate(), DateUtils.DATE_TIME_FORMAT));
            result.setChEndDateStr(DateUtils.dateToString(result.getChEndDate(), DateUtils.DATE_TIME_FORMAT));
            result.setChStatCode(getCodeDesc(result.getChStatCode(), codeList));
        }
        return resultList;
        
    }
    
    private static String getCodeDesc(String subCode, List<Code> codeList) {
        for(Code code : codeList) {
            if(code.getCode().equals(subCode)) {
                return code.getCodeName();
            }
        }
        return StringConstants.BLANK;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<RechargingDto> retrieveRechargingWithCustomer4DownloadByRechargingSearchCond(RechargingSearchCond searchCond) {
        //
        List<RechargingDto> resultList = provider.retrieveRecharging4DownloadByRechargingSearchCond(searchCond);
        if(resultList.isEmpty())
            return resultList;
        
        List<Code> codeList = codeProvider.retrieveCodeByParentCode("RECS00");
        Map<String, Customer> mapCust = new HashMap<>();
        Map<String, Company> mapCom = new HashMap<>();
        for(RechargingDto result : resultList) {
            Customer customer = mapCust.get(result.getCustomerId());
            if(customer == null && !mapCust.containsKey(result.getCustomerId())) {
                // 원장(TB_CUCU001)에 없는 CUT_ID를 참조하는 이력이 있을 수 있음 - null도 캐시해 반복조회 방지
                customer = custExtProcess.retrieveCustomerByUserId(result.getCustomerId());
                mapCust.put(result.getCustomerId(), customer);
            }
            if(customer != null) {
                result.setCustName(customer.getCustName());
                result.setCellphone(customer.getMblPhoneNo());
                result.setMblPhoneNo(customer.getMblPhoneNo());
            }

            Company company = mapCom.get(result.getCompanyId());
            if(company == null && !mapCom.containsKey(result.getCompanyId())) {
                company = comExtProcess.retrieveCompanyById(result.getCompanyId());
                mapCom.put(result.getCompanyId(), company);
            }
            if(company != null) {
                result.setCompanyName(company.getCompanyName());
            }
            result.setChStatCode(getCodeDesc(result.getChStatCode(), codeList));
            result.setChStartDateStr(DateUtils.dateToString(result.getChStartDate(), DateUtils.DATE_TIME_FORMAT));
            result.setChEndDateStr(DateUtils.dateToString(result.getChEndDate(), DateUtils.DATE_TIME_FORMAT));
        }
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<RechargingMonthlyCustomerDto> retrieveMonthlyCustomerSummary4Download(int year, int month) {
        //
        int lastDay = YearMonth.of(year, month).lengthOfMonth();
        String fromDate = String.format("%04d%02d01000000", year, month);
        String toDate = String.format("%04d%02d%02d235959", year, month, lastDay);

        List<RechargingMonthlyCustomerDto> resultList = provider.retrieveMonthlyCustomerSummary(fromDate, toDate);
        for (RechargingMonthlyCustomerDto result : resultList) {
            result.setYear(year);
            result.setMonth(month);
        }
        return resultList;
    }
}

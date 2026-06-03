/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.process.logic;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.customer.entity.CustomerVehicleProvider;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerVehicle;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerVehicleSearchCond;
import kr.co.kevit.localcsms.customer.process.CustomerVehicleService;

/**
 * 고객 차량(EVCCID) Service 구현.
 *
 * @author bckim
 */
@Service
@Transactional
public class CustomerVehicleServiceImpl implements CustomerVehicleService {

    @Autowired
    private CustomerVehicleProvider provider;

    @Override
    public void registerVehicle(CustomerVehicle vehicle) {
        if (vehicle == null || vehicle.getEvccId() == null || vehicle.getEvccId().isEmpty()) {
            throw new KEVITException("EVCCID는 필수입니다.");
        }
        if (vehicle.getCustomerId() == null || vehicle.getCustomerId().isEmpty()) {
            throw new KEVITException("고객 ID는 필수입니다.");
        }
        CustomerVehicle exists = provider.retrieveVehicle(vehicle.getEvccId());
        if (exists != null) {
            throw new KEVITException("이미 등록된 EVCCID 입니다: " + vehicle.getEvccId());
        }
        if (vehicle.getWriter() != null && vehicle.getWriter().getRegistrationDate() == null) {
            vehicle.getWriter().setRegistrationDate(new Date());
            vehicle.getWriter().setUpdateDate(new Date());
        }
        provider.registerVehicle(vehicle);
    }

    @Override
    public void modifyVehicle(CustomerVehicle vehicle) {
        if (vehicle == null || vehicle.getEvccId() == null || vehicle.getEvccId().isEmpty()) {
            throw new KEVITException("EVCCID는 필수입니다.");
        }
        if (vehicle.getWriter() != null) {
            vehicle.getWriter().setUpdateDate(new Date());
        }
        provider.modifyVehicle(vehicle);
    }

    @Override
    public void removeVehicle(String evccId) {
        if (evccId == null || evccId.isEmpty()) {
            throw new KEVITException("EVCCID는 필수입니다.");
        }
        provider.removeVehicle(evccId);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerVehicle retrieveVehicle(String evccId) {
        return provider.retrieveVehicle(evccId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerVehicle> retrieveVehiclesByCustomerId(String customerId) {
        return provider.retrieveVehiclesByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CustomerVehicle> retrieveVehiclesBySearchCond(CustomerVehicleSearchCond searchCond) {
        return provider.retrieveVehiclesBySearchCond(searchCond);
    }

    @Transactional
    @Override
    public int accumulateReward(String evccId, java.math.BigDecimal reward, String updUserId) {
        if (evccId == null || evccId.isEmpty() || reward == null
                || reward.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            return 0;
        }
        return provider.accumulateReward(evccId, reward, updUserId);
    }
}

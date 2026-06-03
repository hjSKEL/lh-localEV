/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.customer.entity.CustomerVehicleProvider;
import kr.co.kevit.localcsms.customer.entity.dao.CustomerVehicleMapper;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerVehicle;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerVehicleSearchCond;

/**
 * 고객 차량(EVCCID) Provider 구현.
 *
 * @author bckim
 */
@Component
public class CustomerVehicleProviderImpl implements CustomerVehicleProvider {

    @Autowired
    private CustomerVehicleMapper mapper;

    @Override
    public void registerVehicle(CustomerVehicle vehicle) {
        mapper.insertVehicle(vehicle);
    }

    @Override
    public void modifyVehicle(CustomerVehicle vehicle) {
        mapper.updateVehicle(vehicle);
    }

    @Override
    public void removeVehicle(String evccId) {
        mapper.deleteVehicle(evccId);
    }

    @Override
    public CustomerVehicle retrieveVehicle(String evccId) {
        return mapper.selectByEvccId(evccId);
    }

    @Override
    public List<CustomerVehicle> retrieveVehiclesByCustomerId(String customerId) {
        return mapper.selectByCustomerId(customerId);
    }

    @Override
    public Page<CustomerVehicle> retrieveVehiclesBySearchCond(CustomerVehicleSearchCond searchCond) {
        int totCnt = mapper.countBySearchCond(searchCond);
        Page<CustomerVehicle> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<CustomerVehicle> result = mapper.selectBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }
}

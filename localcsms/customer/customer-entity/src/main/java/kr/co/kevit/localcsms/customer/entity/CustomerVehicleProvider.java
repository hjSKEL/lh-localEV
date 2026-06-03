/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity;

import java.util.List;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerVehicle;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerVehicleSearchCond;

/**
 * 고객 차량(EVCCID) Provider.
 *
 * @author bckim
 */
public interface CustomerVehicleProvider {

    void registerVehicle(CustomerVehicle vehicle);

    void modifyVehicle(CustomerVehicle vehicle);

    void removeVehicle(String evccId);

    CustomerVehicle retrieveVehicle(String evccId);

    /** 고객 보유 차량 목록 */
    List<CustomerVehicle> retrieveVehiclesByCustomerId(String customerId);

    /** 검색조건 페이지 목록 */
    Page<CustomerVehicle> retrieveVehiclesBySearchCond(CustomerVehicleSearchCond searchCond);
}

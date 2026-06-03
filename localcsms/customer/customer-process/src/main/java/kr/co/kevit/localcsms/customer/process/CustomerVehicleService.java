/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.process;

import java.util.List;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerVehicle;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerVehicleSearchCond;

/**
 * 고객 차량(EVCCID) Service.
 *
 * @author bckim
 */
public interface CustomerVehicleService {

    /** 등록 — EVCCID 중복 시 예외 */
    void registerVehicle(CustomerVehicle vehicle);

    /** 수정 */
    void modifyVehicle(CustomerVehicle vehicle);

    /** 삭제 (EVCCID 기준) */
    void removeVehicle(String evccId);

    /** EVCCID 단건 조회 */
    CustomerVehicle retrieveVehicle(String evccId);

    /** 고객 보유 차량 목록 */
    List<CustomerVehicle> retrieveVehiclesByCustomerId(String customerId);

    /** 검색조건 페이지 목록 */
    Page<CustomerVehicle> retrieveVehiclesBySearchCond(CustomerVehicleSearchCond searchCond);
}

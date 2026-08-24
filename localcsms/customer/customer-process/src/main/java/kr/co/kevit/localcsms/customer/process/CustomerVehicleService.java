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

    /** 등록 — VIN 중복 시 예외 */
    void registerVehicle(CustomerVehicle vehicle);

    /** 수정 */
    void modifyVehicle(CustomerVehicle vehicle);

    /** 삭제 (VIN 기준) */
    void removeVehicle(String vinNo);

    /** VIN 단건 조회 */
    CustomerVehicle retrieveVehicle(String vinNo);

    /** 차량번호 단건 조회 (중복확인용) */
    CustomerVehicle retrieveVehicleByCarNo(String carNo);

    /** 고객 보유 차량 목록 */
    List<CustomerVehicle> retrieveVehiclesByCustomerId(String customerId);

    /** 검색조건 페이지 목록 */
    Page<CustomerVehicle> retrieveVehiclesBySearchCond(CustomerVehicleSearchCond searchCond);

    /** 누적 방전 보상금 적립 (V2G) */
    int accumulateReward(String vinNo, java.math.BigDecimal reward, String updUserId);
}

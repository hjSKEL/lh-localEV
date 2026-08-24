/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.customer.entity.domain.CustomerVehicle;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerVehicleSearchCond;

/**
 * 고객 차량 (EVCCID) Mapper. TB : TB_CUEV001
 *
 * @author bckim
 */
@Repository
public interface CustomerVehicleMapper {

    int insertVehicle(@Param("vehicle") CustomerVehicle vehicle);

    int updateVehicle(@Param("vehicle") CustomerVehicle vehicle);

    int deleteVehicle(@Param("vinNo") String vinNo);

    /** VIN 단건 조회 */
    CustomerVehicle selectByVinNo(@Param("vinNo") String vinNo);

    /** 차량번호 단건 조회 (중복확인용) */
    CustomerVehicle selectByCarNo(@Param("carNo") String carNo);

    /** 고객 ID 기준 보유 차량 목록 */
    List<CustomerVehicle> selectByCustomerId(@Param("customerId") String customerId);

    int countBySearchCond(@Param("searchCond") CustomerVehicleSearchCond searchCond);

    List<CustomerVehicle> selectBySearchCond(@Param("searchCond") CustomerVehicleSearchCond searchCond);

    /** 누적 방전 보상금 적립 (V2G) */
    int accumulateReward(@Param("vinNo") String vinNo,
                         @Param("reward") java.math.BigDecimal reward,
                         @Param("updUserId") String updUserId);
}

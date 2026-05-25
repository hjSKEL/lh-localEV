/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.derctrl.entity.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.derctrl.entity.domain.DerControl;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerControlDto;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerControlSearchCond;

@Repository
public interface DerControlMapper {

    int insertDerControl(@Param("ctrl") DerControl ctrl);

    int updateStatus(@Param("controlId") String controlId,
                     @Param("originCd") String originCd,
                     @Param("statusCd") String statusCd,
                     @Param("csAckDt") Date csAckDt,
                     @Param("reasonCd") String reasonCd,
                     @Param("updUserId") String updUserId);

    int updateStatusForActive(@Param("cpId") String cpId,
                              @Param("csId") String csId,
                              @Param("controlType") String controlType,
                              @Param("isDefault") String isDefault,
                              @Param("originCd") String originCd,
                              @Param("newStatus") String newStatus,
                              @Param("updUserId") String updUserId);

    DerControl selectDerControl(@Param("controlId") String controlId,
                                @Param("originCd") String originCd);

    DerControl selectActive(@Param("cpId") String cpId,
                            @Param("csId") String csId,
                            @Param("controlType") String controlType,
                            @Param("isDefault") String isDefault,
                            @Param("originCd") String originCd);

    int countDerControlBySearchCond(@Param("searchCond") DerControlSearchCond searchCond);

    List<DerControlDto> selectDerControlBySearchCond(@Param("searchCond") DerControlSearchCond searchCond);
}

/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.payment.entity.domain.TariffAssignment;
import kr.co.kevit.localcsms.payment.entity.shared.TariffAssignmentDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffAssignmentSearchCond;

@Repository
public interface TariffAssignmentMapper {

    int insertAssignment(@Param("assignment") TariffAssignment assignment);

    int updateStatus(@Param("seq") long seq,
                     @Param("statusCd") String statusCd,
                     @Param("csAckDt") Date csAckDt,
                     @Param("reasonCd") String reasonCd,
                     @Param("updUserId") String updUserId);

    int updateStatusForActiveDriver(@Param("idToken") String idToken,
                                    @Param("newStatus") String newStatus,
                                    @Param("updUserId") String updUserId);

    int updateStatusForActiveDefault(@Param("cpId") String cpId,
                                     @Param("csId") String csId,
                                     @Param("evseId") Integer evseId,
                                     @Param("newStatus") String newStatus,
                                     @Param("updUserId") String updUserId);

    TariffAssignment selectAssignment(@Param("seq") long seq);

    TariffAssignment selectActiveDriverByIdToken(@Param("idToken") String idToken);

    TariffAssignment selectActiveDefaultByEvse(@Param("cpId") String cpId,
                                               @Param("csId") String csId,
                                               @Param("evseId") Integer evseId);

    int countAssignmentBySearchCond(@Param("searchCond") TariffAssignmentSearchCond searchCond);

    List<TariffAssignmentDto> selectAssignmentBySearchCond(@Param("searchCond") TariffAssignmentSearchCond searchCond);
}

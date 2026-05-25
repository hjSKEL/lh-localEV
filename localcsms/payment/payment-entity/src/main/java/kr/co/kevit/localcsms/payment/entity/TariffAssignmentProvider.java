/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity;

import java.util.Date;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.TariffAssignment;
import kr.co.kevit.localcsms.payment.entity.shared.TariffAssignmentDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffAssignmentSearchCond;

/**
 * @author bckim
 * @since 2026. 5. 25.
 */
public interface TariffAssignmentProvider {

    void registerAssignment(TariffAssignment assignment);

    int modifyStatus(long seq, String statusCd, Date csAckDt, String reasonCd, String updUserId);

    int modifyStatusForActiveDriver(String idToken, String newStatus, String updUserId);

    int modifyStatusForActiveDefault(String cpId, String csId, Integer evseId, String newStatus, String updUserId);

    TariffAssignment retrieveAssignment(long seq);

    TariffAssignment retrieveActiveDriverByIdToken(String idToken);

    TariffAssignment retrieveActiveDefaultByEvse(String cpId, String csId, Integer evseId);

    Page<TariffAssignmentDto> retrieveAssignmentBySearchCond(TariffAssignmentSearchCond searchCond);
}

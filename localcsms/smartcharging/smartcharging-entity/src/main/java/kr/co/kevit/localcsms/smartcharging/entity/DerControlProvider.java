/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.smartcharging.entity;

import java.util.Date;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerControl;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerControlDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerControlSearchCond;

public interface DerControlProvider {

    void registerDerControl(DerControl ctrl);

    int modifyStatus(String controlId, String originCd, String statusCd,
                     Date csAckDt, String reasonCd, String updUserId);

    /** 같은 (cpId, csId, controlType, isDefault, originCd) 의 ACTIVE 행을 REPLACED 로 전이 */
    int modifyStatusForActive(String cpId, String csId, String controlType, String isDefault,
                              String originCd, String newStatus, String updUserId);

    DerControl retrieveDerControl(String controlId, String originCd);

    DerControl retrieveActive(String cpId, String csId, String controlType, String isDefault, String originCd);

    Page<DerControlDto> retrieveDerControlBySearchCond(DerControlSearchCond cond);
}

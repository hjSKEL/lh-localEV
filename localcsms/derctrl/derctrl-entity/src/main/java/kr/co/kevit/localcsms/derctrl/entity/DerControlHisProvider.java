/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.derctrl.entity;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.derctrl.entity.domain.DerControlHis;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerControlHisDto;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerControlHisSearchCond;

public interface DerControlHisProvider {

    void registerHis(DerControlHis his);

    Page<DerControlHisDto> retrieveHisBySearchCond(DerControlHisSearchCond cond);
}

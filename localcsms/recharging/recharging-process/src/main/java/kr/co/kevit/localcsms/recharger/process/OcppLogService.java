/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.OcppLog;
import kr.co.kevit.localcsms.recharger.entity.shared.OcppLogSearchCond;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 7. 2.
 */
public interface OcppLogService {

    void registerOcppLog(OcppLog log);

    Page<OcppLog> retrieveOcppLogByOcppLogSearchCond(OcppLogSearchCond searchCond);
}

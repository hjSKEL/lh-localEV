/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCardHis;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardHisSearchCond;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface PrepaidCardHisProvider {

    void registerPrepaidCardHis(PrepaidCardHis history);

    PrepaidCardHis retrievePrepaidCardHis(Long seq);

    Page<PrepaidCardHisDto> retrievePrepaidCardHisBySearchCond(PrepaidCardHisSearchCond searchCond);

}

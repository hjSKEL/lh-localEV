/*******************************************************************************
 * Copyright(c) 2019 AEA All rights reserved.
 * This software is the proprietary information of AEA.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.process;

import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.shared.AccessLogSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 *
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 10. 18.
 */
public interface AccessLogService {

    void registerAccessLog(AccessLog accessLog);
    
    Page<AccessLog> retrieveAccessLogBySearchCond(AccessLogSearchCond searchCond);

}

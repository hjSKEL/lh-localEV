/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.dao;

import org.springframework.stereotype.Repository;

/**
 * Sequence 조회 Mapper.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 19.
 */
@Repository
public interface SequenceMapper {

    Integer selectRemoteStartSeq();

    Integer selectReservSeq();

    Integer selectMonitoringReportSeq();

    Integer selectDisplayMessagesSeq();

    Integer selectDisplayMessageSeq();

    Integer selectCustomerInformationSeq();

    Integer selectFirmwareSeq();

    Integer selectLogSeq();

    Integer selectChargingProfileSeq();
}

/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.process;

/**
 * Sequence 생성 서비스.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 19.
 */
public interface SequenceService {

    Integer generateRemoteStartSeq();

    Integer generateReservSeq();

    Integer generateMonitoringReportSeq();

    Integer generateDisplayMessagesSeq();

    Integer generateDisplayMessageSeq();

    Integer generateCustomerInformationSeq();

    Integer generateFirmwareSeq();

    Integer generateLogSeq();

    Integer generateChargingProfileSeq();
}

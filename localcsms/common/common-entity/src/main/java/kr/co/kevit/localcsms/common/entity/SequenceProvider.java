/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity;

/**
 * Sequence 조회 Provider.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 19.
 */
public interface SequenceProvider {

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

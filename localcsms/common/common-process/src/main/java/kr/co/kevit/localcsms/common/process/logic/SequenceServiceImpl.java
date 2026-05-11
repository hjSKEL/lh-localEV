/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.process.logic;

import kr.co.kevit.localcsms.common.entity.SequenceProvider;
import kr.co.kevit.localcsms.common.process.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Sequence 생성 서비스 구현체.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 19.
 */
@Service
@Transactional
public class SequenceServiceImpl implements SequenceService {

    @Autowired
    private SequenceProvider provider;

    @Override
    public Integer generateRemoteStartSeq() {
        return provider.selectRemoteStartSeq();
    }

    @Override
    public Integer generateReservSeq() {
        return provider.selectReservSeq();
    }

    @Override
    public Integer generateMonitoringReportSeq() {
        return provider.selectMonitoringReportSeq();
    }

    @Override
    public Integer generateDisplayMessagesSeq() {
        return provider.selectDisplayMessagesSeq();
    }

    @Override
    public Integer generateDisplayMessageSeq() {
        return provider.selectDisplayMessageSeq();
    }

    @Override
    public Integer generateCustomerInformationSeq() {
        return provider.selectCustomerInformationSeq();
    }

    @Override
    public Integer generateFirmwareSeq() {
        return provider.selectFirmwareSeq();
    }

    @Override
    public Integer generateLogSeq() {
        return provider.selectLogSeq();
    }

    @Override
    public Integer generateChargingProfileSeq() {
        return provider.selectChargingProfileSeq();
    }
}

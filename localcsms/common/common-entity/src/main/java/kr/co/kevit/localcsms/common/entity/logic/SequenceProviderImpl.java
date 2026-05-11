/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.logic;

import kr.co.kevit.localcsms.common.entity.SequenceProvider;
import kr.co.kevit.localcsms.common.entity.dao.SequenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Sequence 조회 Provider 구현체.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 19.
 */
@Repository
public class SequenceProviderImpl implements SequenceProvider {

    @Autowired
    private SequenceMapper mapper;

    @Override
    public Integer selectRemoteStartSeq() {
        return mapper.selectRemoteStartSeq();
    }

    @Override
    public Integer selectReservSeq() {
        return mapper.selectReservSeq();
    }

    @Override
    public Integer selectMonitoringReportSeq() {
        return mapper.selectMonitoringReportSeq();
    }

    @Override
    public Integer selectDisplayMessagesSeq() {
        return mapper.selectDisplayMessagesSeq();
    }

    @Override
    public Integer selectDisplayMessageSeq() {
        return mapper.selectDisplayMessageSeq();
    }

    @Override
    public Integer selectCustomerInformationSeq() {
        return mapper.selectCustomerInformationSeq();
    }

    @Override
    public Integer selectFirmwareSeq() {
        return mapper.selectFirmwareSeq();
    }

    @Override
    public Integer selectLogSeq() {
        return mapper.selectLogSeq();
    }

    @Override
    public Integer selectChargingProfileSeq() {
        return mapper.selectChargingProfileSeq();
    }
}
